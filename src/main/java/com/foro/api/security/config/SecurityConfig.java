package com.foro.api.security.config;

import com.foro.api.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import static com.foro.api.model.enums.UserRole.ADMIN;
import static com.foro.api.model.enums.UserRole.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    private static final String[] AUTH_WHITELIST = {
            "/auth/**",
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
    };

    private static final String[] GET_WHITELIST ={
            "/api/users/**",
            "/api/topics/**",
            "/api/replies/**",
            "/api/courses/**",
    };

    private static final String[] DELETE_WHITELIST ={
            "/api/topics/**",
            "/api/replies/**",
    };

    private static final String[] ADMIN_LIST ={
            "/api/courses/**",
            "/api/courses",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.NOT_FOUND),
                        new AntPathRequestMatcher("/**")
                )
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .requestMatchers(HttpMethod.GET, GET_WHITELIST).permitAll()
                .requestMatchers(HttpMethod.DELETE, DELETE_WHITELIST).hasAnyRole(ADMIN.name(), USER.name())
                .requestMatchers(HttpMethod.POST, ADMIN_LIST).hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.PUT, ADMIN_LIST).hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, ADMIN_LIST).hasRole(ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                .and()
                .build();
    }
}
