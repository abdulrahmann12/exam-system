package com.exam.exam_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.exam.exam_system.repository.UserRepository;
import com.exam.exam_system.service.JwtService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtService jwtService;
	private final UserRepository userRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(c -> c.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/").permitAll().requestMatchers("/images/**")
						.permitAll().requestMatchers("/actuator/**").permitAll().requestMatchers("/index.html")
						.permitAll().requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
						.permitAll().requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/api/auth/refresh-token").permitAll()
						.requestMatchers("/api/auth/change-password").permitAll().anyRequest().authenticated())
				.sessionManagement(Session -> Session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();

	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtService, userDetailsService());
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return input -> (UserDetails) userRepository.findByUsername(input).or(() -> userRepository.findByEmail(input))
				.orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
