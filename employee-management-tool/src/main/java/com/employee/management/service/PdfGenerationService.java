package com.employee.management.service;

import com.lowagie.text.DocumentException;
import java.io.ByteArrayOutputStream;

public interface PdfGenerationService {

  ByteArrayOutputStream generatePdfFromHtml(String name) throws DocumentException;
}
