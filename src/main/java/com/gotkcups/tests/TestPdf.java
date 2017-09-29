/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotkcups.tests;

import com.gotkcups.pdfkit.PDFUtil;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ricardo
 */
public class TestPdf {
  
  public static void main(String[] args) throws Exception {
    ByteArrayOutputStream target = new ByteArrayOutputStream();
    ByteArrayOutputStream source = new ByteArrayOutputStream();
    String fil = "C:/Users/Ricardo/Documents/package.pdf";
    byte[] s = IOUtils.toByteArray(new FileInputStream(fil));
    PDFUtil pdf = new PDFUtil();
    pdf.tilex(s, target);
    IOUtils.write(target.toByteArray(), new FileOutputStream("C:/Users/Ricardo/Documents/kkk.pdf"));
  }
}
