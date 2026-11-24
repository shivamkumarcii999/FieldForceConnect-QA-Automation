package com.fieldforce.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * Manages the singleton instance of ExtentReports.
 */
public class ExtentManager {
    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportDir = System.getProperty("user.dir") + File.separator + "reports";
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String reportPath = reportDir + File.separator + "ExtentReport.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Field Force Connect - Automation Test Report");
            sparkReporter.config().setReportName("QA Automation Results - Nimap Machine Test");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("UTF-8");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "Field Force Connect");
            extent.setSystemInfo("Target URL", "https://test.fieldforceconnect.com/");
            extent.setSystemInfo("Environment", "QA / Staging");
            extent.setSystemInfo("Framework", "Selenium WebDriver 4 + TestNG");
            extent.setSystemInfo("Language", "Java 17");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Tester", "QA Automation Engineer");
        }
        return extent;
    }
}
