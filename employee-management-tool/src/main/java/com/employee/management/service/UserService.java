package com.employee.management.service;

import com.employee.management.dto.UserDetails;
import com.employee.management.model.Users;
import java.util.Optional;

public interface UserService {

  /**
   * @param token
   */


  UserDetails FetchDetailsOfUser(String token);

  Optional<Users> saveUserDetails(UserDetails userDetails);
}
