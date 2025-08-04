package com.example.demo.global.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class customJwtFilter extends GenericFilterBean {
	private final Logger logger = LoggerFactory.getLogger(customJwtFilter.class);
	private final String AUTHORIZATION_HEADER = "Authorization";
	private AccessTokenProvider accessTokenProvider;
	public customJwtFilter(AccessTokenProvider accessTokenProvider) {
		this.accessTokenProvider = accessTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
		String jwt = resolveToken(httpServletRequest);
		String requestURI = httpServletRequest.getRequestURI();

		if (requestURI.startsWith("/swagger") ||
			requestURI.startsWith("/v3/api-docs") ||
			requestURI.startsWith("/swagger-ui") ||
			requestURI.startsWith("/webjars") ||
			requestURI.startsWith("/configuration")) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		if (StringUtils.hasText(jwt) && accessTokenProvider.validateToken(jwt)) {
			Authentication authentication = accessTokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);

			if ("/api/payment/ready".equals(requestURI)) {
				System.out.println("인증된 사용자 ID: " + authentication.getName());
			}
		} else {
			logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

			return null;
	}
}
