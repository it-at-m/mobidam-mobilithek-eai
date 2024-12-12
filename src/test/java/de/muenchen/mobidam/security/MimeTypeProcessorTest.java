/*
 * The MIT License
 * Copyright © 2024 Landeshauptstadt München | it@M
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.muenchen.mobidam.security;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.exception.MobidamSecurityException;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.converter.stream.FileInputStreamCache;
import org.apache.camel.converter.stream.InputStreamCache;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.tika.metadata.Metadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class MimeTypeProcessorTest {

    private static final String TEXT_CONTENT = "test content with plain text";
    private static final String XML_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>test content</root>";

    private final MimeTypeProcessor processor = new MimeTypeProcessor(new MimeTypeChecker());

    @Test
    public void testCheckWithValidPlainText() throws Exception {
        Exchange exchange = createExchange(List.of(MediaType.TEXT_PLAIN_VALUE));
        InputStreamCache stream = new InputStreamCache(TEXT_CONTENT.getBytes());
        exchange.getIn().setBody(stream);
        processor.process(exchange);
    }

    @Test
    public void testCheckWithValidXml() throws Exception {
        Exchange exchange = createExchange(List.of(MediaType.APPLICATION_XML_VALUE));
        InputStreamCache stream = new InputStreamCache(XML_CONTENT.getBytes());
        exchange.getIn().setBody(stream);
        processor.process(exchange);
    }

    @Test
    public void testCheckWithValidCSVNoContentType() throws Exception {
        Exchange exchange = createExchange(List.of(MimeTypeChecker.TEXT_CSV_TYPE.toString()));
        FileInputStreamCache stream = new FileInputStreamCache(new File("src/test/resources/testdata/ladesaulen-example.csv"));
        exchange.getIn().setBody(stream);
        processor.process(exchange);
    }

    @Test
    public void testCheckWithValidCSVContentTypeAvailable() throws Exception {
        Exchange exchange = createExchange(List.of(MimeTypeChecker.TEXT_CSV_TYPE.toString()));
        exchange.getIn().setHeader(Metadata.CONTENT_TYPE, "text/csv;UTF-8");
        FileInputStreamCache stream = new FileInputStreamCache(new File("src/test/resources/testdata/ladesaulen-less-10-rows-example.csv"));
        exchange.getIn().setBody(stream);
        processor.process(exchange);
    }

    @Test
    public void testCheckWithValidXml2() throws Exception {
        Exchange exchange = createExchange(List.of(MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_XML_VALUE));
        try (InputStream resStream = this.getClass().getResourceAsStream("/testdata/valid.xml")) {
            if (resStream == null) throw new IOException("Resource not found: /testdata/valid.xml");
            InputStreamCache stream = new InputStreamCache(resStream.readAllBytes());
            exchange.getIn().setBody(stream);
            processor.process(exchange);
        }
    }

    @Test
    public void testCheckWithInvalidMimeType() {
        Exchange exchange = createExchange(List.of(MediaType.APPLICATION_XML_VALUE));
        InputStreamCache stream = new InputStreamCache(TEXT_CONTENT.getBytes());
        exchange.getIn().setBody(stream);
        Assertions.assertThrows(MobidamSecurityException.class, () -> processor.process(exchange));
    }

    @Test
    public void testCheckWithInvalidMimeType2() {
        Exchange exchange = createExchange(List.of(MediaType.TEXT_PLAIN_VALUE));
        InputStreamCache stream = new InputStreamCache(XML_CONTENT.getBytes());
        exchange.getIn().setBody(stream);
        Assertions.assertThrows(MobidamSecurityException.class, () -> processor.process(exchange));
    }

    @Test
    public void testCheckWithInvalidMimeTypeCSV() {
        Exchange exchange = createExchange(List.of(MimeTypeChecker.TEXT_CSV_TYPE.toString()));
        FileInputStreamCache stream = new FileInputStreamCache(new File("src/test/resources/testdata/valid.xml"));
        exchange.getIn().setBody(stream);
        Assertions.assertThrows(MobidamSecurityException.class, () -> processor.process(exchange));
    }

    @Test
    public void testCheckWithNotAllowedMimeTypeCSV() {
        Exchange exchange = createExchange(List.of(MimeTypeChecker.TEXT_CSV_TYPE.toString()));
        exchange.getIn().setHeader(Metadata.CONTENT_TYPE, "invalid/csv;UTF-8");
        FileInputStreamCache stream = new FileInputStreamCache(new File("src/test/resources/testdata/ladesaulen-example.csv"));
        exchange.getIn().setBody(stream);
        Assertions.assertThrows(MobidamSecurityException.class, () -> processor.process(exchange));
    }

    @Test
    public void testCheckWithInvalidCSVFileContentToSmall() throws Exception {
        Exchange exchange = createExchange(List.of(MimeTypeChecker.TEXT_CSV_TYPE.toString()));
        FileInputStreamCache stream = new FileInputStreamCache(new File("src/test/resources/testdata/ladesaulen-less-10-rows-example.csv"));
        exchange.getIn().setBody(stream);
        Assertions.assertThrows(MobidamSecurityException.class, () -> processor.process(exchange));
    }

    private Exchange createExchange(List<String> mimeTypes) {
        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        InterfaceDTO interfaceDTO = new InterfaceDTO();
        interfaceDTO.setMaliciousCodeDetectionEnabled(true);
        interfaceDTO.setAllowedMimeTypes(mimeTypes);
        exchange.getIn().setHeader(Constants.INTERFACE_TYPE, interfaceDTO);
        return exchange;
    }

}
