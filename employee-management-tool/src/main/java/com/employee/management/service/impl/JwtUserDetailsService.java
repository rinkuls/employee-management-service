package com.employee.management.service.impl;

import com.employee.management.repo.UsersRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {


  private final UsersRepo usersRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    var user = usersRepo.findByUserName(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    return new org.springframework.security.core.userdetails.User(
        user.get().getUserName(),
        " ",
        List.of(new SimpleGrantedAuthority(user.get().getRole())) // Role(s)
    );
  }

}
