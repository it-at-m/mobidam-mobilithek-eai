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
package de.muenchen.mobidam.security;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.config.ResourceTypes;
import de.muenchen.mobidam.exception.MobidamSecurityException;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.StreamCache;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceTypeProcessor implements Processor {

    private final ResourceTypeChecker resourceTypeChecker;

    private final ResourceTypes resourceTypes;

    @Override
    public void process(Exchange exchange) throws Exception {
        var mobilithekInterface = exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class);
        if (mobilithekInterface.getAllowedResourceTypes() == null) {
            return;
        }
        StreamCache receivedStream = exchange.getIn().getBody(StreamCache.class);
        receivedStream.reset();
        InputStream stream = (InputStream) receivedStream;
        log.debug("Checking mime type of content for interface {}", mobilithekInterface.getName());
        boolean result = resourceTypeChecker.check(stream,
                resourceTypes.getResourceTypes(mobilithekInterface.getAllowedResourceTypes()), exchange);
        if (!result) {
            throw new MobidamSecurityException("Illegal MIME type detected in interface: " + mobilithekInterface.getName());
        }
    }

}
