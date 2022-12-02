package com.example.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ComponentScan
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, ApplicationContext context) throws Exception {
		http.jee().authenticatedUserDetailsService(token -> context.getBean(UserDetailsService.class).loadUserDetails(token)).mappableAuthorities("ROLE_ADMIN", "ROLE_USER").and() // lambda required for warm context refresh
				.httpBasic(Customizer.withDefaults())
				.authorizeHttpRequests((matchers) -> matchers
						.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // this doesn't work but antMatchers did
				);
		return http.build();
	}

}
