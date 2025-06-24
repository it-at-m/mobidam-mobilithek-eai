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

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodeDetectorFactory {

    private final MaliciousXmlCodeDetector maliciousXmlCodeDetector;
    private final DefaultMaliciousCodeDetector defaultMaliciousCodeDetector;
    private final MaliciousCSVCodeDetector maliciousCSVCodeDetector;

    private final Map<String, MaliciousCodeDetector> map = new HashMap<>();

    @PostConstruct
    public void init() {
        map.put(MediaType.APPLICATION_XML_VALUE, maliciousXmlCodeDetector);
        map.put(ResourceTypeChecker.BINARY_CSV_TYPE.toString(), maliciousCSVCodeDetector);
    }

    public MaliciousCodeDetector getCodeDetector(final String mimeType) {
        System.out.println("MIRNA: " + mimeType);
        return map.getOrDefault(mimeType, defaultMaliciousCodeDetector);
    }

}
