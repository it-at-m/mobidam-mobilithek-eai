package de.muenchen.mobidam.security;

import java.io.InputStream;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Component;


@Component
public class DefaultMaliciousCodeDetector implements MaliciousCodeDetector {

    public boolean isValidData(final InputStream stream) throws Exception {
        return isValidInput(new String(stream.readAllBytes()));
    }

    protected boolean isValidInput(final String content) {
        String clean = Encode.forHtml(content);
        return content.equals(clean);
    }

}
