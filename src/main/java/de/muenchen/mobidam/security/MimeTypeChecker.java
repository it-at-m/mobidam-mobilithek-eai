package de.muenchen.mobidam.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class MimeTypeChecker {

    private final Tika tika = new Tika();

    public boolean check(final InputStream stream, final List<String> allowedMimeTypes) throws IOException {

        String mimetype = getMimeType(stream);
        log.debug("File is of mime type: {}", mimetype);
        return allowedMimeTypes.contains(mimetype);
    }

    private String getMimeType(final InputStream stream) throws IOException {
        return tika.detect(stream);
    }

}
