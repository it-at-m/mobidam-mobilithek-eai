package de.muenchen.mobidam.security;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.converter.stream.InputStreamCache;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeDetectionProcessor implements Processor {

    private final CodeDetectorFactory codeDetectorFactory;
    @Override
    public void process(Exchange exchange) throws Exception {
        var mobilithekInterface = exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class);
        InputStreamCache stream = exchange.getIn().getBody(InputStreamCache.class);
        stream.reset();
        MaliciousCodeDetector codeDetector = codeDetectorFactory.getCodeDetector(mobilithekInterface.getAllowedMimeTypes().get(0)); // TODO
        boolean result = codeDetector.isValidData(stream); // from exchange
        if (!result){
            log.warn("XSS attack detected: {}", mobilithekInterface.getName());
            // TODO: quarantäne
        }
    }
}
