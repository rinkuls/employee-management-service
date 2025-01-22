package com.employee.management.repo;

import com.employee.management.model.Employee;
import java.util.Optional;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {


  /**
   * @param name
   * @return
   */
  Optional<Employee> findByName(String name);

  /**
   * @param empId
   * @return
   */
  Optional<Employee> findByEmpId(Long empId);


}
