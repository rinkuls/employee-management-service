package com.employee.management.service.impl;

import com.employee.management.dto.UserDetails;
import com.employee.management.model.Users;
import com.employee.management.repo.UsersRepo;
import com.employee.management.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
  private static final String DEFAULT_PASSWORD = "password";
  private final UsersRepo usersRepo;
  private final JwtDecoder jwtDecoder;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails FetchDetailsOfUser(String token) {
    logger.info("Validating and parsing token: {}", token);

    try {

      var empId = jwtDecoder.decode(token).getClaimAsString("empId");

      var user = usersRepo.findByEmpId(Long.valueOf(empId));

      return new UserDetails(user.get().getRole(),
          Math.toIntExact(user.get().getEmpId()), user.get().getUsername());

    } catch (Exception e) {
      logger.error("Error validating token: {}", e.getMessage(), e);
      throw new RuntimeException("Invalid token");
    }

  }

  @Override
  public Optional<Users> saveUserDetails(UserDetails userDetails) {
    logger.info("Saving user details to the database: {}", userDetails);

    try {

      var newUser = Users.builder().role(userDetails.getRole()).username(userDetails.getUsername())
          .empId(Long.valueOf(userDetails.getEmpId()))
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
