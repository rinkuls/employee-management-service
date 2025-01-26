package com.employee.management.repo;

import com.employee.management.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface UsersRepo extends JpaRepository<Users, Long> {

  /**
   * @param userName
   * @return
   */

  @RestResource(exported = false)
  Optional<Users> findByUserName(String userName);

  /**
   * @param empId
   * @return
   */
  @RestResource(exported = false)
  Optional<Users> findByEmpId(Long empId);


  @RestResource(exported = false)
  Optional<Users> findByRole(String role);

  @Query("SELECT COUNT(*) > 0 FROM Users u WHERE u.empId = :empId")
  @RestResource(exported = true)
  boolean existsByEmpId(@Param("empId") String empId);


}
