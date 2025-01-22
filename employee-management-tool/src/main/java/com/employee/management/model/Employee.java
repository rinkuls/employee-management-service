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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Kids> kids;

  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
  private Spouse spouse;


  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
  private ProfessionalDetails professionalDetails;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PastEmployment> pastEmployments;


}
