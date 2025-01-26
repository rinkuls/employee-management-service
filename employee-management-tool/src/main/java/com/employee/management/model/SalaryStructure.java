package com.employee.management.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class SalaryStructure {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salary_structure_seq")
  @SequenceGenerator(name = "salary_structure_seq", sequenceName = "salary_structure_seq", allocationSize = 1)
  private Long id;


  private BigDecimal basicSalary;
  private BigDecimal hra;
  private BigDecimal allowances;
  private BigDecimal deductions;

  @Column(nullable = false)
  private BigDecimal totalSalary;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;
}
