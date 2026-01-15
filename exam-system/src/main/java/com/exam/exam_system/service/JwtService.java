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
import com.exam.exam_system.Entities.Token;
import com.exam.exam_system.Entities.User;
import com.exam.exam_system.repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class JwtService {

	private final TokenRepository repository;
	
	@Value("${secret}")
	private String secret;
	
	@Value("${expiration}")
	private String expiration;
	
	@Value("${refreshExpiration}")
	private String refreshExpiration;
	
	private Key getSignKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername(), expiration);
		
	}
	
	public String generateRefreshToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername(), refreshExpiration);
	}
	
	private String createToken(Map<String, Object> claims, String subject, String exp) {

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(exp)))
				.signWith(getSignKey(), SignatureAlgorithm.HS256)
				.compact();
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
	        return Jwts
	                .parserBuilder()
	                .setSigningKey(getSignKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    } catch (Exception e) {
	        throw new InvalidTokenException(Messages.TOKEN_NOT_FOUND);
	    }
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims :: getExpiration);

	}
	
	public void saveUserToken(User user, String jwtToken) {
		Token token = Token.builder()
				.user(user)
				.token(jwtToken)
				.expired(false)
				.revoked(false)
				.build();
		repository.save(token);
	}
	
	@Transactional
	public void revokeAllUserTokens(User user) {
		List<Token> validUserTokens = repository.findAllValidTokenByUser(user.getUserId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(t -> {
			t.setExpired(true);
			t.setRevoked(true);
		});
		repository.saveAll(validUserTokens);
	}
	
	
}
