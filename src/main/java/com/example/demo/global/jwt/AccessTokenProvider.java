package com.example.demo.global.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey; // JWT 0.12.3: Key 대신 SecretKey 사용

@Component
public class AccessTokenProvider {
	private final Logger logger = LoggerFactory.getLogger(AccessTokenProvider.class);
	private static final String AUTHORITIES_KEY = "auth";

	private final SecretKey key;
	private final long accessTokenExpiration;

	public AccessTokenProvider(@Value("${jwt.secret}") String secret,
							   @Value("${jwt.access-token-expiration:3600000}") long accessTokenExpiration){
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpiration = accessTokenExpiration;
	}

	public String createAccessToken(Authentication authentication){
		String authorities = authentication.getAuthorities().stream()
			//.map(GrantedAuthority::getAuthority)
				.map(auth -> auth.getAuthority().replace("ROLE_",""))
			//.collect(Collectors.joining(","));
				.findFirst()
				.get();

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.accessTokenExpiration);

		return Jwts.builder()
			.subject(authentication.getName())
				.claim("role", authorities)
			//.claim(AUTHORITIES_KEY, authorities)
			.signWith(key)
			.expiration(validity)
			.compact();
	}
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts
			.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();

		String role = claims.get("role", String.class); // JWT의 role claim 추출

//		Collection<? extends GrantedAuthority> authorities =
//			Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//				.map(SimpleGrantedAuthority::new)
//				.collect(Collectors.toList());

		Collection<? extends GrantedAuthority> authorities =
				List.of(new SimpleGrantedAuthority("ROLE_" + role));

		return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			logger.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			logger.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			logger.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			logger.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}
}
