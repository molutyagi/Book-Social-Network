package com.booknetwork.api.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
// @RequiredArgsConstructor
public class JwtService {

	@Value("${application.security.jwt.secretKey}")
	private String SECRET_KEY;

	@Value("${application.security.jwt.access-token-expiration}")
	private long JWT_TOKEN_VALIDITY;

	@Value("${application.security.jwt.refresh-token-expiration}")
	private long REFRESH_TOKEN_VALIDITY;

	// Generate Token Services
	public String generateAccessToken(UserDetails userDetails) {
		return generateAccessToken(new HashMap<>(), userDetails);
	}

	public String generateAccessToken(Map<String, Object> claims, UserDetails userDetails) {
		return buildToken(claims, userDetails, JWT_TOKEN_VALIDITY);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return generateRefreshToken(new HashMap<>(), userDetails);
	}

	public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
		return buildToken(claims, userDetails, REFRESH_TOKEN_VALIDITY);
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long jwtexpiration) {
		List<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtexpiration)).claim("authorities", authorities)
				.signWith(getSignInKey()).compact();
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// validate Token services
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpirationFromToken(token).before(new Date());
	}

	private Date extractExpirationFromToken(String token) {
		return extractClaims(token, Claims::getExpiration);
	}

	public String extractUserName(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
	}

}
