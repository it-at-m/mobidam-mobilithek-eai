package de.muenchen.mobidam.mobilithek;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.config.Interfaces;
import de.muenchen.mobidam.integration.client.domain.DatentransferCreateDTO;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InterfaceMessageFactory {

    private Interfaces interfaceProps;

    public void parkRideDataStart(Exchange exchange) {

        var dto = new DatentransferCreateDTO();
        dto.setEreignis(EreignisTyp.BEGINN.name());
        dto.setZeitstempel(LocalDateTime.now());
        dto.setProzessId(exchange.getExchangeId());
        dto.setSchnittstelle(interfaceProps.getInterfaces().get(Constants.PARK_RIDE_DATA).getMobidamSstId());
        dto.setInfo("Interface start.");
        exchange.getMessage().setBody(dto);
    }

    public void parkRideDataSuccess(Exchange exchange) {

        var dto = new DatentransferCreateDTO();
        dto.setEreignis(EreignisTyp.ERFOLG.name());
        dto.setZeitstempel(LocalDateTime.now());
        dto.setProzessId(exchange.getExchangeId());
        dto.setSchnittstelle(interfaceProps.getInterfaces().get(Constants.PARK_RIDE_DATA).getMobidamSstId());
        dto.setInfo(String.format("Interface status code : '%s' (%s)", exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE),
                exchange.getIn().getHeader(AWS2S3Constants.KEY)));
        exchange.getMessage().setBody(dto);
    }

    public void parkRideDataError(Exchange exchange) {

        var dto = new DatentransferCreateDTO();
        dto.setEreignis(EreignisTyp.FEHLER.name());
        dto.setZeitstempel(LocalDateTime.now());
        dto.setProzessId(exchange.getExchangeId());
        dto.setSchnittstelle(interfaceProps.getInterfaces().get(Constants.PARK_RIDE_DATA).getMobidamSstId());
        var ex = exchange.getException() != null ? exchange.getException() : (Exception) exchange.getAllProperties().get(Exchange.EXCEPTION_CAUGHT);
        dto.setInfo(String.format("End interface with error : %s", ex.getMessage()));
        exchange.getMessage().setBody(dto);
    }

    public void parkRideDataWarn(Exchange exchange) {

        var dto = new DatentransferCreateDTO();
        dto.setEreignis(EreignisTyp.WARNUNGEN.name());
        dto.setZeitstempel(LocalDateTime.now());
        dto.setProzessId(exchange.getExchangeId());
        dto.setSchnittstelle(interfaceProps.getInterfaces().get(Constants.PARK_RIDE_DATA).getMobidamSstId());
        dto.setInfo("Interface is not activated");
        exchange.getMessage().setBody(dto);
    }

    public void parkRideDataEnd(Exchange exchange) {

        var dto = new DatentransferCreateDTO();
        dto.setEreignis(EreignisTyp.ENDE.name());
        dto.setZeitstempel(LocalDateTime.now());
        dto.setProzessId(exchange.getExchangeId());
        dto.setSchnittstelle(interfaceProps.getInterfaces().get(Constants.PARK_RIDE_DATA).getMobidamSstId());
        dto.setInfo("Interface end");
        exchange.getMessage().setBody(dto);
    }

}
