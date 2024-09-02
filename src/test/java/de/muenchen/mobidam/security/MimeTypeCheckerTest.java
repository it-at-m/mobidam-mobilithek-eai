package de.muenchen.mobidam.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class MimeTypeCheckerTest {

    private final MimeTypeChecker mimeTypeChecker = new MimeTypeChecker();

    @Test
    public void testCheckWithValidPlainText() throws IOException {
        Assertions.assertTrue(mimeTypeChecker.check(new ByteArrayInputStream("test content with plain text".getBytes()), List.of("text/plain")));
    }

    @Test
    public void testCheckWithValidXml() throws IOException {
        Assertions.assertTrue(mimeTypeChecker.check(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>test content</root".getBytes()),
                List.of("application/xml")));
    }

    @Test
    public void testCheckWithInvalidMimeType() throws IOException {
        Assertions.assertFalse(mimeTypeChecker.check(new ByteArrayInputStream("test content with plain text".getBytes()), List.of("application/xml")));
    }
}
