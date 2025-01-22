package com.employee.management.repo;

import com.employee.management.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

  /**
   * @param userName
   * @return
   */
  Optional<Users> findByUserName(String userName);

  /**
   * @param empId
   * @return
   */
  Optional<Users> findByEmpId(Long empId);


  Optional<Users> findByRole(String role);




}
