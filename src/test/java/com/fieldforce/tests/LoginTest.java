package com.fieldforce.tests;

import com.fieldforce.base.BaseTest;
import com.fieldforce.config.ConfigReader;
import com.fieldforce.pages.DashboardPage;
import com.fieldforce.pages.LoginPage;
import com.fieldforce.utils.ExcelUtil;
import com.fieldforce.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Automation Test Suite for Field Force Connect Login Journey.
 * Implements parametrization techniques (both in-line DataProvider and Apache POI Excel DataProvider)
 * and verifies validation feedback.
 */
public class LoginTest extends BaseTest {

    /**
     * Parametrized Data Provider supplying multiple sets of credentials:
     * - Valid Credentials (Positive)
     * - Invalid Password (Negative)
     * - Unregistered User (Negative)
     * - Invalid Email Format (Negative)
     * - Blank Credentials (Negative)
     */
    @DataProvider(name = "loginParamData")
    public Object[][] getLoginData() {
        return new Object[][]{
                // Username, Password, ScenarioType, ExpectedValidationMsg
                {"admin@fieldforce.com", "Nimap@2026", "VALID", "SUCCESS"},
                {"admin@fieldforce.com", "WrongPassword123", "INVALID_PASSWORD", "Login failed"},
                {"unregistered.user@nimap.com", "Nimap@2026", "UNREGISTERED", "Login failed"},
                {"invalid_email_format", "Nimap@2026", "INVALID_FORMAT", "Enter a valid email or 6-15 digit mobile number"},
                {"", "", "BLANK", "Email or mobile number is required"}
        };
    }

    /**
     * Alternative Excel-based Data Provider reading from LoginTestData.xlsx via Apache POI.
     */
    @DataProvider(name = "excelLoginData")
    public Object[][] getExcelLoginData() {
        String excelPath = "src/test/resources/testdata/LoginTestData.xlsx";
        Object[][] data = ExcelUtil.getTestData(excelPath, "LoginData");
        if (data.length == 0) {
            return getLoginData();
        }
        return data;
    }

    @Test(dataProvider = "loginParamData", description = "Parametrized Login Journey Verification", priority = 1)
    public void testLoginJourneyWithParametrization(String username, String password, String scenario, String expectedMsg) {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");

        loginPage.enterUsername(username);
        loginPage.enterPassword(password);

        if ("BLANK".equals(scenario)) {
            // Validate inline error messages for blank fields
            String errorMsg = loginPage.getFirstErrorMessage();
            Assert.assertTrue(errorMsg.contains(expectedMsg) || !loginPage.isSignInButtonEnabled(),
                    "Expected inline validation for blank fields. Observed: " + errorMsg);
            return;
        }

        if ("INVALID_FORMAT".equals(scenario)) {
            // Validate format error triggers on blur
            WaitUtils.sleep(500);
            String errorMsg = loginPage.getFirstErrorMessage();
            Assert.assertTrue(errorMsg.contains(expectedMsg) || errorMsg.contains("valid"),
                    "Expected invalid format validation error. Observed: " + errorMsg);
            return;
        }

        // Click Sign In
        loginPage.clickSignIn();

        if ("VALID".equals(scenario)) {
            DashboardPage dashboardPage = new DashboardPage(getDriver());
            WaitUtils.sleep(2000);
            String currentUrl = getDriver().getCurrentUrl();
            String toast = loginPage.getToastMessage();
            Assert.assertTrue(currentUrl.contains("dashboard") ||
                            toast.toLowerCase().contains("success") ||
                            dashboardPage.isDashboardLoaded(),
                    "User should be navigated to dashboard or receive success toast upon valid login. Current URL: " + currentUrl + ", Toast: " + toast);
        } else {
            // Negative scenarios should display error message or toast
            WaitUtils.sleep(1500);
            String toast = loginPage.getToastMessage();
            String inlineError = loginPage.getFirstErrorMessage();
            Assert.assertTrue(toast.contains(expectedMsg) ||
                            inlineError.contains(expectedMsg) ||
                            toast.toLowerCase().contains("failed") ||
                            toast.toLowerCase().contains("invalid"),
                    "Expected error feedback containing '" + expectedMsg + "'. Received Toast: '" + toast + "', Inline: '" + inlineError + "'");
        }
    }

    @Test(description = "Verify Navigation to Forgot Password Page", priority = 2)
    public void testNavigateToForgotPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.clickForgotPassword();
        WaitUtils.sleep(1000);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("forget-password"),
                "User should be navigated to forgot password page. Current URL: " + getDriver().getCurrentUrl());
    }

    @Test(description = "Verify Navigation to Sign Up Page", priority = 3)
    public void testNavigateToSignUp() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.clickSignUp();
        WaitUtils.sleep(1000);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("register"),
                "User should be navigated to register page. Current URL: " + getDriver().getCurrentUrl());
    }
}
