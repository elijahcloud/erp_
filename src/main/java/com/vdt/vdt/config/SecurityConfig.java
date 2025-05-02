package com.vdt.vdt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**","/**","/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**")// disable CSRF for H2 console
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()) // allow H2 to render in iframe
                )
            .cors(cors -> cors.configurationSource(corsConfigurationSource)) // ✅ CORRECT CORS INTEGRATION
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**","/actuator/health","/h","/swagger-ui.html","/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**").permitAll() // allow login/register
                .anyRequest().authenticated()
            ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
                );
        return http.build();
    }
}