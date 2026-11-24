package com.fieldforce.base;

import com.fieldforce.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;

/**
 * Base Test class initializing WebDriver, browser configuration, and lifecycle management.
 */
public class BaseTest {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }

    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("") String browserParam, @Optional("") String headlessParam) {
        String browser = (browserParam != null && !browserParam.isEmpty()) ? browserParam : ConfigReader.getBrowser();
        boolean headless = (headlessParam != null && !headlessParam.isEmpty()) ? Boolean.parseBoolean(headlessParam) : ConfigReader.isHeadless();

        WebDriver localDriver;

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                localDriver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                localDriver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                localDriver = new ChromeDriver(chromeOptions);
                break;
        }

        localDriver.manage().window().maximize();
        localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        localDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        setDriver(localDriver);

        // Navigate to Application URL
        getDriver().get(ConfigReader.getUrl());
    }

    @AfterMethod
    public void tearDown() {
        WebDriver currentDriver = getDriver();
        if (currentDriver != null) {
            try {
                currentDriver.quit();
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }

    public static String captureScreenshotBase64() {
        WebDriver currentDriver = getDriver();
        if (currentDriver instanceof TakesScreenshot) {
            return ((TakesScreenshot) currentDriver).getScreenshotAs(OutputType.BASE64);
        }
        return null;
    }
}
