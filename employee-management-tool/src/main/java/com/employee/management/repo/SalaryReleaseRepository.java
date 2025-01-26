package com.employee.management.repo;

import com.employee.management.model.SalaryRelease;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryReleaseRepository extends JpaRepository<SalaryRelease, Long> {

  List<SalaryRelease> findByEmployeeId(Long employeeId);
}
