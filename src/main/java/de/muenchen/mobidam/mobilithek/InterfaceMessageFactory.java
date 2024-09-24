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
import de.muenchen.mobidam.integration.client.domain.DatentransferCreateDTO;
import de.muenchen.mobidam.sstmanagment.EreignisTyp;
import java.time.LocalDateTime;

import io.micrometer.core.instrument.Metrics;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InterfaceMessageFactory {

    public void mobilithekMessageStart(Exchange exchange) {

        Metrics.counter("mobidam.exchanges.ereignis.beginn.counter").increment();

        var dto = new DatentransferCreateDTO();
        dto.setEreignis(EreignisTyp.BEGINN.name());
        dto.setZeitstempel(LocalDateTime.now());
        dto.setProzessId(exchange.getExchangeId());
        dto.setSchnittstelle(exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class).getMobidamSstId());
        exchange.getMessage().setBody(dto);
    }

    public void mobilithekMessageSuccess(Exchange exchange) {

        Metrics.counter("mobidam.exchanges.ereignis.erfolg.counter").increment();

        var dto = new DatentransferCreateDTO();
        dto.setEreignis(EreignisTyp.ERFOLG.name());
        dto.setZeitstempel(LocalDateTime.now());
        dto.setProzessId(exchange.getExchangeId());
        dto.setSchnittstelle(exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class).getMobidamSstId());
        dto.setInfo(String.format("Interface status code : '%s' (%s)", exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE),
                exchange.getIn().getHeader(AWS2S3Constants.KEY)));
        exchange.getMessage().setBody(dto);
    }

    public void mobilithekMessageError(Exchange exchange) {

        Metrics.counter("mobidam.exchanges.ereignis.fehler.counter").increment();

        var dto = new DatentransferCreateDTO();
        dto.setEreignis(EreignisTyp.FEHLER.name());
        dto.setZeitstempel(LocalDateTime.now());
        dto.setProzessId(exchange.getExchangeId());
        dto.setSchnittstelle(exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class).getMobidamSstId());
        var ex = exchange.getException() != null ? exchange.getException() : (Exception) exchange.getAllProperties().get(Exchange.EXCEPTION_CAUGHT);
        dto.setInfo(String.format("End interface with error : %s", ex.getMessage()));
        exchange.getMessage().setBody(dto);
    }

    public void mobilithekMessageEnd(Exchange exchange) {

        Metrics.counter("mobidam.exchanges.ereignis.ende.counter").increment();

        var dto = new DatentransferCreateDTO();
        dto.setEreignis(EreignisTyp.ENDE.name());
        dto.setZeitstempel(LocalDateTime.now());
        dto.setProzessId(exchange.getExchangeId());
        dto.setSchnittstelle(exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class).getMobidamSstId());
        exchange.getMessage().setBody(dto);
    }

}
