package com.employee.management.controller;

import com.employee.management.dto.UserDetails;
import com.employee.management.exception.InvalidEmployeeDataException;
import com.employee.management.service.UserService;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class SystemUserController {

  private static final Logger logger = LoggerFactory.getLogger(SystemUserController.class);
  private final UserService userService;

  @PostMapping("/fetchRole")
  public ResponseEntity<UserDetails> fetchUserRole(
      @RequestHeader("Authorization") String authHeader) {
    logger.info("Received request to fetch user role.");

    try {
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        logger.info("Token extracted successfully: {}", token);
        return ResponseEntity.ok(userService.FetchDetailsOfUser(token));
      }
      logger.warn("Authorization header is missing or invalid.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (Exception e) {
      logger.error("Error fetching user role: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/addUserDetails")
  public ResponseEntity<String> addUserDetails(
      @Valid @RequestBody UserDetails userDetails) {
    logger.info("Received request to add user details.");

    try {
      Optional.ofNullable(userDetails.getName())
          .filter(StringUtils::hasText)
          .orElseThrow(() -> {
            logger.error("Validation failed: User name is missing.");
            return new InvalidEmployeeDataException("User name data is missing.");
          });

      logger.info("User details validated successfully. Saving to database...");
      userService.saveUserDetails(userDetails);

      logger.info("User details saved successfully.");
      return ResponseEntity.ok("User added successfully");
    } catch (Exception e) {
      logger.error("Error adding user details: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add user");
    }
  }
}
