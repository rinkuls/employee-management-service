package com.employee.management.service.impl;

import com.employee.management.dto.UserDetails;
import com.employee.management.model.Users;
import com.employee.management.repo.UsersRepo;
import com.employee.management.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UsersRepo usersRepo;


  @Override
  public Optional<Users> saveUserDetails(UserDetails userDetails) {
    var userDetail= Users.builder().userName(userDetails.getName()).empId(
        Long.valueOf(userDetails.getEmpId())).role(userDetails.getRole()).build();

    return Optional.of(usersRepo.save(userDetail));
  }


}
