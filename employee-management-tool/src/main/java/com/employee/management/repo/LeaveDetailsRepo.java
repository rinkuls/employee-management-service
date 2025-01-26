package com.employee.management.repo;

import com.employee.management.model.EmployeeLeave;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveDetailsRepo extends JpaRepository<EmployeeLeave, Long> {

  /**
   * @param name
   * @return
   */

  @Query("SELECT el  FROM EmployeeLeave el  WHERE el.employee.id IN (select e.id from Employee e  where e.name LIKE '%:name%')")
  Optional<List<EmployeeLeave>> findLeaveDetailsByName(@Param("name") String name);

  /**
   * @param empId
   * @return
   */

  @Query("SELECT el FROM EmployeeLeave el WHERE el.employee.id = :empId ")
  Optional<List<EmployeeLeave>> findLeaveDetailsByEmpID(Long empId);

  /**
   * @param empId
   * @param financialYear
   * @return
   */

  @Query("SELECT el FROM EmployeeLeave el WHERE el.employee.id = :empId AND el.financialYear = :financialYear")
  Optional<EmployeeLeave> findLeaveDetailsByEmpIDAndFinancialYear(Long empId,
      Integer financialYear);

}
