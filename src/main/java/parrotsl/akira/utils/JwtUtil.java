package parrotsl.akira.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import parrotsl.akira.entity.User;
import parrotsl.akira.repository.UserRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

  // Generate a secure key of at least 256 bits
  private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  private final UserRepository userRepository;

  @Autowired
  public JwtUtil(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // Extract username (subject) from JWT token
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Extract expiration date from JWT token
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  // Generic method to extract a claim from the token
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  // Method to extract all claims from the token
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY)  // Ensure the same key is used for parsing
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // Check if the token has expired
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  // Create a JWT token with claims and subject (username)
  private String createToken(Map<String, Object> claims, String subject) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + 1000 * 60 * 60 * 10)) // 10 hours validity
        .signWith(SECRET_KEY)  // Use HS256 for signing
        .compact();
  }

  // Generate the full JWT token for a specific user
  public String generateCompleteJwt(@NonNull String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new RuntimeException("User not found with username: " + username);
    }

    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getRole());
    claims.put("sub", username);
    claims.put("iat", new Date().getTime() / 1000);
    claims.put("exp", new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10).getTime() / 1000);

    return createToken(claims, username);
  }

  // Validate token: check if username matches and if the token is not expired
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
