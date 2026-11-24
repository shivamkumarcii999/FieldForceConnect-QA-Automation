package com.fieldforce.pages;

import com.fieldforce.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Dashboard & Attendance / Punch-In Journey.
 */
public class DashboardPage {
    private final WebDriver driver;

    // Locators
    private final By punchInButton = By.xpath("//button[contains(text(), 'Punch In') or contains(., 'Punch In') or contains(@class, 'punchIn') or contains(@id, 'punch')]");
    private final By punchOutButton = By.xpath("//button[contains(text(), 'Punch Out') or contains(., 'Punch Out')]");
    private final By punchStatusText = By.xpath("//*[contains(text(), 'Punched In') or contains(text(), 'Punched Out')]");
    private final By punchConfirmModal = By.xpath("//div[@role='dialog' or contains(@class, 'modal')]");
    private final By modalConfirmYesButton = By.xpath("//button[contains(text(), 'Yes') or contains(text(), 'Confirm') or contains(text(), 'Punch')]");
    private final By toastNotification = By.xpath("//div[@role='status' or contains(@class, 'toast') or contains(@class, 'go2072408551')]");
    private final By customerMenuLink = By.xpath("//a[contains(@href, '/customer') or contains(@href, '/customers') or contains(., 'Customers') or contains(., 'Customer')]");
    private final By userProfileBadge = By.xpath("//div[contains(@class, 'avatar') or contains(@class, 'profile') or contains(@class, 'user')]");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isDashboardLoaded() {
        try {
            return WaitUtils.waitForUrlContains(driver, "dashboard", 10) ||
                    driver.findElements(punchInButton).size() > 0 ||
                    driver.findElements(userProfileBadge).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickPunchIn() {
        try {
            WebElement button = WaitUtils.waitForClickable(driver, punchInButton, 10);
            button.click();
        } catch (Exception e) {
            // Fallback Javascript click
            WebElement button = driver.findElement(punchInButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    public void confirmPunchInIfModalAppears() {
        try {
            if (driver.findElements(punchConfirmModal).size() > 0) {
                WebElement yesBtn = WaitUtils.waitForClickable(driver, modalConfirmYesButton, 5);
                yesBtn.click();
            }
        } catch (Exception ignored) {
        }
    }

    public String getToastOrPopupMessage() {
        // Check Toast first
        try {
            WebElement toast = WaitUtils.waitForVisibility(driver, toastNotification, 8);
            String text = toast.getText().trim();
            if (!text.isEmpty()) {
                return text;
            }
        } catch (Exception ignored) {
        }

        // Check Modal dialog text
        try {
            WebElement modal = WaitUtils.waitForVisibility(driver, punchConfirmModal, 5);
            return modal.getText().trim();
        } catch (Exception ignored) {
        }

        return "";
    }

    public boolean isPunchedIn() {
        try {
            return driver.findElements(punchOutButton).size() > 0 ||
                    driver.findElements(punchStatusText).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void navigateToCustomers() {
        try {
            WebElement link = WaitUtils.waitForClickable(driver, customerMenuLink, 10);
            link.click();
        } catch (Exception e) {
            // Fallback direct URL navigation
            driver.get("https://test.fieldforceconnect.com/customers");
        }
    }
}
