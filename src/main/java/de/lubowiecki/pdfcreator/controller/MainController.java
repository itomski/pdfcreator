package de.lubowiecki.pdfcreator.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import de.lubowiecki.pdfcreator.model.Doc;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.WebConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

@Controller
@RequestMapping("/docs")
public class MainController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private TemplateEngine templateEngine;

    private final Doc DOC;

    public MainController() {
        DOC = new Doc();
        DOC.setHeader("Kochen ohne Fett");
        DOC.setAuthor("Bob van Bobsen");
        DOC.setBody("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque consectetur doloremque ducimus fuga libero, molestiae officia recusandae sint voluptate voluptatem. Adipisci autem corporis deserunt dicta dolor eaque exercitationem facere facilis fugit harum illo laboriosam magnam magni maiores, maxime natus necessitatibus nisi non obcaecati optio perferendis quam quasi quos repudiandae saepe sequi suscipit voluptatum! Aliquam architecto cumque dolorem, doloremque et eum eveniet ex explicabo illo laudantium molestias quas quisquam reiciendis repellendus soluta tempora vero. Accusamus assumenda, commodi debitis ducimus esse illo, molestias nemo quos, sed sit suscipit tempore unde voluptates. Accusamus atque cumque cupiditate iste itaque laudantium obcaecati quo repellat, voluptatem.");
        DOC.setFooter("Copyright by Bob van Bobsen");
    }

    @GetMapping
    public String asHtml(Model model) {
        model.addAttribute("doc", DOC);
        return "doc";
    }

    @GetMapping("/pdf")
    public ResponseEntity<?> asPdf(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        Context ctx = new Context(); // Hat ähnliche Funktion wie das Model
        ctx.setVariable("doc", DOC);
        String html = templateEngine.process("pdf", ctx); // Vorlage und Daten zum HTML-String verarbeiten

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties props = new ConverterProperties();
        props.setBaseUri("http://localhost:8080");

        HtmlConverter.convertToPdf(html, target, props); // PDF erzeugen und als Bytes in ein ByteArray schreiben

        byte[] bytes = target.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=doc.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes); // PDF als Byte-Array an den Aufrufer senden
    }
}
