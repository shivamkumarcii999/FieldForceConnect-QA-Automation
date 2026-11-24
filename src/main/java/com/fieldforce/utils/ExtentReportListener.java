package com.fieldforce.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.fieldforce.base.BaseTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Listener integrating ExtentReports 5 for automated report generation.
 */
public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("=== Test Suite Execution Started: " + context.getName() + " ===");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("=== Test Suite Execution Finished: " + context.getName() + " ===");
        if (extent != null) {
            extent.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentTest extentTest = extent.createTest(testName, description != null ? description : testName);
        test.set(extentTest);
        test.get().log(Status.INFO, "Starting Test Case: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, MarkupHelper.createLabel(result.getMethod().getMethodName() + " PASSED", ExtentColor.GREEN));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, MarkupHelper.createLabel(result.getMethod().getMethodName() + " FAILED", ExtentColor.RED));
        test.get().log(Status.FAIL, result.getThrowable());

        // Capture screenshot on failure
        try {
            String screenshotBase64 = BaseTest.captureScreenshotBase64();
            if (screenshotBase64 != null && !screenshotBase64.isEmpty()) {
                test.get().addScreenCaptureFromBase64String(screenshotBase64, "Failure Screenshot");
            }
        } catch (Exception e) {
            test.get().log(Status.WARNING, "Failed to capture failure screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, MarkupHelper.createLabel(result.getMethod().getMethodName() + " SKIPPED", ExtentColor.YELLOW));
        if (result.getThrowable() != null) {
            test.get().log(Status.SKIP, result.getThrowable());
        }
    }
}
