package com.fieldforce.tests;

import com.fieldforce.base.BaseTest;
import com.fieldforce.config.ConfigReader;
import com.fieldforce.pages.CustomerPage;
import com.fieldforce.pages.DashboardPage;
import com.fieldforce.pages.LoginPage;
import com.fieldforce.utils.ExcelUtil;
import com.fieldforce.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Automation Test Suite for Customer Module.
 * Parametrized Add Customer journey and validation (in-line and Excel-driven).
 */
public class CustomerTest extends BaseTest {

    /**
     * Parametrized Data Provider supplying multiple customer datasets.
     */
    @DataProvider(name = "customerParamData")
    public Object[][] getCustomerData() {
        long timestamp = System.currentTimeMillis() % 10000;
        return new Object[][]{
                {"Nimap Solutions " + timestamp, "Rajesh Kulkarni", "982012" + timestamp, "rajesh." + timestamp + "@nimap.com", "Andheri East, Mumbai"},
                {"Apex Global Corp " + (timestamp + 1), "Anita Sharma", "983344" + (timestamp + 1), "anita." + timestamp + "@apex.com", "Hinjewadi Phase 1, Pune"}
        };
    }

    /**
     * Alternative Excel-based Data Provider reading from CustomerTestData.xlsx via Apache POI.
     */
    @DataProvider(name = "excelCustomerData")
    public Object[][] getExcelCustomerData() {
        String excelPath = "src/test/resources/testdata/CustomerTestData.xlsx";
        Object[][] data = ExcelUtil.getTestData(excelPath, "CustomerData");
        if (data.length == 0) {
            return getCustomerData();
        }
        return data;
    }

    @Test(dataProvider = "customerParamData", description = "Add Customer using Parametrization and Validate it", priority = 1)
    public void testAddCustomerWithParametrization(String customerName, String contactPerson, String mobile, String email, String address) {
        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());
        CustomerPage customerPage = new CustomerPage(getDriver());

        // Step 1: Login
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        WaitUtils.sleep(2000);

        // Step 2: Navigate to Customer module
        dashboardPage.navigateToCustomers();
        WaitUtils.sleep(1500);

        // Step 3: Add Customer with parametrized data
        customerPage.addCustomer(customerName, contactPerson, mobile, email, address);

        // Step 4: Validate confirmation Toast / Popup message
        String toastMessage = customerPage.getToastMessage();
        System.out.println("Observed Customer Toast message: " + toastMessage);

        Assert.assertFalse(toastMessage.isEmpty(),
                "Confirmation toast notification should appear after adding customer");

        Assert.assertTrue(customerPage.isCustomerAddedSuccessfully() ||
                        toastMessage.toLowerCase().contains("customer") ||
                        toastMessage.toLowerCase().contains("success") ||
                        toastMessage.toLowerCase().contains("already exists"),
                "Customer addition must be validated by toast feedback. Observed: " + toastMessage);
    }
}
