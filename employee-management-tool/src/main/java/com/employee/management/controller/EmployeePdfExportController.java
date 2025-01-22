package com.employee.management.controller;

import com.employee.management.exception.InvalidEmployeeDataException;
import com.employee.management.service.PdfGenerationService;
import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
public class EmployeePdfExportController {

  private final PdfGenerationService pdfGenerationService;


  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "PDF generated successfully"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @Operation(
      summary = "Generate PDF from Freemarker template",
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @GetMapping("/{name}")
  @Transactional
  public ResponseEntity<byte[]> generatePdf(
      @PathVariable String name

  ) {
    var validatedName = Optional.ofNullable(name)
        .filter(StringUtils::hasText)
        .orElseThrow(() -> new InvalidEmployeeDataException("Employee data is missing."));

    return generatePdfResponse(validatedName);


  }

  private ResponseEntity<byte[]> generatePdfResponse(String name) {
    try {
      var pdfBytes = pdfGenerationService.generatePdfFromHtml(name).toByteArray();

      // Build headers and create response
      var headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=\"employee.pdf\"");

      return ResponseEntity.ok()
          .headers(headers)
          .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
          .body(pdfBytes);

    } catch (DocumentException ex) {

      throw new RuntimeException("Failed to generate PDF for employee: " + name, ex);
    }
  }
}
