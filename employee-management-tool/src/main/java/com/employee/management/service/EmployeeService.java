package com.employee.management.service;

import com.employee.management.dto.UserDetails;
import com.employee.management.model.Employee;
import java.util.Optional;

public interface EmployeeService {

  /**
   * @param employee
   * @return
   */
  Employee saveOrUpdateEmployee(Employee employee);

  /**
   * @param name
   * @return
   */
  Optional<Employee> findByName(String name);

  UserDetails FetchDetailsOfUser(String token);

  void saveUserDetails(UserDetails userDetails);
}
