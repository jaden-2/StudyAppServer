package com.JWT;

import java.nio.charset.StandardCharsets;
import java.util.Date;


import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.entities.CustomUserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JWTUtil {
	
	@Value("${security.jwt.secret}")
	private String SECRET;
	private SecretKey secretKey;
	
	private static final int jwtExpiration = 1000 * 60 * 60; // 60 minutes
	
	@PostConstruct
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateToken(CustomUserDetails userDetails) {
		return Jwts.builder()
				.subject(userDetails.getUsername())
				.claim("role", "USER")
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(secretKey)
				.compact();
	}
	
	public String extractUsername(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();

	}
	
	public String extractClaim(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("role", String.class);	
	}
	public boolean isTokenValid(String token, CustomUserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isValidAfterPassChange(token, userDetails);
	}

	private boolean isTokenExpired(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration()
				.before(new Date());
	}
	
	private boolean isValidAfterPassChange(String token, CustomUserDetails userDetails) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getIssuedAt()
				.after(userDetails.getPassChangeDate());
	}
	
}
