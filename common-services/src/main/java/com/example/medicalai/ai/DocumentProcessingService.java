package com.example.medicalai.ai;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@Service
public class DocumentProcessingService {
    private final boolean ocrEnabled;
    private final String tessData;

    public DocumentProcessingService(@Value("${ocr.enabled:false}") boolean ocrEnabled,
                                     @Value("${tesseract.datapath:}") String tessData) {
        this.ocrEnabled = ocrEnabled;
        this.tessData = tessData;
    }

    public String extractCombinedText(List<MultipartFile> files) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (MultipartFile f : files) {
            if (f == null || f.isEmpty()) continue;
            String ct = f.getContentType() == null ? "" : f.getContentType();
            if (MediaType.APPLICATION_PDF_VALUE.equals(ct) || f.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                File tmp = File.createTempFile("pdf-", ".pdf");
                try (OutputStream out = new FileOutputStream(tmp)) {
                    f.getInputStream().transferTo(out);
                }
                sb.append(extractPdf(tmp)).append(" ");
                tmp.delete();
            } else if (ct.startsWith("image/") || f.getOriginalFilename().toLowerCase().matches(".*\\.(jpg|jpeg|png)$")) {
                if (!ocrEnabled)
                    throw new IllegalStateException("OCR is required for images but OCR is disabled. Set ocr.enabled=true and tesseract.datapath.");
                sb.append(ocrImage(f.getInputStream())).append(" ");
            } else {
                throw new IOException("Unsupported file type: " + f.getOriginalFilename());
            }
        }
        return sb.toString().trim();
    }

    private String extractPdf(File pdfFile) throws IOException {
        try (PDDocument doc = PDDocument.load(pdfFile)){
            org.apache.pdfbox.text.PDFTextStripper stripper =
                    new org.apache.pdfbox.text.PDFTextStripper();

            String text = stripper.getText(doc);
            if (text != null && text.trim().length() > 100) {
                return text;
            }
            if (!ocrEnabled) {
                return text == null ? "" : text; // return whatever we got
            }

            // OCR fallback
            PDFRenderer renderer = new PDFRenderer(doc);
            StringBuilder sb = new StringBuilder();
            Tesseract t = new Tesseract();

            if (tessData != null && !tessData.trim().isEmpty()) {
                t.setDatapath(tessData);
            }
            t.setLanguage("eng");

            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                BufferedImage img = renderer.renderImageWithDPI(i, 300, ImageType.RGB);
                try {
                    sb.append(t.doOCR(img)).append(' ');
                } catch (TesseractException e) {
                    throw new IOException(e);
                }
            }
            return sb.toString();
        }
    }

    private String ocrImage(InputStream in) throws IOException {
        try {
            BufferedImage img = ImageIO.read(in);
            if (img == null) throw new IOException("Unreadable image");
            Tesseract t = new Tesseract();
//            if (tessData != null && !tessData.isBlank()) t.setDatapath(tessData);

            if (tessData != null && !tessData.trim().isEmpty()) {
                t.setDatapath(tessData);
            }

            t.setLanguage("eng");
            return t.doOCR(img);
        } catch (TesseractException ex) {
            throw new IOException(ex);
        }
    }
}
