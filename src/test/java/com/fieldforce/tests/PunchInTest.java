package com.fieldforce.tests;

import com.fieldforce.base.BaseTest;
import com.fieldforce.config.ConfigReader;
import com.fieldforce.pages.DashboardPage;
import com.fieldforce.pages.LoginPage;
import com.fieldforce.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Automation Test Suite for Attendance Punch In Journey.
 * Verifies Toast / Popup notification message after Punch In.
 */
public class PunchInTest extends BaseTest {

    @Test(description = "Verify the Toast / Popup message after the PunchIn", priority = 1)
    public void testVerifyPunchInToastOrPopup() {
        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());

        // Step 1: Perform Login
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        WaitUtils.sleep(2000);

        // Step 2: Trigger Punch In action
        dashboardPage.clickPunchIn();

        // Step 3: Handle confirmation modal if prompted
        dashboardPage.confirmPunchInIfModalAppears();

        // Step 4: Verify the Toast / Popup message after PunchIn
        String toastOrPopup = dashboardPage.getToastOrPopupMessage();
        System.out.println("Observed PunchIn Toast / Popup message: " + toastOrPopup);

        Assert.assertFalse(toastOrPopup.isEmpty(),
                "Toast / Popup confirmation message should be displayed after PunchIn");

        // Validate message content
        String lowerMsg = toastOrPopup.toLowerCase();
        Assert.assertTrue(lowerMsg.contains("punch") ||
                        lowerMsg.contains("success") ||
                        lowerMsg.contains("accepted") ||
                        lowerMsg.contains("attendance") ||
                        lowerMsg.contains("already"),
                "Toast / Popup message should confirm punch-in status. Observed message: " + toastOrPopup);
    }
}
