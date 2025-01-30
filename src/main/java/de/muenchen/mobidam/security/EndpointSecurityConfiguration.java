package de.muenchen.mobidam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class EndpointSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/**",
                        // allow access to /actuator/info
                        "/actuator/info",
                        // allow access to /actuator/health for OpenShift Health Check
                        "/actuator/health",
                        // allow access to /actuator/health/liveness for OpenShift Liveness Check
                        "/actuator/health/liveness",
                        // allow access to /actuator/health/readiness for OpenShift Readiness Check
                        "/actuator/health/readiness",
                        // allow access to /actuator/metrics for Prometheus monitoring in OpenShift
                        "/actuator/metrics",
                        "/actuator/prometheus")
                        .permitAll().anyRequest().authenticated());

        return http.build();
    }
}
