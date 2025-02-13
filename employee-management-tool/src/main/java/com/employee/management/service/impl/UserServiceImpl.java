package com.employee.management.service.impl;

import com.employee.management.dto.UserDetails;
import com.employee.management.model.Users;
import com.employee.management.repo.UsersRepo;
import com.employee.management.service.UserService;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  public static final String REALM_ACCESS = "realm_access";
  public static final String ROLES = "roles";
  public static final String ADMIN_ROLE = "ADMIN";
  public static final String USER_ROLE = "USER";
  public static final String PREFERRED_USERNAME = "preferred_username";
  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
  private static final String DEFAULT_PASSWORD = "password";
  private final UsersRepo usersRepo;
  private final JwtDecoder jwtDecoder;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails FetchDetailsOfUser(String token) {
    logger.info("Extracting role and username from token");

    try {
      var jwt = jwtDecoder.decode(token);

      // Extract role from realm_access
      return Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS))
          .map(realmAccess -> realmAccess.get(ROLES))
          .filter(Objects::nonNull)
          .map(roles -> (Collection<String>) roles) // Explicit cast to Collection<String>
          .flatMap(roles -> roles.stream()
              .filter(role -> ADMIN_ROLE.equals(role) || USER_ROLE.equals(role))
              .findFirst())
          .map(role -> new UserDetails(role, jwt.getClaimAsString(PREFERRED_USERNAME)))
          .orElseThrow(() -> new RuntimeException("No valid role found in token"));

    } catch (Exception e) {
      logger.error("Error parsing token: {}", e.getMessage(), e);
      throw new RuntimeException("Invalid token", e);
    }
  }


  @Override
  public Optional<Users> saveUserDetails(UserDetails userDetails) {
    logger.info("Saving user details to the database: {}", userDetails);

    try {

      var newUser = Users.builder().role(userDetails.getRole()).username(userDetails.getUsername())
          .empId(RandomUtils.nextLong())
          .password(passwordEncoder.encode(DEFAULT_PASSWORD)).build();

      newUser.setDefaultPasswordChanged(false);
      Users savedUser = usersRepo.save(newUser);
      logger.info("User details saved successfully. ID: {}", savedUser.getId());
      return Optional.of(savedUser);
    } catch (Exception e) {
      logger.error("Error saving user details: {}", e.getMessage(), e);
      throw e;
    }
  }


}
