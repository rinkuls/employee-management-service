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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Kids {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kids_seq")
  @SequenceGenerator(name = "kids_seq", sequenceName = "kids_seq", allocationSize = 1)
  private Long id;

  private String name;
  private int age;
  private String gender;
  private String profession;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employee_id")
  private Employee employee;

}
