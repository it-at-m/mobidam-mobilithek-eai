package de.muenchen.mobidam.mobilithek;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.integration.client.domain.DatentransferCreateDTO;
import de.muenchen.mobidam.integration.service.SstManagementIntegrationService;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SstManagementIntegrationServiceFacade {

    @Autowired
    private SstManagementIntegrationService service;

    public void isActivated(Exchange exchange) throws Exception {
        var mobilithekInterface = exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class);
        exchange.getIn().setBody(service.isActivated(mobilithekInterface.getMobidamSstId().toString()));
    }

    public void logDatentransfer(Exchange exchange) throws Exception {
        service.logDatentransfer(exchange.getIn().getBody(DatentransferCreateDTO.class));
    }

}
