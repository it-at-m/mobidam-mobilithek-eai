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
import java.io.InputStream;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.CharSequenceReader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MaliciousCSVCodeDetector implements MaliciousCodeDetector {

    private static final Pattern EXCEL_FORMULA_PATTERN = Pattern.compile("^[=+-@]^\\w*");

    public boolean isValidData(final InputStream stream, InterfaceDTO interfaceDto) throws Exception {

        CSVParser records = CSVFormat.newFormat(interfaceDto.getExpectedCsvDelimiter()).parse(new CharSequenceReader(new String(stream.readAllBytes())));
        CSVRecord record = null;
        int rowCount = 0;
        var rows = records.iterator();
        while (rows.hasNext()) {
            rowCount++;
            record = records.iterator().next();
            var values = record.stream().toList();
            if (values.size() != interfaceDto.getExpectedCsvColumnCount()) {
                log.warn("Invalid column count expected/found {}/{} with delimiter '{}' : {}", interfaceDto.getExpectedCsvColumnCount(), record.values().length,
                        interfaceDto.getExpectedCsvDelimiter(), record);
                return false;
            }
            var columns = values.iterator();
            while (columns.hasNext()) {
                var item = columns.next();
                var valid = EXCEL_FORMULA_PATTERN.matcher(item.trim()).matches();
                if (valid) {
                    log.warn("MaliciousCSVCode - Excel Formula : {}", item);
                    return false;
                }
            }
        }
        log.info("File row size/read size : {}/{}", records.getRecordNumber(), rowCount);
        return true;
    }

}
