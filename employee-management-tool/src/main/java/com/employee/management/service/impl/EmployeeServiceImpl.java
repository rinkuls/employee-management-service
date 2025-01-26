package com.employee.management.service.impl;

import com.employee.management.dto.EmployeeDetailsDTO;
import com.employee.management.dto.mapper.EmployeeMapper;
import com.employee.management.model.Employee;
import com.employee.management.repo.EmployeeRepo;
import com.employee.management.service.EmployeeService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


  private final EmployeeRepo employeeRepo;
  private final EmployeeMapper employeeMapper;


  @Override
  public Employee saveOrUpdateEmployee(Employee employee) {
    return employeeRepo.findByEmpId(employee.getEmpId())
        .map(existingEmployee -> {
          copyEmployeeDetails(existingEmployee, employee);
          return employeeRepo.save(existingEmployee);
        })
        .orElseGet(() -> {
          copyEmployeeDetails(employee, employee);
          return employeeRepo.save(employee);
        });
  }

  private void copyEmployeeDetails(Employee target, Employee source) {
    // Copy simple fields
    target.setName(source.getName());
    target.setExtraMartialAffair(source.isExtraMartialAffair());
    target.setEmail(source.getEmail());
    target.setPhoneNumber(source.getPhoneNumber());
    target.setAddress(source.getAddress());
    target.setMarried(source.isMarried());
    target.setDreamWish(source.getDreamWish());
    target.setNatureBehavior(source.getNatureBehavior());

    // Copy complex associations
    Optional.ofNullable(source.getProfessionalDetails())
        .ifPresent(professionalDetails -> {
          professionalDetails.setEmployee(target);
          target.setProfessionalDetails(professionalDetails);
        });

    Optional.ofNullable(source.getPastEmployments())
        .ifPresent(pastEmployments -> {
          pastEmployments.forEach(pastEmployment -> pastEmployment.setEmployee(target));
          target.setPastEmployments(pastEmployments);
        });

    Optional.ofNullable(source.getKids())
        .ifPresent(kids -> {
          kids.forEach(kid -> kid.setEmployee(target));
          target.setKids(kids);
        });

    Optional.ofNullable(source.getSpouse())
        .ifPresent(spouse -> {
          spouse.setEmployee(target);
          target.setSpouse(spouse);
        });
  }


  @Override
  public Optional<Employee> findByName(String name) {
    return employeeRepo.findByName(name);
  }


  @Override
  public List<EmployeeDetailsDTO> getAllEmployees() {

    return employeeMapper.toEmployeeDetailsDTOlist(employeeRepo.findAll());
  }


}
