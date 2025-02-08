package com.employee.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetails {

  @NonNull
  private String role;
  @NonNull
  private Integer empId;
  @NonNull
  private String username;
}
