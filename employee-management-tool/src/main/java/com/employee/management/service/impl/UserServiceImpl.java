package com.employee.management.service.impl;

import com.employee.management.dto.UserDetails;
import com.employee.management.exception.SecretKeyException;
import com.employee.management.model.Users;
import com.employee.management.repo.UsersRepo;
import com.employee.management.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor  need to check on this
public class UserServiceImpl implements UserService {

  private static final String ENV_FILE = "gitHubToken.env";
  private static final String ENV_DIRECTORY = System.getProperty("user.dir");
  private static final String GITHUB_TOKEN_KEY = "GITHUB_TOKEN";
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String ERROR_FETCHING_SECRET = "Error fetching secret key from GitHub";
  private static final String GITHUB_TOKEN_NOT_FOUND = "GitHub token not found in .env file";
  private static final String SECRET_KEY_NOT_FETCHED = "Secret key could not be fetched from GitHub";
  private final UsersRepo usersRepo;
  private final String secretKey;
  @Value("${jwt.secret.url}")
  private final String secretUrl;

  public UserServiceImpl(
      UsersRepo usersRepo,
      @Value("${jwt.secret.url}") String secretUrl) {
    this.usersRepo = usersRepo;
    this.secretUrl = secretUrl;

    this.secretKey = fetchSecretKeyFromGitHub()
        .orElseThrow(() -> new SecretKeyException(SECRET_KEY_NOT_FETCHED));
  }

  @Override
  public UserDetails FetchDetailsOfUser(String token) {
    // Validate and parse the token
    var signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

    var jwsClaims = Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(token);

    var claims = jwsClaims.getBody();

    // Extract details
    String role = claims.get("role", String.class); // Assuming "role" is stored in the token
    Integer empId = claims.get("empId", Integer.class); // Assuming "empId" is stored in the token
    String name = claims.getSubject(); // Assuming username is stored as the subject

    return new UserDetails(role, empId, name);
  }

  @Override
  public Optional<Users> saveUserDetails(UserDetails userDetails) {
    var userDetail = Users.builder().userName(userDetails.getName()).empId(
        Long.valueOf(userDetails.getEmpId())).role(userDetails.getRole()).build();

    return Optional.of(usersRepo.save(userDetail));
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
