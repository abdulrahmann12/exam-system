package com.exam.exam_system.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.exception.InvalidTokenException;
import com.exam.exam_system.Entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtService {

	@Value("${secret}")
	private String secret;

	@Value("${expiration}")
	private String expiration;

	private Key getSignKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String generateToken(User user) {

		Map<String, Object> claims = new HashMap<>();

		claims.put("permissions", user.getRole().getPermissions().stream()
				.filter(p -> Boolean.TRUE.equals(p.getActive())).map(p -> p.getCode()).toList());

		return createToken(claims, user.getUsername(), expiration);
	}

	private String createToken(Map<String, Object> claims, String subject, String exp) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(exp)))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			throw new InvalidTokenException(Messages.TOKEN_NOT_FOUND);
		}
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);

	}

	@SuppressWarnings("unchecked")
	public List<String> extractPermissions(String token) {
		return extractAllClaims(token).get("permissions", List.class);
	}

}
