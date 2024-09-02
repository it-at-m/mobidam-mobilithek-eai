package de.muenchen.mobidam.security;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.exception.MobidamSecurityException;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.converter.stream.InputStreamCache;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MimeTypeProcessor implements Processor {

    private final MimeTypeChecker mimeTypeChecker;

    @Override
    public void process(Exchange exchange) throws Exception {
        var mobilithekInterface = exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class);
        if (mobilithekInterface.getAllowedMimeTypes() == null) {
            return;
        }
        InputStreamCache stream = exchange.getMessage().getBody(InputStreamCache.class);
        stream.reset();
        log.debug("Checking mime type of content for interface {}", mobilithekInterface.getName());
        boolean result = mimeTypeChecker.check(stream, mobilithekInterface.getAllowedMimeTypes());
        if (!result) {
            throw new MobidamSecurityException("Illegal MIME type detected in interface: " + mobilithekInterface.getName());
        }
    }

}
