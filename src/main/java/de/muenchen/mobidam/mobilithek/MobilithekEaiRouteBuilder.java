/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
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

    @Override
    public void configure() {

        onException(Exception.class, SSLException.class)
                .handled(true)
                .bean("interfaceMessageFactory", "parkRideDataError")
                .bean("sstManagementIntegrationService", "logDatentransfer")
                .log(LoggingLevel.ERROR, "${exception}");

        from(MOBIDAM_S3_ROUTE)
                .routeId("Interface-Mobilithek-Info")
                .log(LoggingLevel.INFO, Constants.MOBIDAM_LOGGER, "Mobilithek-Info is requested.")
                .bean("interfaceMessageFactory", "parkRideDataStart")
                .bean("sstManagementIntegrationServiceFacade", "logDatentransfer")
                .bean("sstManagementIntegrationServiceFacade", "isActivated")
                .choice().when(simple("${body} == 'TRUE'"))
                .setBody(simple("${null}"))
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .toD(String.format("${header.%s.mobilithekUrl}", Constants.INTERFACE_TYPE))
                .setHeader(Constants.PARAMETER_BUCKET_NAME, simple(String.format("${header.%s.s3Bucket}", Constants.INTERFACE_TYPE)))
                .process("s3CredentialProvider")
                .process("s3ObjectKey")
                .toD("{{mobidam.s3.url}}")
                .bean("interfaceMessageFactory", "parkRideDataSuccess")
                .bean("sstManagementIntegrationService", "logDatentransfer")
                .otherwise()
                .bean("interfaceMessageFactory", "parkRideDataWarn")
                .bean("sstManagementIntegrationService", "logDatentransfer")
                .log(LoggingLevel.INFO, Constants.MOBIDAM_LOGGER, "Interface-P+R is DEACTIVATED. More information see SST-Management.")
                .end()
                .bean("interfaceMessageFactory", "parkRideDataEnd")
                .bean("sstManagementIntegrationService", "logDatentransfer");
    }

}
