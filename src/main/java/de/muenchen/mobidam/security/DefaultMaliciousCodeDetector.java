package de.muenchen.mobidam.security;

import org.owasp.encoder.Encode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultMaliciousCodeDetector {

    public boolean isValidData(final File file) throws Exception { // TODO: check param type
        return isValidInput(Files.readString(Path.of(file.getPath()))); // TODO
    }

    protected boolean isValidInput(final String content){
        String clean = Encode.forHtml(content);
        return content.equals(clean);
        }


}
