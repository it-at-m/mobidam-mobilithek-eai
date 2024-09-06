package de.muenchen.mobidam.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class MimeTypeCheckerTest {

    private static final String TEXT_CONTENT = "test content with plain text";
    private static final String XML_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>test content</root>";

    private final MimeTypeChecker mimeTypeChecker = new MimeTypeChecker();

    @Test
    public void testCheckWithValidPlainText() throws IOException {
        Assertions.assertTrue(mimeTypeChecker.check(new ByteArrayInputStream(TEXT_CONTENT.getBytes()), List.of("text/plain")));
    }

    @Test
    public void testCheckWithValidXml() throws IOException {
        Assertions.assertTrue(mimeTypeChecker.check(new ByteArrayInputStream(XML_CONTENT.getBytes()),
                List.of("application/xml")));
    }

    @Test
    public void testCheckWithInvalidMimeType() throws IOException {
        Assertions.assertFalse(mimeTypeChecker.check(new ByteArrayInputStream(TEXT_CONTENT.getBytes()), List.of("application/xml")));
    }

    @Test
    public void testCheckWithInvalidMimeType2() throws IOException {
        Assertions.assertFalse(mimeTypeChecker.check(new ByteArrayInputStream(XML_CONTENT.getBytes()), List.of("text/plain")));
    }
}
