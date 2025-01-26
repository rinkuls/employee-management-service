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
public class PastEmploymentDetailsDTO {

  private String companyName;
  private String designation;
  private BigDecimal salary;
}
