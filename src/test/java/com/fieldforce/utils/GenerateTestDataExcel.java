package com.fieldforce.utils;

/**
 * Runner to generate sample Excel test data for data-driven testing.
 */
public class GenerateTestDataExcel {
    public static void main(String[] args) {
        String loginExcelPath = "src/test/resources/testdata/LoginTestData.xlsx";
        String[] loginHeaders = {"Username", "Password", "ScenarioType", "ExpectedValidationMsg"};
        Object[][] loginRows = {
                {"admin@fieldforce.com", "Nimap@2026", "VALID", "SUCCESS"},
                {"admin@fieldforce.com", "WrongPassword123", "INVALID_PASSWORD", "Login failed"},
                {"unregistered.user@nimap.com", "Nimap@2026", "UNREGISTERED", "Login failed"},
                {"invalid_email_format", "Nimap@2026", "INVALID_FORMAT", "Enter a valid email or 6-15 digit mobile number"},
                {"", "", "BLANK", "Email or mobile number is required"}
        };
        ExcelUtil.createSampleExcel(loginExcelPath, "LoginData", loginHeaders, loginRows);

        String customerExcelPath = "src/test/resources/testdata/CustomerTestData.xlsx";
        String[] customerHeaders = {"CustomerName", "ContactPerson", "Mobile", "Email", "Address"};
        Object[][] customerRows = {
                {"Nimap Solutions 101", "Rajesh Kulkarni", "9820123456", "rajesh.k@nimap.com", "Andheri East, Mumbai"},
                {"Apex Global Corp 102", "Anita Sharma", "9833445566", "anita.s@apex.com", "Hinjewadi Phase 1, Pune"}
        };
        ExcelUtil.createSampleExcel(customerExcelPath, "CustomerData", customerHeaders, customerRows);

        System.out.println("Excel test data files generated successfully.");
    }
}
