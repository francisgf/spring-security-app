package com.app.Jwt;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.Entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";
	private static final long TOKEN_DURATION_IN_SECONDS = 1000 * 60 * 24; // 24 horas

	
	public String getToken(UserDetails user) {

		return getToken(new HashMap<>(), user);
	}

	private String getToken(Map<String, Object> extraClaims, UserDetails user) {

		return Jwts.builder().setClaims(extraClaims).setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_DURATION_IN_SECONDS))
				.signWith(getKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * key secret random secure
	 * 
	 * @return
	 */
	public String generateSecret() {

		SecureRandom random = new SecureRandom();

		byte[] keyBytes = new byte[32];
		random.nextBytes(keyBytes);
		String encodedKey = Base64.getEncoder().encodeToString(keyBytes);
		System.out.println("SECRET:" + encodedKey);

		return encodedKey;
	}

	/**
	 * get user from token
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {

		return getClaim(token, Claims::getSubject);
	}

	/**
	 * Validate token
	 * 
	 * @param token
	 * @param userDetails
	 * @return
	 */
	public boolean isValidToken(String token, UserDetails userDetails) {

		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private Claims getAllClaims(String token) {

		return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();

	}

	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Date getExpiration(String token) {
		return getClaim(token, Claims::getExpiration);
	}

	/**
	 * validate token expired
	 * 
	 * @param token
	 * @return
	 */
	private boolean isTokenExpired(String token) {
		return getExpiration(token).before(new Date());
	}

}
