package de.muenchen.mobidam.security;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.exception.MobidamSecurityException;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import org.apache.camel.Exchange;
import org.apache.camel.converter.stream.InputStreamCache;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

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

    private Exchange createExchange(List<String> mimeTypes) {
        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        InterfaceDTO interfaceDTO = new InterfaceDTO();
        interfaceDTO.setMaliciousCodeDetectionEnabled(true);
        interfaceDTO.setAllowedMimeTypes(mimeTypes);
        exchange.getIn().setHeader(Constants.INTERFACE_TYPE, interfaceDTO);
        return exchange;
    }

}
