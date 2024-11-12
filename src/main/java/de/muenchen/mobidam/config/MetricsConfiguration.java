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

import de.muenchen.mobidam.security.FileSizeProcessor;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.CamelContext;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MetricsConfiguration {

    private final MeterRegistry meterRegistry;
    private final MetricsNameConfig metricsNameConfig;
    private final CamelContext camelContext;
    private final FileSizeProcessor fileSizeProcessor;

    private final Counter beginnCounter;
    private final Counter endeCounter;
    private final Counter fehlerCounter;
    private final Counter erfolgCounter;
    private final Counter warnungenCounter;
    private final Gauge inflightExchanges;
    private final Gauge maxFileSize;
    private Timer processingTime;

    public MetricsConfiguration(final MeterRegistry meterRegistry, MetricsNameConfig metricsNameConfig, CamelContext camelContext,
            FileSizeProcessor fileSizeProcessor) {
        this.meterRegistry = meterRegistry;
        this.metricsNameConfig = metricsNameConfig;
        this.camelContext = camelContext;
        this.fileSizeProcessor = fileSizeProcessor;
        this.beginnCounter = Counter.builder(metricsNameConfig.getBeginnCounterMetric()).register(meterRegistry);
        this.endeCounter = Counter.builder(metricsNameConfig.getEndCounterMetric()).register(meterRegistry);
        this.fehlerCounter = Counter.builder(metricsNameConfig.getFehlerCounterMetric()).register(meterRegistry);
        this.erfolgCounter = Counter.builder(metricsNameConfig.getErfolgCounterMetric()).register(meterRegistry);
        this.warnungenCounter = Counter.builder(metricsNameConfig.getWarnungenCounterMetric()).register(meterRegistry);
        this.inflightExchanges = Gauge.builder(metricsNameConfig.getInflightExchangesMetric(), camelContext, context -> context.getInflightRepository().size())
                .register(meterRegistry);
        this.processingTime = Timer.builder(metricsNameConfig.getProcessingTimeMetric()).register(meterRegistry);
        this.maxFileSize = Gauge.builder(metricsNameConfig.getMaxFileSizeMetric(), fileSizeProcessor::getMaxStreamSize).register(meterRegistry);

    }

}
