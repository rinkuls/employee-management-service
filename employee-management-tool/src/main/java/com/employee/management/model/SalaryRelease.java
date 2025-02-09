package com.employee.management.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryRelease {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salary_release_seq")
  @SequenceGenerator(name = "salary_release_seq", sequenceName = "salary_release_seq", allocationSize = 1)
  private Long id;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "salary_structure_id", nullable = false)
  private SalaryStructure salaryStructure;

  @Column(nullable = false, length = 7) // Format 'YYYY-MM'
  private String salaryMonth;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ReleaseStatus releaseStatus;

  private BigDecimal amountReleased;


  private LocalDateTime releaseDate;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

