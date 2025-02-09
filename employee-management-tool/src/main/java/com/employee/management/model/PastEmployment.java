package com.employee.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PastEmployment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "past_employment_seq")
  @SequenceGenerator(name = "past_employment_seq", sequenceName = "past_employment_seq", allocationSize = 1)
  private Long id;

  private String companyName;
  private String designation;
  private BigDecimal salary;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "employee_id")
  @JsonIgnore
  private Employee employee;

}