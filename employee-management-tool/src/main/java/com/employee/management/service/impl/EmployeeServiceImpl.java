package com.employee.management.service.impl;

import com.employee.management.dto.UserDetails;
import com.employee.management.exception.SecretKeyException;
import com.employee.management.model.Employee;
import com.employee.management.repo.EmployeeRepo;
import com.employee.management.service.EmployeeService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private EmployeeRepo employeeRepo;
  private final String secretKey;
  @Value("${jwt.secret.url}")
  private final String secretUrl;
  private static final String ENV_FILE = "gitHubToken.env";
  private static final String ENV_DIRECTORY = System.getProperty("user.dir");
  private static final String GITHUB_TOKEN_KEY = "GITHUB_TOKEN";
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String ERROR_FETCHING_SECRET = "Error fetching secret key from GitHub";
  private static final String GITHUB_TOKEN_NOT_FOUND = "GitHub token not found in .env file";
  private static final String SECRET_KEY_NOT_FETCHED = "Secret key could not be fetched from GitHub";



  public EmployeeServiceImpl(
      EmployeeRepo employeeRepo,
      @Value("${jwt.secret.url}") String secretUrl) {
    this.employeeRepo = employeeRepo;
    this.secretUrl = secretUrl;
    this.secretKey = "just for push";
    //this.secretKey = fetchSecretKeyFromGitHub()
      //  .orElseThrow(() -> new SecretKeyException(SECRET_KEY_NOT_FETCHED));
  }
  @Override
  public Employee saveOrUpdateEmployee(Employee employee) {
    return employeeRepo.findByEmpId(employee.getEmpId())
        .map(existingEmployee -> {
          copyEmployeeDetails(existingEmployee, employee);
          return employeeRepo.save(existingEmployee);
        })
        .orElseGet(() -> {
          copyEmployeeDetails(employee, employee);
          return employeeRepo.save(employee);
        });
  }

  private void copyEmployeeDetails(Employee target, Employee source) {
    // Copy simple fields
    target.setName(source.getName());
    target.setExtraMartialAffair(source.isExtraMartialAffair());
    target.setEmail(source.getEmail());
    target.setPhoneNumber(source.getPhoneNumber());
    target.setAddress(source.getAddress());
    target.setMarried(source.isMarried());
    target.setDreamWish(source.getDreamWish());
    target.setNatureBehavior(source.getNatureBehavior());

    // Copy complex associations
    Optional.ofNullable(source.getProfessionalDetails())
        .ifPresent(professionalDetails -> {
          professionalDetails.setEmployee(target);
          target.setProfessionalDetails(professionalDetails);
        });

    Optional.ofNullable(source.getPastEmployments())
        .ifPresent(pastEmployments -> {
          pastEmployments.forEach(pastEmployment -> pastEmployment.setEmployee(target));
          target.setPastEmployments(pastEmployments);
        });

    Optional.ofNullable(source.getKids())
        .ifPresent(kids -> {
          kids.forEach(kid -> kid.setEmployee(target));
          target.setKids(kids);
        });

    Optional.ofNullable(source.getSpouse())
        .ifPresent(spouse -> {
          spouse.setEmployee(target);
          target.setSpouse(spouse);
        });
  }


  @Override
  public Optional<Employee> findByName(String name) {
    return employeeRepo.findByName(name);
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



    return  new UserDetails(role, empId, name);
  }

  @Override
  public void saveUserDetails(UserDetails userDetails) {

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
