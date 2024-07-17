package de.muenchen.mobidam.security;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeDetectionProcessor implements Processor {

    private final CodeDetectorFactory codeDetectorFactory;
    @Override
    public void process(Exchange exchange) throws Exception {
        var mobilithekInterface = exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class);
        File file = exchange.getIn().getBody(File.class); // TODO
        DefaultMaliciousCodeDetector codeDetector = codeDetectorFactory.getCodeDetector(mobilithekInterface.getMimeType());
        boolean result = codeDetector.isValidData(file); // from exchange
        if (!result){
            log.warn("XSS attack detected: {}", file);
            // TODO: quarantäne
        }
    }
}
