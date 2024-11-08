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
package de.muenchen.mobidam.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MetricsNameConfig {

    @Value("${mobidam.metrics.beginn-counter-metric}")
    private String beginnCounterMetric;
    @Value("${mobidam.metrics.ende-counter-metric}")
    private String endCounterMetric;
    @Value("${mobidam.metrics.erfolg-counter-metric}")
    private String erfolgCounterMetric;
    @Value("${mobidam.metrics.fehler-counter-metric}")
    private String fehlerCounterMetric;
    @Value("${mobidam.metrics.warnungen-counter-metric}")
    private String warnungenCounterMetric;
    @Value("${mobidam.metrics.processing-time-metric}")
    private String processingTimeMetric;
    @Value("${mobidam.metrics.inflight-exchanges-metric}")
    private String inflightExchangesMetric;
    @Value("${mobidam.metrics.max-file-size-metric}")
    private String maxFileSizeMetric;

}
