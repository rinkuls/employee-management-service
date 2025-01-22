package com.employee.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.employee.management.model")
public class EmployeeManagementToolApplication {

  public static void main(String[] args) {
    SpringApplication.run(EmployeeManagementToolApplication.class, args);
  }

}
