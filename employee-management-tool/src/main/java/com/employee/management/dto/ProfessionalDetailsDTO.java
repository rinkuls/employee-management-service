package com.employee.management.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalDetailsDTO {

  private String currentCompany;
  private String currentDesignation;
  private BigDecimal currentSalary;
  private String currentLocation;
}
