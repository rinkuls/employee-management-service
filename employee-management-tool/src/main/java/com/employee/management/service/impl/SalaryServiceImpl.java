package com.employee.management.service.impl;

import com.employee.management.dto.SalaryStructureRequest;
import com.employee.management.model.Employee;
import com.employee.management.model.ReleaseStatus;
import com.employee.management.model.SalaryRelease;
import com.employee.management.model.SalaryStructure;
import com.employee.management.repo.EmployeeRepo;
import com.employee.management.repo.SalaryReleaseRepository;
import com.employee.management.repo.SalaryStructureRepository;
import com.employee.management.service.SalaryService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {

  private final EmployeeRepo employeeRepository;
  private final SalaryStructureRepository salaryStructureRepository;
  private final SalaryReleaseRepository salaryReleaseRepository;


  @Override
  public SalaryStructure getSalaryStructureByEmployeeId(Long employeeId) {
    return salaryStructureRepository.findByEmployeeId(employeeId)
        .orElseThrow(() -> new EntityNotFoundException(
            "Salary structure not found for employee ID: " + employeeId));
  }

  @Override
  public List<SalaryRelease> getSalaryReleasesByEmployeeId(Long employeeId) {
    return salaryReleaseRepository.findByEmployeeId(employeeId);
  }

  @Override
  public SalaryRelease releaseSalary(Long employeeId, YearMonth month) {
    var salaryStructure = salaryStructureRepository.findByEmployeeId(employeeId)
        .orElseThrow(() -> new EntityNotFoundException(
            "Salary structure not found for employee ID: " + employeeId));

    var totalSalary = salaryStructure.getTotalSalary();

    var salaryRelease = SalaryRelease.builder()
        .employee(salaryStructure.getEmployee())
        .salaryMonth(String.valueOf(month))
        .amountReleased(totalSalary)
        .releaseStatus(ReleaseStatus.RELEASED)
        // Assuming the salary release is successful
        .build();

    return salaryReleaseRepository.save(salaryRelease);
  }

  @Override
  public SalaryStructure defineOrUpdateSalaryStructure(SalaryStructureRequest request) {
    Employee employee = employeeRepository.findById(request.getEmployeeId())
        .orElseThrow(() -> new EntityNotFoundException(
            "Employee not found with ID: " + request.getEmployeeId()));

    BigDecimal totalSalary = calculateTotalSalary(request);

    SalaryStructure salaryStructure = salaryStructureRepository.findByEmployeeId(
            request.getEmployeeId())
        .orElse(SalaryStructure.builder()
            .employee(employee)
            .build());

    salaryStructure.setBasicSalary(request.getBasicSalary());
    salaryStructure.setHra(request.getHra());
    salaryStructure.setAllowances(request.getAllowances());
    salaryStructure.setDeductions(request.getDeductions());
    salaryStructure.setTotalSalary(totalSalary);

    return salaryStructureRepository.save(salaryStructure);
  }

  @Override
  public void deleteSalaryStructureByEmployeeId(Long employeeId) {
    salaryStructureRepository.deleteByEmployeeId(employeeId);
  }

  private BigDecimal calculateTotalSalary(SalaryStructureRequest request) {
    return request.getBasicSalary()
        .add(request.getHra())
        .add(request.getAllowances())
        .subtract(request.getDeductions());
  }
}
