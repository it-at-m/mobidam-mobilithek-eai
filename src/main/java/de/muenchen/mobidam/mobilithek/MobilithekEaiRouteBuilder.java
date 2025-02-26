/*
 * The MIT License
 * Copyright © 2024 Landeshauptstadt München | it@M
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.muenchen.mobidam.mobilithek;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.eai.common.CommonConstants;
import de.muenchen.mobidam.exception.MobidamSecurityException;
import de.muenchen.mobidam.s3.S3ObjectPathBuilder;
import javax.net.ssl.SSLException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.impl.engine.DefaultStreamCachingStrategy;
import org.apache.camel.spi.StreamCachingStrategy;
import org.springframework.stereotype.Component;

@Component
public class MobilithekEaiRouteBuilder extends RouteBuilder {

    public static final String MOBIDAM_S3_ROUTE = "direct:mobilithek-info";

    public static final String MOBIDAM_ROUTE_ID = "Interface-Mobilithek-Info";
    public static final String MOBIDAM_ENDPOINT_S3_ID = "Endpoint-S3";
    public static final String MOBIDAM_ENDPOINT_S3_QUARANTINE_ID = "Endpoint-S3-Quarantine";

    @Override
    public void configure() {

        StreamCachingStrategy strategy = new DefaultStreamCachingStrategy();
        strategy.setSpoolEnabled(true);

        CamelContext context = getContext();
        context.setStreamCachingStrategy(strategy);
        context.setStreamCaching(true);

        // spotless:off
        onException(MobidamSecurityException.class)
                .handled(true)
                .process(exchange -> {
                    var mobilithekInterface = exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class);
                    exchange.getIn().setHeader(AWS2S3Constants.KEY, S3ObjectPathBuilder.buildQuarantinePath(mobilithekInterface));
                })
                .toD("aws2-s3://${header.bucketName}?accessKey=RAW(${header.accessKey})&secretKey=RAW(${header.secretKey})&region=${properties:camel.component.aws2-s3.region}&overrideEndpoint=true&uriEndpointOverride=${properties:camel.component.aws2-s3.override-endpoint}").id(MOBIDAM_ENDPOINT_S3_QUARANTINE_ID)
                .log(LoggingLevel.INFO, "Moved to quarantine: ${header." + AWS2S3Constants.KEY + "}")
                .to("direct:handleError")
        ;

        onException(Exception.class, SSLException.class)
                .handled(true)
                .to("direct:handleError")
        ;

        from(MOBIDAM_S3_ROUTE)
            .routeId(MOBIDAM_ROUTE_ID)
            .toD(String.format("metrics:counter:mobidam_sst_${header.%s.identifier}_total", Constants.INTERFACE_TYPE))
            .bean("sstManagementIntegrationServiceFacade", "isActivated").id("sstManagementIntegrationServiceFacade.isActivated")
            .choice().when(simple("${body} == 'TRUE'")).id("choice.isActivated")
                .bean("interfaceMessageFactory", "mobilithekMessageStart").id("interfaceMessageFactory.start")
                .bean("sstManagementIntegrationServiceFacade", "logDatentransfer").id("sstManagementIntegrationServiceFacade.logTransfer.start")
                .setBody(simple("${null}"))
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .toD(String.format("${header.%s.mobilithekUrl}", Constants.INTERFACE_TYPE)).id("mobilithek.request")
                .setHeader(CommonConstants.HEADER_BUCKET_NAME, simple(String.format("${header.%s.s3Bucket}", Constants.INTERFACE_TYPE)))
                .process("s3CredentialProvider").id("s3CredentialProvider")
                .process("resourceTypeProcessor").id("resourceTypeProcessor")
                .toD(String.format("metrics:timer:mobidam_sst_${header.%s.identifier}_codedetection_seconds?action=start", Constants.INTERFACE_TYPE))
                .process("codeDetectionProcessor").id("codeDetectionProcessor")
                .toD(String.format("metrics:timer:mobidam_sst_${header.%s.identifier}_codedetection_seconds?action=stop", Constants.INTERFACE_TYPE))
                .process("s3ObjectKeyProvider").id("s3ObjectKeyProvider")
                .process("fileSizeProcessor").id("fileSizeProcessor")
                .toD("aws2-s3://${header.bucketName}?accessKey=RAW(${header.accessKey})&secretKey=RAW(${header.secretKey})&region=${properties:camel.component.aws2-s3.region}&overrideEndpoint=true&uriEndpointOverride=${properties:camel.component.aws2-s3.override-endpoint}").id(MOBIDAM_ENDPOINT_S3_ID)
                .bean("interfaceMessageFactory", "mobilithekMessageSuccess")
                .bean("sstManagementIntegrationService", "logDatentransfer").id("sstManagementIntegrationServiceFacade.logTransfer.success")
                .bean("interfaceMessageFactory", "mobilithekMessageEnd")
                .bean("sstManagementIntegrationService", "logDatentransfer").id("sstManagementIntegrationServiceFacade.logTransfer.end")
                .toD(String.format("metrics:counter:mobidam_sst_${header.%s.identifier}_success_total", Constants.INTERFACE_TYPE))
            .otherwise()
                .log(LoggingLevel.DEBUG, Constants.MOBIDAM_LOGGER, String.format("${header.%s.mobidamSstId} is not active.", Constants.INTERFACE_TYPE))
            .end()
            ;

        from("direct:handleError")
            .routeId("Error-Handler")
            .bean("interfaceMessageFactory", "mobilithekMessageError")
            .bean("sstManagementIntegrationService", "logDatentransfer")
            .log(LoggingLevel.ERROR, "${exception}")
            .bean("interfaceMessageFactory", "mobilithekMessageEnd")
            .bean("sstManagementIntegrationService", "logDatentransfer")
            ;
        //  spotless:on

    }

}
