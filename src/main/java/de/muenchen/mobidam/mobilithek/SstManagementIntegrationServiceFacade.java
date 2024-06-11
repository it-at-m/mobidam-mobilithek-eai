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
