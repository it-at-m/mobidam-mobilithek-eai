package de.muenchen.mobidam.security;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.exception.MobidamSecurityException;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.converter.stream.InputStreamCache;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class MimeTypeChecker implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        var mobilithekInterface = exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class);
        InputStreamCache stream = exchange.getMessage().getBody(InputStreamCache.class);
        stream.reset();
        log.debug("Checking mime type of content for interface {}", mobilithekInterface.getName());
        boolean result = check(stream, mobilithekInterface.getAllowedMimeTypes());
        if (!result) {
            throw new MobidamSecurityException("Illegal MIME type detected in interface: " + mobilithekInterface.getName());
        }
    }

    private boolean check(final InputStreamCache stream, final List<String> allowedMimeTypes) throws IOException {

        //        Metadata metadata = new Metadata();
        //        InputStream input = TikaInputStream.get(content);
        //        MediaType mimetype = tika.getDetector().detect(input, metadata);
        String mimetype = getMimeTypeSimple(stream);
        log.debug("File is of mime type: {}", mimetype);
        return allowedMimeTypes.contains(mimetype);
    }

    private String getMimeTypeSimple(final InputStream stream) {
        Tika tika = new Tika();
        //        String mimeType = tika.detect(stream);
        String mimeType = null;
        try {
            mimeType = getMimeType(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return mimeType;
    }

    private String getMimeType(final InputStream stream) throws TikaException, IOException, SAXException {
        Parser parser = new AutoDetectParser();

        BodyContentHandler handler = new BodyContentHandler();

        Metadata metadata = new Metadata();

        ParseContext ctx = null;
        parser.parse(stream, handler, metadata, ctx);

        MediaType mediaType = MediaType.parse(metadata.get(Metadata.CONTENT_TYPE));

        return mediaType.toString();
    }

}
