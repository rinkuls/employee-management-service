package com.employee.management.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
  @SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 1)
  private Long id;

  @NotNull(message = "Name is required")
  private String name;
  private Long empId;
  private String email;
  private String phoneNumber;
  private String address;
  private boolean married;
  @Column(name = "extra_martial_affair")
  private boolean extraMartialAffair;
  private String dreamWish;
  private String natureBehavior;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<Kids> kids;

  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
  @ToString.Exclude
  private Spouse spouse;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<EmployeeLeave> employeeLeave;

  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
  @ToString.Exclude
  private ProfessionalDetails professionalDetails;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<PastEmployment> pastEmployments;

  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
  @ToString.Exclude
  private SalaryStructure salaryStructure;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<SalaryRelease> salaryReleases;


}
