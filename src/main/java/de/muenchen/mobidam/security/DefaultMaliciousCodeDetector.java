package de.muenchen.mobidam.security;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Component
public class DefaultMaliciousCodeDetector implements MaliciousCodeDetector {

    public boolean isValidData(final InputStream stream) throws Exception {
        return isValidInput(new String(stream.readAllBytes()));
    }

    protected boolean isValidInput(final String content){
        String clean = Encode.forHtml(content);
        return content.equals(clean);
        }


}
