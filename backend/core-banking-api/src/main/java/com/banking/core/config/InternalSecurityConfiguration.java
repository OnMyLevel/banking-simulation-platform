package com.banking.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("!jwt")
public class InternalSecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/internal/**").hasAnyRole(SecurityRoles.ROLE_OPS, SecurityRoles.ROLE_ADMIN)
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .build();
    }

    @Bean
    UserDetailsService internalUsers() {
        UserDetails ops = User.withUsername("ops")
            .password("{noop}ops")
            .roles(SecurityRoles.ROLE_OPS)
            .build();
        UserDetails admin = User.withUsername("admin")
            .password("{noop}admin")
            .roles(SecurityRoles.ROLE_ADMIN)
            .build();
        return new InMemoryUserDetailsManager(ops, admin);
    }
}
