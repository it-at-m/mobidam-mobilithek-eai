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
import javax.net.ssl.SSLException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.springframework.stereotype.Component;

@Component
public class MobilithekEaiRouteBuilder extends RouteBuilder {

    public static final String MOBIDAM_S3_ROUTE = "direct:mobilithek-info";

    public static final String MOBIDAM_ROUTE_ID = "Interface-Mobilithek-Info";
    public static final String MOBIDAM_ENDPOINT_S3_ID = "Endpoint-S3";

    @Override
    public void configure() {

        onException(Exception.class, SSLException.class)
                .handled(true)
                .bean("interfaceMessageFactory", "parkRideDataError")
                .bean("sstManagementIntegrationService", "logDatentransfer")
                .log(LoggingLevel.ERROR, "${exception}")
                .bean("interfaceMessageFactory", "parkRideDataEnd")
                .bean("sstManagementIntegrationService", "logDatentransfer");

        // spotless:off
        from(MOBIDAM_S3_ROUTE)
                .routeId(MOBIDAM_ROUTE_ID)
                .bean("sstManagementIntegrationServiceFacade", "isActivated")
                .choice().when(simple("${body} == 'TRUE'"))
                    .bean("interfaceMessageFactory", "parkRideDataStart")
                    .bean("sstManagementIntegrationServiceFacade", "logDatentransfer")
                    .setBody(simple("${null}"))
                    .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                    .toD(String.format("${header.%s.mobilithekUrl}", Constants.INTERFACE_TYPE))
                    .setHeader(Constants.PARAMETER_BUCKET_NAME, simple(String.format("${header.%s.s3Bucket}", Constants.INTERFACE_TYPE)))
                    .process("s3CredentialProvider")
                    .process("s3ObjectKeyBuilder")
                    .toD("aws2-s3://$simple{header.bucketName}?accessKey=RAW($simple{header.accessKey})&secretKey=RAW($simple{header.secretKey})&region=${camel.component.aws2-s3.region}&overrideEndpoint=true&uriEndpointOverride=${camel.component.aws2-s3.override-endpoint}").id(MOBIDAM_ENDPOINT_S3_ID)
                    .bean("interfaceMessageFactory", "parkRideDataSuccess")
                    .bean("sstManagementIntegrationService", "logDatentransfer")
                    .bean("interfaceMessageFactory", "parkRideDataEnd")
                    .bean("sstManagementIntegrationService", "logDatentransfer")
                .end();
        //  spotless:on

    }

}