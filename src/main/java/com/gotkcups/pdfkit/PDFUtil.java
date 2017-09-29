/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotkcups.pdfkit;

import com.itextpdf.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author rfteves
 */
public class PDFUtil {

  

  public FBAUsage tiled(byte[] src, ByteArrayOutputStream dest)
    throws IOException, DocumentException, java.io.IOException {
    PdfReader reader = new PdfReader(src);
    Rectangle pagesize = reader.getPageSizeWithRotation(1);
    Rectangle mediabox = new Rectangle(pagesize.getWidth(), pagesize.getHeight() / 2);
    Document document = new Document(pagesize);
    PdfWriter writer = PdfWriter.getInstance(document, dest);
    document.open();
    int n = reader.getNumberOfPages();
    String text = "";
    try {
      text = PdfTextExtractor.getTextFromPage(reader, n);
    } catch (Throwable e) {
    }
    FBAUsage usage = FBAUsage.getInstance(text);
    if (usage.isValid()) {
      for (int p = 1; p <= n; p++) {
        for (int i = 0; i < 2; i++) {
          if (i == 0) {
            mediabox = new Rectangle(
              0, pagesize.getHeight() / 2, pagesize.getWidth(), pagesize.getHeight());
          } else {
            mediabox = new Rectangle(
              0, 0, pagesize.getWidth(), pagesize.getHeight() / 2);
          }
          document.setPageSize(mediabox);
          document.newPage();
          if (i == 0) {
            Image pagex = Image.getInstance(writer.getImportedPage(reader, p));
            pagex.setRotationDegrees(-90f);
            pagex.scalePercent(1.6f * 100);
            pagex.setAbsolutePosition(-612f, -160f);
            document.setPageSize(mediabox);
            document.add(pagex);
          } else {
            Image pagex = Image.getInstance(writer.getImportedPage(reader, p));
            pagex.scalePercent(1.28f * 100);
            pagex.setAbsolutePosition(-18f, -79f);
            document.setPageSize(mediabox);
            document.add(pagex);
          }
        }
      }
      document.close();
      reader.close();
    }
    return usage;
  }

  public void tilex(byte[] src, ByteArrayOutputStream dest)
    throws IOException, DocumentException, java.io.IOException {
    PdfReader reader = new PdfReader(src);
    Rectangle pagesize = reader.getPageSizeWithRotation(1);
    Document document = new Document(pagesize);
    PdfWriter writer = PdfWriter.getInstance(document, dest);
    document.open();
    int n = reader.getNumberOfPages();
    String text = "";
    try {
      text = PdfTextExtractor.getTextFromPage(reader, n);
    } catch (Throwable e) {
    }
    for (int p = 1; p <= n; p++) {
      for (int i = 0; i < 2; i++) {
        Rectangle mediabox = null;
        if (i == 0) {
          mediabox = new Rectangle(
            0, pagesize.getHeight() / 2, pagesize.getWidth(), pagesize.getHeight());
        } else {
          mediabox = new Rectangle(
            0, 0, pagesize.getWidth(), pagesize.getHeight() / 2);
        }
        document.setPageSize(mediabox);
        document.newPage();
        if (i == 0) {
          Image pagex = Image.getInstance(writer.getImportedPage(reader, p));
          pagex.setRotationDegrees(-180f);
          pagex.scalePercent(1.35f * 100);
          pagex.setAbsolutePosition(-70f, 348f);
          document.setPageSize(mediabox);
          document.add(pagex);
        } else {
          Image pagex = Image.getInstance(writer.getImportedPage(reader, p));
          pagex.scalePercent(1.28f * 100);
          pagex.setAbsolutePosition(-18f, -79f);
          document.setPageSize(mediabox);
          document.add(pagex);
        }
      }
    }
    document.close();
    reader.close();
  }
}
