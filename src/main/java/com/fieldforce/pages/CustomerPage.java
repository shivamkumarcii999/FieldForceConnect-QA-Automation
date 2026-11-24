package com.fieldforce.pages;

import com.fieldforce.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Customer Management & Add Customer Journey.
 */
public class CustomerPage {
    private final WebDriver driver;

    // Locators
    private final By actionsButton = By.xpath("//button[contains(., 'Actions') or contains(text(), 'Actions')]");
    private final By newCustomerOption = By.xpath("//*[contains(text(), 'New Customer') or contains(., 'New Customer')]");
    private final By addCustomerDrawer = By.xpath("//div[@role='dialog' or contains(@class, 'drawer') or contains(@class, 'modal')]");

    // Add Customer Form Fields
    private final By customerNameInput = By.xpath("//input[@name='LeadName' or contains(@placeholder, 'Customer Name') or contains(@placeholder, 'Lead')]");
    private final By contactPersonInput = By.xpath("//input[@name='PersonName' or contains(@placeholder, 'Contact Person')]");
    private final By mobileNumberInput = By.xpath("//input[@name='MobileNo' or contains(@placeholder, 'Mobile')]");
    private final By emailInput = By.xpath("//input[@name='Email' or contains(@placeholder, 'Email')]");
    private final By addressInput = By.xpath("//input[@name='Address' or contains(@placeholder, 'Address')]");

    private final By submitButton = By.xpath("//button[@type='submit' or contains(text(), 'Submit') or contains(text(), 'Save')]");
    private final By toastNotification = By.xpath("//div[@role='status' or contains(@class, 'toast') or contains(@class, 'go2072408551')]");

    public CustomerPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openAddCustomerForm() {
        try {
            WebElement actions = WaitUtils.waitForClickable(driver, actionsButton, 10);
            actions.click();
            WaitUtils.sleep(500);
            WebElement newCust = WaitUtils.waitForClickable(driver, newCustomerOption, 5);
            newCust.click();
        } catch (Exception e) {
            // Direct click if new customer button is directly visible
            WebElement newCust = WaitUtils.waitForClickable(driver, newCustomerOption, 5);
            newCust.click();
        }
    }

    public CustomerPage enterCustomerName(String name) {
        WebElement element = WaitUtils.waitForVisibility(driver, customerNameInput, 10);
        element.clear();
        element.sendKeys(name);
        return this;
    }

    public CustomerPage enterContactPerson(String contactPerson) {
        try {
            WebElement element = driver.findElement(contactPersonInput);
            element.clear();
            element.sendKeys(contactPerson);
        } catch (Exception ignored) {
        }
        return this;
    }

    public CustomerPage enterMobile(String mobile) {
        try {
            WebElement element = driver.findElement(mobileNumberInput);
            element.clear();
            element.sendKeys(mobile);
        } catch (Exception ignored) {
        }
        return this;
    }

    public CustomerPage enterEmail(String email) {
        try {
            WebElement element = driver.findElement(emailInput);
            element.clear();
            element.sendKeys(email);
        } catch (Exception ignored) {
        }
        return this;
    }

    public CustomerPage enterAddress(String address) {
        try {
            WebElement element = driver.findElement(addressInput);
            element.clear();
            element.sendKeys(address);
        } catch (Exception ignored) {
        }
        return this;
    }

    public void clickSubmit() {
        try {
            WebElement btn = WaitUtils.waitForClickable(driver, submitButton, 10);
            btn.click();
        } catch (Exception e) {
            WebElement btn = driver.findElement(submitButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void addCustomer(String customerName, String contactPerson, String mobile, String email, String address) {
        openAddCustomerForm();
        enterCustomerName(customerName);
        if (contactPerson != null && !contactPerson.isEmpty()) enterContactPerson(contactPerson);
        if (mobile != null && !mobile.isEmpty()) enterMobile(mobile);
        if (email != null && !email.isEmpty()) enterEmail(email);
        if (address != null && !address.isEmpty()) enterAddress(address);
        clickSubmit();
    }

    public String getToastMessage() {
        try {
            WebElement toast = WaitUtils.waitForVisibility(driver, toastNotification, 8);
            return toast.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isCustomerAddedSuccessfully() {
        String msg = getToastMessage();
        return msg.toLowerCase().contains("success") || msg.toLowerCase().contains("customer added");
    }
}
