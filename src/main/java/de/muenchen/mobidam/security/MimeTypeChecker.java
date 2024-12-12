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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.converter.stream.FileInputStreamCache;
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
public class MimeTypeChecker {

    public static final MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");

    private final Tika tika = new Tika();

    public boolean check(final InputStream stream, final List<String> allowedMimeTypes, final String contentType) throws IOException {

        log.debug("SST supplied content-type: {}", contentType);
        String mimetype = contentType != null ? contentType : getMimeType(stream);
        log.debug("Detected file mime type: {}", mimetype);
        Optional<String> allowedMimeType = allowedMimeTypes.stream().filter(mimetype::contains).findAny();
        log.debug("Allowed meme types '{}' found in '{}' : {}", allowedMimeTypes.toString(), mimetype, allowedMimeType.isPresent());
        return allowedMimeType.isPresent();
    }

    private String getMimeType(final InputStream stream) throws IOException {
        if (stream instanceof FileInputStreamCache) {
            return csvTypeCheck(stream);
        } else {
            return tika.detect(stream);
        }
    }

    private String csvTypeCheck(InputStream stream) throws IOException {
        ContentHandler handler = new BodyContentHandler(-1);
        TextAndCSVParser parser = new TextAndCSVParser();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        try {
            parser.parse(stream, handler, metadata, context);
        } catch (Exception e) {
            throw new IOException(e);
        }
        log.debug("File metadata {}", metadata.toString());
        if (metadata.get("csv:num_rows") == null)
            log.warn("Mime type=text/csv check : File content analysis may be incorrect if file content is too small : {}",
                    metadata.get(Metadata.CONTENT_TYPE));

        return metadata.get(Metadata.CONTENT_TYPE).toLowerCase().contains(TEXT_CSV_TYPE.toString()) ? TEXT_CSV_TYPE.toString()
                : "invalid csv content type : " + metadata.get(Metadata.CONTENT_TYPE);
    }

}
