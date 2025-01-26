package com.employee.management.service;

import com.employee.management.dto.SalaryStructureRequest;
import com.employee.management.model.SalaryRelease;
import com.employee.management.model.SalaryStructure;
import java.time.YearMonth;
import java.util.List;

public interface SalaryService {

  SalaryStructure getSalaryStructureByEmployeeId(Long employeeId);

  List<SalaryRelease> getSalaryReleasesByEmployeeId(Long employeeId);

  SalaryRelease releaseSalary(Long employeeId, YearMonth month);

  SalaryStructure defineOrUpdateSalaryStructure(SalaryStructureRequest request);

  void deleteSalaryStructureByEmployeeId(Long employeeId);
}
