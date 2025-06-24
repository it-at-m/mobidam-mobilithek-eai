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

import de.muenchen.mobidam.sstmanagment.DurationLog;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.csv.TextAndCSVParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;

@Service
@Slf4j
public class ResourceTypeChecker {

    public static final MediaType BINARY_CSV_TYPE = new MediaType("binary", "octet-stream");

    public static final MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");

    private final Tika tika = new Tika();

    private final DurationLog parseDuration = new DurationLog("Tika-Mime-Type-Detection");

    public boolean check(final InputStream stream, final List<String> allowedMimeTypes, final Exchange exchange)
            throws IOException {

        log.debug("Entered Check Method");
        var contentType = exchange.getIn().getHeader(Exchange.CONTENT_TYPE, String.class);
        log.debug("Got header content type");
        if (contentType != null && allowedMimeTypes.stream().filter(contentType::contains).findAny().isEmpty()) {
            log.error("Mimetype is not allowed: {}", contentType);
            return false;
        }
        log.debug("Checking detected type");
        String detectedMimeType = getResourceType(stream, contentType, exchange);
        if (detectedMimeType != null && allowedMimeTypes.stream().filter(detectedMimeType::contains).findAny().isEmpty()) {
            log.error("Mimetype is not allowed: {}", detectedMimeType);
            return false;
        }
        return true;

    }

    private String getResourceType(final InputStream stream, String contentType, Exchange exchange) throws IOException {

        log.debug("Getting resource type");
        if (contentType != null && contentType.contains(TEXT_CSV_TYPE.toString())) {
            log.debug("CSV Branch");
            ContentHandler handler = new BodyContentHandler(-1);
            TextAndCSVParser parser = new TextAndCSVParser();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            log.debug("Strating parser");
            try {
                parseDuration.startDebug();
                parser.parse(stream, handler, metadata, context);
                parseDuration.endDebug();
            } catch (Exception e) {
                throw new IOException(e);
            }
            log.debug("Exit Parser");
            log.debug("Tika file metadata {}", metadata);
            String tikaContentType = metadata.get(Metadata.CONTENT_TYPE);
            if (tikaContentType.toLowerCase().contains(TEXT_CSV_TYPE.toString())) {
                exchange.getIn().setHeader(TextAndCSVParser.DELIMITER_PROPERTY.getName(), metadata.get(TextAndCSVParser.DELIMITER_PROPERTY));
                return TEXT_CSV_TYPE.toString();
            } else {
                log.warn("File content too small, Tika heuristic cannot determine 'text/csv' with the necessary certainty.");
                return "file-content-too-small";
            }
        } else {
            log.debug("Fallback tika detect");
            return tika.detect(stream);
        }
    }
}
