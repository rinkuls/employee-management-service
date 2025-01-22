package com.employee.management.security;

import com.employee.management.dto.JwtValidationResult;
import com.employee.management.exception.SecretKeyException;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {


  private static final String ENV_FILE =
      System.getProperty("spring.profiles.active", "dev").equals("docker")
          ? "/app/gitHubToken.env"
          : "gitHubToken.env";
  private static final String ENV_DIRECTORY =
      System.getProperty("spring.profiles.active", "dev").equals("docker")
          ? "/"
          : ".";
  private static final String GITHUB_TOKEN_KEY = "GITHUB_TOKEN";
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String ERROR_FETCHING_SECRET = "Error fetching secret key from GitHub";
  private static final String GITHUB_TOKEN_NOT_FOUND = "GitHub token not found in .env file";
  private static final String SECRET_KEY_NOT_FETCHED = "Secret key could not be fetched from GitHub";
  private final String secretKey;
  @Value("${jwt.secret.url}")
  private final String secretUrl;
  @Value("${jwt.expiration}")
  private final long jwtExpirationInMs;
  @Value("${jwt.expected-issuer}")
  private String expectedIssuer;
  @Value("${jwt.expected-audience}")
  private String expectedAudience;

  public JwtValidator(
      @Value("${jwt.secret.url}") String secretUrl,
      @Value("${jwt.expiration}") long jwtExpirationInMs
  ) {
    this.secretUrl = secretUrl;
    this.jwtExpirationInMs = jwtExpirationInMs;
    this.secretKey = "test";
   // this.secretKey = fetchSecretKeyFromGitHub()
     //   .orElseThrow(() -> new SecretKeyException(SECRET_KEY_NOT_FETCHED));
  }

  public JwtValidationResult getJwtValidationResult(String token) {
    try {

      // Create the signing key
      var signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

      var jwsClaims = Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(token);

      var claims = jwsClaims.getBody();

      // Validate issuer
      if (!Objects.equals(claims.get("issuer"), expectedIssuer)) {
        return JwtValidationResult.failure("Invalid issuer");
      }

      // Validate audience
      if (!Objects.equals(claims.get("audience"), expectedAudience)) {
        return JwtValidationResult.failure("Invalid audience");
      }

      // Validate expiration
      var expiration = claims.getExpiration();
      if (expiration.before(new Date())) {
        return JwtValidationResult.failure("Token has expired");
      }

      // Validate critical claims
      var username = claims.get("username", String.class);
      var role = claims.get("role", String.class);
      var empId = claims.get("empId", Long.class);

      if (username == null || role == null || empId == null) {
        return JwtValidationResult.failure("Missing critical claims");
      }

      // Additional custom claim validations (e.g., check role hierarchy)
      if (!isValidRole(role)) {
        return JwtValidationResult.failure("Invalid role in token");
      }

      return JwtValidationResult.success(claims);

    } catch (JwtException e) {
      return JwtValidationResult.failure("Invalid token: " + e.getMessage());
    }
  }

  private boolean isValidRole(String role) {
    // Example of a role hierarchy check
    List<String> validRoles = List.of("ADMIN", "USER", "MANAGER");
    return validRoles.contains(role);
  }

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  //check if the token has expired
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public Boolean validateTokenByUserDetails(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private Optional<String> fetchSecretKeyFromGitHub() {
    try {
      Dotenv dotenv = Dotenv.configure()
          .directory(ENV_DIRECTORY)
          .filename(ENV_FILE)
          .load();

      String githubToken = dotenv.get(GITHUB_TOKEN_KEY);

      if (githubToken == null || githubToken.isBlank()) {
        throw new SecretKeyException(GITHUB_TOKEN_NOT_FOUND);
      }

      URL url = new URL(secretUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty(AUTHORIZATION_HEADER, BEARER_PREFIX + githubToken);

      if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        try (var reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
          return Optional.ofNullable(reader.readLine());
        }
      } else {
        throw new SecretKeyException(ERROR_FETCHING_SECRET + ": HTTP " + conn.getResponseCode());
      }
    } catch (Exception e) {
      throw new SecretKeyException(ERROR_FETCHING_SECRET, e);
    }
  }

}
