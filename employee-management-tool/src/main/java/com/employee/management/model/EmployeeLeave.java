package com.employee.management.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeLeave {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_leave_seq")
  @SequenceGenerator(name = "employee_leave_seq", sequenceName = "employee_leave_seq", allocationSize = 1)
  private Long id;

  private Integer totalLeaveGrantedForYear;
  private Integer leaveConsumedForYear;
  private Integer leaveBalancedForYear;
  private Integer financialYear;
  private Integer leaveCarryForwardForYear;
  private Integer allowedLeaveForForwarding;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employee_id")
  private Employee employee;

}