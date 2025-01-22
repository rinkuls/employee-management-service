package com.employee.management.service.impl;

import com.employee.management.exception.EmployeeNotFoundException;
import com.employee.management.repo.EmployeeRepo;
import com.employee.management.service.HtmlContentService;
import com.employee.management.service.PdfGenerationService;
import com.lowagie.text.DocumentException;
import jakarta.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
@RequiredArgsConstructor
public class PdfGenerationServiceImpl implements PdfGenerationService {

  private final HtmlContentService htmlContentService;

  private final EmployeeRepo employeeRepo;

  @Override
  @Transactional
  public ByteArrayOutputStream generatePdfFromHtml(String name) {

    return employeeRepo.findByName(name).map(employee -> {
          var data = Map.of("employee", employee);
          var htmlContent = htmlContentService.generateHtml("employee", data);
          return createPdfFromHtml(htmlContent);
        })

        .orElseThrow(
            () -> new EmployeeNotFoundException("Employee with name '" + name + "' not found"));


  }


  private ByteArrayOutputStream createPdfFromHtml(String htmlContent) {
    try (var pdfOutputStream = new ByteArrayOutputStream()) {
      var renderer = new ITextRenderer();
      renderer.setDocumentFromString(htmlContent);
      renderer.layout();
      renderer.createPDF(pdfOutputStream, false);
      renderer.finishPDF();
      return pdfOutputStream;
    } catch (DocumentException | IOException e) {
      throw new RuntimeException("Failed to generate PDF", e);
    }
  }
}
