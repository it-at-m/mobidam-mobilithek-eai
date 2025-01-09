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

import de.muenchen.mobidam.config.MaliciousContentRegex;
import de.muenchen.mobidam.sstmanagment.DurationLog;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.CharSequenceReader;
import org.apache.tika.parser.csv.TextAndCSVConfig;
import org.apache.tika.parser.csv.TextAndCSVParser;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MaliciousCSVCodeDetector implements MaliciousCodeDetector {

    private final MaliciousContentRegex maliciousPatterns;
    private final TextAndCSVConfig textAndCSVConfig = new TextAndCSVConfig();
    private final DurationLog durationCSVParser = new DurationLog("CSV-Parser");
    private final DurationLog durationMaliciousCodeDetection = new DurationLog("CSV-Malicious-Code-Detection");

    public MaliciousCSVCodeDetector(MaliciousContentRegex maliciousPatterns) {
        this.maliciousPatterns = maliciousPatterns;
    }

    public boolean isValidData(final InputStream stream, Exchange exchange) throws Exception {

        durationCSVParser.startLog();
        CSVParser records = CSVFormat.newFormat(selectTikaCsvDelimiter(exchange)).parse(new CharSequenceReader(new String(stream.readAllBytes())));
        durationCSVParser.endLog();
        CSVRecord record = null;
        int rowCount = 0;
        var rows = records.iterator();
        durationMaliciousCodeDetection.startLog();
        while (rows.hasNext()) {
            rowCount++;
            record = records.iterator().next();
            var columns = record.stream().toList();
            for (var column : columns) {
                var cell = column.trim();
                if (!cell.isEmpty()) {
                    for (Map.Entry<String, Pattern> entry : maliciousPatterns.getMaliciousPatterns().entrySet()) {
                        var match = entry.getValue().matcher(cell).matches();
                        if (match) {
                            log.warn("MaliciousCSVCode - {} ({}) : {}", entry.getKey(), maliciousPatterns.getMaliciousContentRegex().get(entry.getKey()), cell);
                            return false;
                        }
                    }
                }
            }
        }
        durationMaliciousCodeDetection.endLog();
        log.info("File row size/read size : {}/{}", records.getRecordNumber(), rowCount);
        return true;
    }

    private char selectTikaCsvDelimiter(Exchange exchange) {
        return textAndCSVConfig.getNameToDelimiterMap().get(exchange.getIn().getHeader(TextAndCSVParser.DELIMITER_PROPERTY.getName(), String.class));
    }

}
