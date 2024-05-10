package com.nri.ems.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.nri.ems.Config.JWT.JWTAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfig {
        @Autowired
        private UserDetailsService userDetailsService;
        @Autowired
        private JWTAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                return http.csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(
                                                req -> req.requestMatchers("/api/**",
                                                                "/v2/api-docs",
                                                                "/v3/api-docs",
                                                                "/v3/api-docs/**",
                                                                "/swagger-resources",
                                                                "/swagger-resources/**",
                                                                "/configuration/ui",
                                                                "/configuration/security",
                                                                "/swagger-ui/**",
                                                                "/webjars/**",
                                                                "/swagger-ui.html"

                                                )
                                                                .permitAll()
                                                                .requestMatchers("/hr/**").hasRole("HR")
                                                                .requestMatchers("/project/**").hasRole("HR")
                                                                .requestMatchers("/paygrade/**").hasRole("HR")
                                                                .requestMatchers("/dept/**").hasRole("HR")
                                                                .requestMatchers("/emp/**").hasAnyRole("EMPLOYEE", "HR")
                                                                .anyRequest()
                                                                .authenticated())
                                .userDetailsService(userDetailsService)
                                
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                
                                .build();
        }

}
