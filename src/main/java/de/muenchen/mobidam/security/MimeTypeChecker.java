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

import de.muenchen.mobidam.mobilithek.InterfaceDTO;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.CharSequenceReader;
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

    public boolean check(final InputStream stream, final List<String> allowedMimeTypes, final String contentType, final InterfaceDTO interfaceDto)
            throws IOException {

        String mimetype = getMimeType(stream, contentType, interfaceDto);
        log.debug("File is of mime type: {}", mimetype);
        return allowedMimeTypes.contains(mimetype);
    }

    private String getMimeType(final InputStream stream, String contentType, InterfaceDTO interfaceDto) throws IOException {

        if (contentType != null && contentType.equals("text/csv")) {

            ContentHandler handler = new BodyContentHandler(-1);
            TextAndCSVParser parser = new TextAndCSVParser();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            try {
                parser.parse(stream, handler, metadata, context);
            } catch (Exception e) {
                throw new IOException(e);
            }
            log.debug("Tika file metadata {}", metadata.toString());
            var tikaContentType = metadata.get(Metadata.CONTENT_TYPE);
            if (tikaContentType.toLowerCase().contains(TEXT_CSV_TYPE.toString()))
                return TEXT_CSV_TYPE.toString();
            else if (tikaContentType.toLowerCase().contains(org.springframework.http.MediaType.TEXT_PLAIN_VALUE)) {
                log.warn("File content too small, Tika heuristic cannot determine 'text/csv' with the necessary certainty.");
                stream.reset();
                Iterable<CSVRecord> records = CSVFormat.newFormat(interfaceDto.getExpectedCsvDelimiter())
                        .parse(new CharSequenceReader(new String(stream.readAllBytes())));
                var iterator = records.iterator();
                if (iterator.hasNext()) {
                    while (iterator.hasNext()) {
                        var record = iterator.next();
                        if (record.values().length != interfaceDto.getExpectedCsvColumnCount()) {
                            log.warn("Invalid column count expected/found {}/{} with delimiter '{}' : {}", interfaceDto.getExpectedCsvColumnCount(),
                                    record.values().length, interfaceDto.getExpectedCsvDelimiter(), record);
                            return org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
                        }
                    }
                    return TEXT_CSV_TYPE.toString();
                } else
                    return tikaContentType;
            } else
                return tikaContentType;

        } else
            return tika.detect(stream);
    }
}
