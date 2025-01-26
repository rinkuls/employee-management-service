package com.employee.management.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDetailsDTO {

  private String name;
  private Long empId;
  private String email;
  private String phoneNumber;
  private String address;
  private boolean married;
  private boolean extraMartialAffair;
  private String dreamWish;
  private String natureBehavior;

  private List<KidsDetailsDTO> kidsDetails;
  private List<SpouseDetailsDTO> spouseDetail;
  private ProfessionalDetailsDTO professionalDetailsDTO;
  private List<PastEmploymentDetailsDTO> pastEmploymentDetailsDTOS;

}
