package de.muenchen.mobidam.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@ConfigurationProperties(prefix = "mobidam.metrics")
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

}
