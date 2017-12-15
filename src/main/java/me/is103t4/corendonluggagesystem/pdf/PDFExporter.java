/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.pdf;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author Tim van Ekert
 */
public class PDFExporter {
    
    public static void handlePDF() {

        try (PDDocument document = new PDDocument()) {
            
            PDPage page = new PDPage();
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
                     
            contentStream.beginText();

            contentStream.newLineAtOffset(50, 1);
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);

            contentStream.showText("Passagier informatie");

            contentStream.endText();
            
            contentStream.close();
                      
            document.addPage(page);
            
            document.save("Test.pdf");
            
            /*PDPage blankPage = new PDPage();

            document.addPage(blankPage);

            
            
            PDPage page = document.getPage(0);
            PDImageXObject pdImage = PDImageXObject.createFromFile(PDFExporter.class.getResource("/images/CorendonLogo.png").getFile(), document);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);    
            contentStream.beginText();

            contentStream.newLineAtOffset(50, 1);
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);

            contentStream.showText("Passagier informatie");

            contentStream.endText();
            System.out.println("Text added");

            File save = new File("/Users/timvanekert/Desktop/" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "" + UUID.randomUUID() + ".pdf");
            if (!save.exists())
                save.createNewFile();
            
            document.save("Test.pdf");
            document.close();*/
            
        } catch (IOException ex) {
            Logger.getLogger(PDFExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
