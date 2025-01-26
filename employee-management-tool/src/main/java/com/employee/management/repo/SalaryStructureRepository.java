package com.employee.management.repo;

import com.employee.management.model.SalaryStructure;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {

  Optional<SalaryStructure> findByEmployeeId(Long employeeId);

  // TODO need to write query
  void deleteByEmployeeId(Long employeeId);
}
