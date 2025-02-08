package com.employee.management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_gen")
  @SequenceGenerator(name = "users_seq_gen", sequenceName = "users_seq", allocationSize = 1)
  private Long id;
  private String username;
  private String role;
  private Long empId;
  private String password;
  private boolean isDefaultPasswordChanged = false;
}
