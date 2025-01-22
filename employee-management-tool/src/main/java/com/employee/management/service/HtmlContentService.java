package com.employee.management.service;

import com.employee.management.model.Employee;
import java.util.Map;

public interface HtmlContentService {

  String generateHtml(String templateName, Map<String, Employee> data);
}
