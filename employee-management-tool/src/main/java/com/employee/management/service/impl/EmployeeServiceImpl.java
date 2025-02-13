package com.employee.management.service.impl;

import com.employee.management.dto.EmployeeDetailsDTO;
import com.employee.management.dto.mapper.EmployeeMapper;
import com.employee.management.model.Employee;
import com.employee.management.model.Kids;
import com.employee.management.model.PastEmployment;
import com.employee.management.repo.EmployeeRepo;
import com.employee.management.service.EmployeeService;
import java.util.ArrayList;
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
  public Employee saveOrUpdateEmployee(Employee employeeToSave) {
    return employeeRepo.findByempId(employeeToSave.getEmpId())
        .map(existingEmployee -> {
          copyEmployeeDetails(existingEmployee, employeeToSave);
          return employeeRepo.save(existingEmployee);
        })
        .orElseGet(() -> {
          // Ensure relationships are set before saving a new employee
          setEmployeeReferences(employeeToSave);
          return employeeRepo.save(employeeToSave);
        });
  }

  private void setEmployeeReferences(Employee employee) {
    // Set Employee for Professional Details
    Optional.ofNullable(employee.getProfessionalDetails())
        .ifPresent(details -> details.setEmployee(employee));

    // Set Employee for Past Employments
    Optional.ofNullable(employee.getPastEmployments())
        .ifPresent(pastEmployments -> pastEmployments.forEach(emp -> emp.setEmployee(employee)));

    // Set Employee for Kids
    Optional.ofNullable(employee.getKids())
        .ifPresent(kids -> kids.forEach(kid -> kid.setEmployee(employee)));

    // Set Employee for Spouse
    Optional.ofNullable(employee.getSpouse())
        .ifPresent(spouse -> spouse.setEmployee(employee));
  }


  private void copyEmployeeDetails(Employee existingEmployee, Employee employeeToSave) {
    // Copy simple fields
    existingEmployee.setName(employeeToSave.getName());
    existingEmployee.setExtraMartialAffair(employeeToSave.isExtraMartialAffair());
    existingEmployee.setEmail(employeeToSave.getEmail());
    existingEmployee.setPhoneNumber(employeeToSave.getPhoneNumber());
    existingEmployee.setAddress(employeeToSave.getAddress());
    existingEmployee.setMarried(employeeToSave.isMarried());
    existingEmployee.setDreamWish(employeeToSave.getDreamWish());
    existingEmployee.setNatureBehavior(employeeToSave.getNatureBehavior());

    // Copy professional details
    Optional.ofNullable(existingEmployee.getProfessionalDetails())
        .ifPresent(professionalDetails -> {
          var newDetails = employeeToSave.getProfessionalDetails();
          if (newDetails != null) {
            professionalDetails.setCurrentCompany(newDetails.getCurrentCompany());
            professionalDetails.setCurrentDesignation(newDetails.getCurrentDesignation());
            professionalDetails.setCurrentSalary(newDetails.getCurrentSalary());
            professionalDetails.setCurrentLocation(newDetails.getCurrentLocation());

          }
          existingEmployee.setProfessionalDetails(professionalDetails);
        });

    // Update past employment list
    updateEmploymentHistory(existingEmployee, employeeToSave);

    // Update kids list
    updateKidsList(existingEmployee, employeeToSave);

    // Update spouse details

    Optional.ofNullable(existingEmployee.getSpouse())
        .ifPresent(existingSpouseDetails -> {
          var newSpouseDetails = employeeToSave.getSpouse();
          if (newSpouseDetails != null) {
            existingSpouseDetails.setGender(newSpouseDetails.getGender());
            existingSpouseDetails.setAge(newSpouseDetails.getAge());
            existingSpouseDetails.setName(newSpouseDetails.getName());
            existingSpouseDetails.setCurrentOccupation(newSpouseDetails.getCurrentOccupation());

          }
          existingEmployee.setSpouse(existingSpouseDetails);
        });

  }

  /**
   * Updates the past employment history of existingEmployee based on employeeToSave.
   */
  private void updateEmploymentHistory(Employee existingEmployee, Employee employeeToSave) {

    List<PastEmployment> existingEmployments = Optional.ofNullable(
            existingEmployee.getPastEmployments())
        .orElse(new ArrayList<>());
    List<PastEmployment> newEmployments = Optional.ofNullable(employeeToSave.getPastEmployments())
        .orElse(new ArrayList<>());

    var updatedEmployments = newEmployments.stream()
        .map(newEmployment -> existingEmployments.stream()
            .filter(
                existingEmp -> existingEmp.getCompanyName().equals(newEmployment.getCompanyName()))
            .findFirst()
            .map(existingEmp -> {

              existingEmp.setDesignation(newEmployment.getDesignation());
              existingEmp.setSalary(newEmployment.getSalary());
              return existingEmp;
            })
            .orElseGet(() -> {
              newEmployment.setEmployee(existingEmployee);
              return newEmployment;

            })
        )
        .toList();
    existingEmployments.clear(); // This might be the issue if the list is immutable
    existingEmployments.addAll(updatedEmployments);

  }

  /**
   * Updates the kids list of existingEmployee based on employeeToSave.
   */
  private void updateKidsList(Employee existingEmployee, Employee employeeToSave) {

    List<Kids> existingKids = Optional.ofNullable(existingEmployee.getKids())
        .orElse(new ArrayList<>());
    List<Kids> newKids = Optional.ofNullable(employeeToSave.getKids())
        .orElse(new ArrayList<>());

    List<Kids> updatedKids = newKids.stream()
        .map(newKid -> existingKids.stream().filter(existingKid ->
                existingKid.getName().equals(newKid.getName())).findFirst().map(existingKid -> {
              existingKid.setGender(newKid.getGender());
              existingKid.setAge(newKid.getAge());
              existingKid.setProfession(newKid.getProfession());
              return existingKid;
            }).orElseGet(() -> {
              newKid.setEmployee(existingEmployee);
              return newKid;

            })
        )
        .toList();

    existingKids.clear();
    existingKids.addAll(updatedKids);

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
