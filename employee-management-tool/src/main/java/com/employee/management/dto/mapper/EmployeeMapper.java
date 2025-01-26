package com.employee.management.dto.mapper;

import com.employee.management.dto.EmployeeDetailsDTO;
import com.employee.management.dto.KidsDetailsDTO;
import com.employee.management.dto.PastEmploymentDetailsDTO;
import com.employee.management.dto.ProfessionalDetailsDTO;
import com.employee.management.model.Employee;
import com.employee.management.model.Kids;
import com.employee.management.model.PastEmployment;
import com.employee.management.model.ProfessionalDetails;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  /**
   *
   */
  EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);


  @Mapping(source = "kids", target = "kidsDetails", qualifiedByName = "mapKidsList")
  @Mapping(source = "spouse", target = "spouseDetail")
  @Mapping(source = "professionalDetails", target = "professionalDetailsDTO", qualifiedByName = "mapProfessionalDetails")
  @Mapping(source = "pastEmployments", target = "pastEmploymentDetailsDTOS", qualifiedByName = "mapPastEmploymentList")
  @Mapping(source = "phoneNumber", target = "phoneNumber")
    //just example if filed have different name
  List<EmployeeDetailsDTO> toEmployeeDetailsDTOlist(List<Employee> employeesList);


  @Named("mapKidsList")
  default List<KidsDetailsDTO> mapKidsList(List<Kids> kidsList) {

    return Optional.ofNullable(kidsList).orElseGet(List::of).stream().map(
        kids -> KidsDetailsDTO.builder().age(kids.getAge()).name(kids.getName())
            .gender(kids.getGender()).profession(kids.getProfession()).build()).toList();


  }

  @Named("mapProfessionalDetails")
  default ProfessionalDetailsDTO mapProfessionalDetails(ProfessionalDetails professionalDetails) {

    return Optional.ofNullable(professionalDetails).map(details -> ProfessionalDetailsDTO.builder()
        .currentCompany(details.getCurrentCompany())
        .currentLocation(details.getCurrentLocation())
        .currentSalary(details.getCurrentSalary())
        .currentDesignation(details.getCurrentDesignation()).build()).orElse(null);

  }

  @Named("mapPastEmploymentList")
  default List<PastEmploymentDetailsDTO> mapPastEmploymentList(
      List<PastEmployment> pastEmployments) {

    return Optional.ofNullable(pastEmployments).orElseGet(List::of).stream().map(
        pastEmployment -> PastEmploymentDetailsDTO.builder()
            .designation(pastEmployment.getDesignation()).salary(pastEmployment.getSalary())
            .companyName(pastEmployment.getCompanyName())
            .build()).toList();
  }
}
