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

import de.muenchen.mobidam.config.MaliciousDataRegex;
import de.muenchen.mobidam.sstmanagment.DurationLog;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.tika.parser.csv.TextAndCSVConfig;
import org.apache.tika.parser.csv.TextAndCSVParser;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MaliciousCSVCodeDetector implements MaliciousCodeDetector {

    private final MaliciousDataRegex maliciousPatterns;
    private final TextAndCSVConfig textAndCSVConfig = new TextAndCSVConfig();
    private final DurationLog durationCSVParser = new DurationLog("CSV-Parser");
    private final DurationLog durationMaliciousCodeDetection = new DurationLog("CSV-Malicious-Code-Detection");

    public MaliciousCSVCodeDetector(MaliciousDataRegex maliciousPatterns) {
        this.maliciousPatterns = maliciousPatterns;
    }

    public boolean isValidData(final InputStream stream, Exchange exchange) throws Exception {
        durationCSVParser.startDebug();

        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                CSVParser records = CSVFormat.newFormat(selectTikaCsvDelimiter(exchange)).parse(reader)) {

            durationCSVParser.endDebug();
            durationMaliciousCodeDetection.startDebug();

            int rowCount = 0;
            for (CSVRecord record : records) {
                rowCount++;

                for (String column : record) {
                    String cell = column.trim();
                    if (!cell.isEmpty()) {
                        for (Map.Entry<String, Pattern> entry : maliciousPatterns.getMaliciousDataPatterns().entrySet()) {
                            if (entry.getValue().matcher(cell).matches()) {
                                log.warn("MaliciousCSVCode - {} ({}) : {}",
                                        entry.getKey(),
                                        maliciousPatterns.getMaliciousDataRegex().get(entry.getKey()),
                                        cell);
                                return false;
                            }
                        }
                    }
                }
            }

            durationMaliciousCodeDetection.endDebug();
            log.debug("File row size/read size : {}/{}", records.getRecordNumber(), rowCount);
            return true;
        }
    }

    private char selectTikaCsvDelimiter(Exchange exchange) {
        String delimiterKey = exchange.getIn().getHeader(TextAndCSVParser.DELIMITER_PROPERTY.getName(), String.class);

        if (delimiterKey == null) {
            log.warn("Delimiter header not found, using default semicolon");
            return ';';
        }

        Character delimiter = textAndCSVConfig.getNameToDelimiterMap().get(delimiterKey);

        if (delimiter == null) {
            log.warn("Delimiter not found for key: '{}', using default semicolon", delimiterKey);
            return ';';
        }

        return delimiter;
    }

}
