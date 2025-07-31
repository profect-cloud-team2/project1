package com.example.demo.global.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//전체 security 작동 시 security config에 설정할 jwt config
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	private final AccessTokenProvider accessTokenProvider;
	public JwtSecurityConfig(AccessTokenProvider accessTokenProvider) {
		this.accessTokenProvider = accessTokenProvider;
	}

	@Override
	public void configure(HttpSecurity http) {
		http.addFilterBefore(
			new customJwtFilter(accessTokenProvider),
			UsernamePasswordAuthenticationFilter.class
		);
	}
}