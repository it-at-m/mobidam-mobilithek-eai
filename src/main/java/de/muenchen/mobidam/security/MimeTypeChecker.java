package de.muenchen.mobidam.security;

import de.muenchen.mobidam.Constants;
import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Service;
import org.xml.sax.ext.DeclHandler;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@Service
@Slf4j
public class MimeTypeChecker implements Processor {

    private TikaConfig tika;

    @PostConstruct
    public void init() throws TikaException, IOException {
        this.tika = new TikaConfig();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        var mobilithekInterface = exchange.getIn().getHeader(Constants.INTERFACE_TYPE, InterfaceDTO.class);
        Object body = exchange.getMessage().getBody();
        boolean result = check(exchange.getMessage(File.class), mobilithekInterface.getMimeType()); // TODO
        if (!result){
            // TODO: quarantäne
        }
    }

    private boolean check(final File file, final String expectedMimetype) throws IOException {

        log.debug("Checking mime type of ..."); // TODO
        Metadata metadata = new Metadata();
        InputStream input = TikaInputStream.get(Path.of("C:\\Users\\martin.dietrich\\tmp\\mdas-scan\\test2.xml")); // TODO
        MediaType mimetype = tika.getDetector().detect(input, metadata);
        log.debug("File is of mime type: {}", mimetype);
        return expectedMimetype.equals(mimetype.getType());
    }

}
