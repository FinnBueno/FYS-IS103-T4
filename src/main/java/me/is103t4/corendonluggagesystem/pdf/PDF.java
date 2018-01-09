/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.pdf;


import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.util.Matrix;

/**
 * @author Tim van Ekert & Finn Bon
 */
public class PDF {

    // finals
    private static final float LEFT_OFFSET = 85;
    private static final float TOP_OFFSET = 300;
    private static final float PARAGRAPH_SPACING = 35;
    private static final Logger LOGGER = Logger.getLogger(PDF.class.getName());
    private static final float FONT_SIZE = 16;
    public static final String REPLACE_STRING = "<!>";

    // values needed to create the PDF
    private final String title;
    private final boolean signatureLine;
    private final List<String> paragraphs;
    private final Stage stage;
    private final String name;

    /**
     * @param title The file's title in the PDF
     * @param signatureLine Whether the file needs a signature line
     * @param stage The stage used to open a potential warning
     * @param paragraphs The content
     */
    public PDF(String title, boolean signatureLine, Stage stage, String... paragraphs) {
        this("", title, signatureLine, stage, paragraphs);
    }

    /**
     * @param name The file's name
     * @param title The file's title in the PDF
     * @param signatureLine Whether the file needs a signature line
     * @param stage The stage used to open a potential warning
     * @param paragraphs The content
     */
    public PDF(String name, String title, boolean signatureLine, Stage stage, String... paragraphs) {
        this.name = name;
        this.title = title;
        this.signatureLine = signatureLine;
        this.paragraphs = Arrays.asList(paragraphs);
        this.stage = stage;
    }

    /**
     * @param title The file's title in the PDF
     * @param stage The stage used to open a potential warning
     * @param paragraphs The content
     * signatureLine defaults to false
     */
    public PDF(String title, Stage stage, String... paragraphs) {
        this(title, true, stage, paragraphs);
    }

    /**
     * Exports the PDF and prompts the user to find a place to save it
     */
    public void exportPDF() {

        try (PDDocument document = PDDocument.load(getClass().getResourceAsStream("/pdf/PDF_Template.pdf"));
             InputStream is = getClass().getResourceAsStream("/font/arial.ttf")) {

            // font
            PDFont font = PDType0Font.load(document, is, true);

            PDPage page = document.getPage(0);
            try (PDPageContentStream stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode
                    .APPEND, false)) {

                stream.beginText();
                stream.setNonStrokingColor(Color.BLACK);

                // title
                stream.setFont(font, FONT_SIZE * 3);
                stream.setTextMatrix(Matrix.getTranslateInstance(LEFT_OFFSET, page.getMediaBox().getHeight() -
                        TOP_OFFSET + FONT_SIZE * 3));
                stream.showText(title);

                stream.setFont(font, FONT_SIZE);

                float allowedWidth = page.getMediaBox().getWidth() - LEFT_OFFSET * 3;
                float yOffset = 0;
                // loop through paragraphs to write them to the PDF
                for (String text : paragraphs) {

                    // make the strings fit
                    double width = font.getStringWidth(text) * FONT_SIZE;

                    String[] lines = new String[0];

                    if (width > allowedWidth)
                        lines = skipToNextLine(text, allowedWidth, font).split(REPLACE_STRING);

                    stream.setTextMatrix(Matrix.getTranslateInstance(LEFT_OFFSET, page.getMediaBox().getHeight() -
                            TOP_OFFSET - yOffset));

                    // write lines individually
                    int i = 0;
                    for (String line : lines) {
                        stream.showText(line);
                        stream.newLineAtOffset(i == 0 ? 5 : 0, -font.getFontDescriptor().getFontBoundingBox()
                                .getHeight() * FONT_SIZE / 1000);
                        i++;
                    }

                    yOffset += font.getFontDescriptor().getFontBoundingBox().getHeight() * FONT_SIZE / 1000 * i +
                            PARAGRAPH_SPACING;
                }

                // draw signature line if required
                if (signatureLine) {
                    stream.setTextMatrix(Matrix.getTranslateInstance(LEFT_OFFSET, 115));
                    stream.showText("Signature: ");
                    stream.endText();

                    stream.moveTo(LEFT_OFFSET, 50);
                    stream.lineTo(page.getMediaBox().getWidth() - LEFT_OFFSET, 50);
                    stream.fill();
                } else
                    stream.endText();
            }

            // open directory selector
            File file;
            do {
                file = openDirectorySelector();
                if (file == null || !file.isDirectory()) {
                    new AlertBuilder(Alert.AlertType.ERROR, "Error!", "Must select a directory!", "You must select a " +
                            "directory in order to create the PDF file.").showAndWait();
                }
            } while (file == null || !file.isDirectory());

            // save document
            document.save(new File(file.getPath() + File.separator + "GenPDF-" + name + "-" + UUID.randomUUID()
                    .toString().substring(0, 4) + ".pdf"));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Opens the directory selector
     * @return The selected directory
     */
    private File openDirectorySelector() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select Directory to Create PDF in");
        dirChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        return dirChooser.showDialog(stage);
    }

    /**
     * Bring insert REPLACE_STRING where needed
     * @param text The string to run the code on
     * @param maxWidth The maximum allowed width
     * @param font The font to use
     * @return The string with REPLACE_STRING inserted where needed
     */
    private String skipToNextLine(String text, double maxWidth, PDFont font) {

        maxWidth *= 1000;
        String[] words = text.split(" ");
        StringBuilder builder = new StringBuilder();
        float currentWidth = 0;

        for (String word : words) {

            // get total string's width
            try {
                currentWidth += font.getStringWidth(word) * FONT_SIZE;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // skip to next line if needed
            if (currentWidth > maxWidth || word.endsWith(REPLACE_STRING)) {
                currentWidth = 0;
                if (!word.equals(REPLACE_STRING))
                    builder.append(REPLACE_STRING);
            } else
                builder.append(" ");
            builder.append(word);

        }
        return builder.toString();
    }

}
