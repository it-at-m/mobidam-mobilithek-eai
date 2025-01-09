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

import de.muenchen.mobidam.config.ContentType;
import de.muenchen.mobidam.config.Types;
import de.muenchen.mobidam.exception.MobidamException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

public class MimeTypesSortedListTest {

    @Test
    @DirtiesContext
    public void testCsvSingle() throws MobidamException {

        Types types = new Types();
        types.setTypes(
                Map.of("xml", new ContentType(List.of("application/xml", "text/plain")),
                        "csv", new ContentType(List.of("text/csv")),
                        "plain", new ContentType(List.of("text/plain"))));
        Assertions.assertEquals(List.of("text/csv"), types.getContentMimeTypes(List.of("csv")));
    }

    @Test
    public void testSelectManyCsvFirstList() throws MobidamException {

        Types types = new Types();
        types.setTypes(
                Map.of("xml", new ContentType(List.of("application/xml", "text/plain")),
                        "csv", new ContentType(List.of("text/csv")),
                        "plain", new ContentType(List.of("text/plain"))));
        Assertions.assertEquals(List.of("text/csv", "application/xml", "text/plain"), types.getContentMimeTypes(List.of("csv", "xml")));
    }

    @Test
    public void testSelectManyXmlFirst() throws MobidamException {

        Types types = new Types();
        types.setTypes(
                Map.of("xml", new ContentType(List.of("application/xml", "text/plain")),
                        "csv", new ContentType(List.of("text/csv")),
                        "plain", new ContentType(List.of("text/plain"))));
        Assertions.assertEquals(List.of("application/xml", "text/plain", "text/csv"), types.getContentMimeTypes(List.of("xml", "csv")));
    }

    @Test
    public void testSelectManyPlainFirst() throws MobidamException {

        Types types = new Types();
        types.setTypes(
                Map.of("xml", new ContentType(List.of("application/xml", "text/plain")),
                        "csv", new ContentType(List.of("text/csv")),
                        "plain", new ContentType(List.of("text/plain"))));
        Assertions.assertEquals(List.of("text/plain", "text/csv"), types.getContentMimeTypes(List.of("plain", "csv")));
    }

    @Test
    public void testSelectManyPlainFirstRemoveDuplicates() throws MobidamException {

        Types types = new Types();
        types.setTypes(
                Map.of("xml", new ContentType(List.of("application/xml", "text/plain")),
                        "csv", new ContentType(List.of("text/csv")),
                        "plain", new ContentType(List.of("text/plain"))));
        Assertions.assertEquals(List.of("text/plain", "text/csv", "application/xml"), types.getContentMimeTypes(List.of("plain", "csv", "xml")));
    }

}
