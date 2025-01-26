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
    try {
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        logger.info("got Token and now validating it");
        return ResponseEntity.ok(userService.FetchDetailsOfUser(token));
      }

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/addUserDetails")
  public ResponseEntity<String> addUserDetails(
      @Valid @RequestBody UserDetails userDetails) {

    Optional.ofNullable(userDetails.getName())
        .filter(StringUtils::hasText)
        .orElseThrow(() -> new InvalidEmployeeDataException("User name data is missing."));
    logger.info("got details and now saving it");
    userService.saveUserDetails(userDetails);

    return ResponseEntity.ok("User added successfully");


  }


}
