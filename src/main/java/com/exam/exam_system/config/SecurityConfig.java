package com.exam.exam_system.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.exam.exam_system.service.JwtService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtService jwtService;
	private final UserDetailsService cachedUserDetailsService;

	@Value("${cors.allowed-origins:http://localhost:3000}")
	private String allowedOrigins;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(c -> c.disable())
				.headers(headers -> headers
						.contentTypeOptions(opts -> {})
						.frameOptions(frame -> frame.deny())
						.xssProtection(xss -> {})
				)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/").permitAll()
						.requestMatchers("/images/**").permitAll()
						.requestMatchers("/actuator/health", "/actuator/info").permitAll()
						.requestMatchers("/index.html").permitAll()
						.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
						.requestMatchers("/api/auth/login", "/api/auth/forget-password",
								"/api/auth/reset-password", "/api/auth/refresh-token",
								"/api/auth/logout").permitAll()
						.requestMatchers("/api/students/register").permitAll()
						.requestMatchers("/api/sessions/enter").permitAll()
						.requestMatchers("/api/sessions/access").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(Session -> Session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();

	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtService, cachedUserDetailsService);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

}