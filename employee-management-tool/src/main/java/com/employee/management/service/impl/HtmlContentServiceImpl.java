package com.employee.management.service.impl;

import com.employee.management.model.Employee;
import com.employee.management.service.HtmlContentService;
import freemarker.template.Configuration;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HtmlContentServiceImpl implements HtmlContentService {

  private final Configuration freemarkerConfig;


  @Override
  public String generateHtml(String templateName, Map<String, Employee> data) {
    try {
      var template = freemarkerConfig.getTemplate(templateName + ".ftl");
      var outputStream = new ByteArrayOutputStream();
      template.process(data, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
      return outputStream.toString(StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Failed to process Freemarker template: " + e.getMessage(), e);
    }
  }
}
