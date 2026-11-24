package com.fieldforce.pages;

import com.fieldforce.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Field Force Connect Login Page.
 */
public class LoginPage {
    private final WebDriver driver;

    // Locators
    private final By usernameInput = By.xpath("//input[@name='username' or @placeholder='Enter Email or Mobile Number']");
    private final By passwordInput = By.xpath("//input[@name='password' or @type='password']");
    private final By signInButton = By.xpath("//button[@type='submit' or contains(text(), 'Sign in') or contains(text(), 'Sign In')]");
    private final By forgotPasswordLink = By.xpath("//a[contains(@href, 'forget-password') or contains(text(), 'Forgot Password')]");
    private final By signInWithOtpButton = By.xpath("//button[contains(text(), 'Sign In With OTP') or contains(., 'Sign In With OTP')]");
    private final By signUpLink = By.xpath("//a[contains(@href, 'register') or contains(text(), 'Sign up')]");
    private final By fieldErrorMessages = By.cssSelector("p.Mui-error, span.Mui-error, .MuiFormHelperText-root.Mui-error");
    private final By toastNotification = By.xpath("//div[@role='status' or contains(@class, 'toast') or contains(@class, 'go2072408551')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage enterUsername(String username) {
        WebElement element = WaitUtils.waitForVisibility(driver, usernameInput, 15);
        element.clear();
        if (username != null && !username.isEmpty()) {
            element.sendKeys(username);
        }
        return this;
    }

    public LoginPage enterPassword(String password) {
        WebElement element = WaitUtils.waitForVisibility(driver, passwordInput, 15);
        element.clear();
        if (password != null && !password.isEmpty()) {
            element.sendKeys(password);
        }
        return this;
    }

    public void clickSignIn() {
        WebElement button = WaitUtils.waitForClickable(driver, signInButton, 15);
        button.click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
    }

    public void clickForgotPassword() {
        WebElement link = WaitUtils.waitForClickable(driver, forgotPasswordLink, 10);
        link.click();
    }

    public void clickSignInWithOtp() {
        WebElement button = WaitUtils.waitForClickable(driver, signInWithOtpButton, 10);
        button.click();
    }

    public void clickSignUp() {
        WebElement link = WaitUtils.waitForClickable(driver, signUpLink, 10);
        link.click();
    }

    public boolean isSignInButtonEnabled() {
        try {
            WebElement button = driver.findElement(signInButton);
            return button.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstErrorMessage() {
        try {
            WebElement error = WaitUtils.waitForVisibility(driver, fieldErrorMessages, 5);
            return error.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public List<String> getAllErrorMessages() {
        WaitUtils.sleep(500);
        List<WebElement> errors = driver.findElements(fieldErrorMessages);
        return errors.stream().map(e -> e.getText().trim()).filter(s -> !s.isEmpty()).toList();
    }

    public String getToastMessage() {
        try {
            WebElement toast = WaitUtils.waitForVisibility(driver, toastNotification, 8);
            return toast.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isLoginPageLoaded() {
        try {
            return WaitUtils.waitForVisibility(driver, usernameInput, 10).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
