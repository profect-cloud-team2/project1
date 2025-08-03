package com.example.demo.global.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.entity.UserEntity.UserRole;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

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

	// ✅ 액세스 토큰 생성
	public String createAccessToken(Authentication authentication){
		String authorities = authentication.getAuthorities().stream()
			.map(auth -> auth.getAuthority().replace("ROLE_",""))
			.findFirst()
			.get();

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.accessTokenExpiration);

		return Jwts.builder()
			.subject(authentication.getName()) // userId
			.claim("role", authorities)
			.signWith(key)
			.expiration(validity)
			.compact();
	}

	// ✅ Authentication 반환 시 UserEntity 포함하도록 수정
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts
			.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();

		String userId = claims.getSubject(); // UUID
		String role = claims.get("role", String.class); // ex. "ADMIN", "CUSTOMER"

		UserEntity user = UserEntity.builder()
			.userId(java.util.UUID.fromString(userId))
			.role(UserRole.valueOf(role))
			.build();

		Collection<? extends GrantedAuthority> authorities =
			List.of(new SimpleGrantedAuthority("ROLE_" + role));

		return new UsernamePasswordAuthenticationToken(user, token, authorities);
	}

	// ✅ 토큰 유효성 검사
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
