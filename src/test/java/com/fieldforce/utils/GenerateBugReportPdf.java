package com.fieldforce.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PrintOptions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Pure Java utility utilizing Selenium 4's native PrintsPage API
 * to render BUG_REPORT.html into a high-fidelity, printable BUG_REPORT.pdf.
 */
public class GenerateBugReportPdf {

    public static void main(String[] args) {
        System.out.println("=== Initiating PDF Generation from BUG_REPORT.html in Java ===");

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        try {
            File htmlFile = new File("BUG_REPORT.html");
            if (!htmlFile.exists()) {
                System.err.println("Error: BUG_REPORT.html does not exist at: " + htmlFile.getAbsolutePath());
                return;
            }

            String fileUrl = htmlFile.toURI().toString();
            System.out.println("Loading file URL: " + fileUrl);
            driver.get(fileUrl);

            // Allow DOM rendering
            Thread.sleep(1500);

            PrintsPage printer = (PrintsPage) driver;
            PrintOptions printOptions = new PrintOptions();
            printOptions.setBackground(true);
            printOptions.setScale(0.95);

            Pdf pdf = printer.print(printOptions);
            byte[] pdfBytes = Base64.getDecoder().decode(pdf.getContent());

            String outputPath = "BUG_REPORT.pdf";
            Files.write(Paths.get(outputPath), pdfBytes);

            System.out.println("SUCCESS: Generated " + outputPath + " (" + pdfBytes.length + " bytes)");
        } catch (Exception e) {
            System.err.println("Failed to generate PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
        }
    }
}
