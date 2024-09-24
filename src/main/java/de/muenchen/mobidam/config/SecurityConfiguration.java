/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.mobidam.config;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * The central class for configuration of all security aspects.
 */
@Configuration
@Profile("!no-security")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests((requests) -> requests.requestMatchers(
                                // allow access to /actuator/info
                                AntPathRequestMatcher.antMatcher("/actuator/info"),
                                // allow access to /actuator/health for OpenShift Health Check
                                AntPathRequestMatcher.antMatcher("/actuator/health"),
                                // allow access to single metrics values
                                AntPathRequestMatcher.antMatcher("/actuator/metrics/*"),
                                // allow access to /actuator/metrics for Prometheus monitoring in OpenShift
                                AntPathRequestMatcher.antMatcher("/actuator/metrics"))
                        .permitAll())
                .build();
    }
}