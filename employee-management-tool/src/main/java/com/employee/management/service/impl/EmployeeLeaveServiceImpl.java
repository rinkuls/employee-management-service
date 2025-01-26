package com.employee.management.service.impl;

import com.employee.management.model.EmployeeLeave;
import com.employee.management.repo.LeaveDetailsRepo;
import com.employee.management.service.EmployeeLeaveService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmployeeLeaveServiceImpl implements EmployeeLeaveService {

  private final LeaveDetailsRepo leaveDetailsRepo;

  /**
   * @param name
   * @return
   */
  @Override
  public Optional<List<EmployeeLeave>> findLeaveDetailsByName(String name) {
    return leaveDetailsRepo.findLeaveDetailsByName(name);
  }

  /**
   * @param empId
   * @return
   */
  @Override
  public Optional<List<EmployeeLeave>> findLeaveDetailsByEmpID(Long empId) {
    return leaveDetailsRepo.findLeaveDetailsByEmpID(empId);
  }

  /**
   * @param empId
   * @param financialYear
   * @return
   */
  @Override
  public Optional<EmployeeLeave> findLeaveDetailsByEmpIDAndFinancialYear(Long empId,
      Integer financialYear) {
    return leaveDetailsRepo.findLeaveDetailsByEmpIDAndFinancialYear(empId, financialYear);
  }

  /**
   *
   */
  @Override
  public EmployeeLeave addOrUpdateEmployeeLeavesForFinancialYear(EmployeeLeave employeeLeave) {

    return leaveDetailsRepo.findLeaveDetailsByEmpIDAndFinancialYear(
        employeeLeave.getEmployee().getEmpId(),
        employeeLeave.getFinancialYear()).map(dbLeaveTarget -> {
      updateEmployeeLeave(employeeLeave, dbLeaveTarget);
      return leaveDetailsRepo.save(dbLeaveTarget);
    }).orElseGet(() -> {
      updateEmployeeLeave(employeeLeave, employeeLeave);
      return leaveDetailsRepo.save(employeeLeave);
    });


  }

  private void updateEmployeeLeave(EmployeeLeave leaveSource,
      EmployeeLeave dbLeaveTarget) {

    Map<Consumer<Integer>, Supplier<Integer>> fieldsUpdated = Map.of(
        dbLeaveTarget::setAllowedLeaveForForwarding, leaveSource::getAllowedLeaveForForwarding,
        dbLeaveTarget::setTotalLeaveGrantedForYear, leaveSource::getTotalLeaveGrantedForYear,
        dbLeaveTarget::setLeaveBalancedForYear, leaveSource::getLeaveBalancedForYear,
        dbLeaveTarget::setLeaveConsumedForYear, leaveSource::getLeaveConsumedForYear,
        dbLeaveTarget::setAllowedLeaveForForwarding, leaveSource::getAllowedLeaveForForwarding
    );

    fieldsUpdated.forEach((setter, getter) -> setter.accept(getter.get()));

    Optional.ofNullable(dbLeaveTarget.getId()).orElseGet(() -> {
          dbLeaveTarget.setFinancialYear(leaveSource.getFinancialYear());
          return null;
        }

    );

  }
}
