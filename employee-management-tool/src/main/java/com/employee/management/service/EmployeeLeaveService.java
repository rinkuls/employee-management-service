package com.employee.management.service;

import com.employee.management.model.EmployeeLeave;
import java.util.List;
import java.util.Optional;

public interface EmployeeLeaveService {

  /**
   * @param name
   * @return
   */


  Optional<List<EmployeeLeave>> findLeaveDetailsByName(String name);

  Optional<List<EmployeeLeave>> findLeaveDetailsByEmpID(Long empId);

  Optional<EmployeeLeave> findLeaveDetailsByEmpIDAndFinancialYear(Long empId,
      Integer financialYear);

  EmployeeLeave addOrUpdateEmployeeLeavesForFinancialYear(EmployeeLeave employeeLeave);
}
