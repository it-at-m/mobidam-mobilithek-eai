package de.muenchen.mobidam.security;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.exception.MobidamSecurityException;
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
        if (!mobilithekInterface.getMaliciousCodeDetectionEnabled()) {
            return;
        }
        InputStreamCache stream = exchange.getIn().getBody(InputStreamCache.class);
        stream.reset();
        MaliciousCodeDetector codeDetector = codeDetectorFactory.getCodeDetector(mobilithekInterface.getAllowedMimeTypes().get(0));
        boolean result = false;
        try {
            result = codeDetector.isValidData(stream); // from exchange
        } catch (Exception ex) {
            log.warn("Malicious code detection failed", ex);
        }
        if (!result) {
            log.warn("Possible malicious code detected: {}", mobilithekInterface.getName());
            throw new MobidamSecurityException("Possible malicious code detected in interface: " + mobilithekInterface.getName());
        }
    }
}
