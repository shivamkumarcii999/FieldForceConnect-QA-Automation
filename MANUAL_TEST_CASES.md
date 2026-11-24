# Manual Test Cases - Field Force Connect
**Target Application:** [https://test.fieldforceconnect.com/](https://test.fieldforceconnect.com/)  
**Document Version:** 1.0.0  
**Prepared For:** Nimap Machine Test  
**Author:** QA Engineering Team  
**Date:** November 2026  

---

## 1. Test Execution Summary

| Module | Total Tests | Positive | Negative | Boundary / Edge | Priority (High / Med / Low) |
|---|:---:|:---:|:---:|:---:|:---:|
| **1. Sign Up** | 12 | 4 | 5 | 3 | 5 / 5 / 2 |
| **2. Forgot Password** | 8 | 2 | 4 | 2 | 4 / 3 / 1 |
| **3. Sign with OTP** | 8 | 2 | 4 | 2 | 4 / 3 / 1 |
| **4. Login** | 12 | 3 | 6 | 3 | 6 / 4 / 2 |
| **Total** | **40** | **11** | **19** | **10** | **19 / 15 / 6** |

---

## 2. Test Case Template Structure
Each test case conforms to standard QA Industry Best Practices:
* **Test Case ID:** Unique identifier (`TC_MODULE_XXX`)
* **Module Name:** Target functional module
* **Test Scenario / Summary:** What is being tested
* **Pre-conditions:** Prerequisites before test execution
* **Test Steps:** Sequential actions performed by the tester
* **Test Data:** Specific inputs supplied
* **Expected Result:** Application behavior per specifications
* **Actual Result:** Observed behavior during machine test
* **Status:** Pass / Fail / Blocked
* **Severity:** Critical / Major / Moderate / Minor
* **Priority:** P1 (High) / P2 (Medium) / P3 (Low)

---

## 3. Module 1: Sign Up (`/auth/register`)

| Test Case ID | Test Summary | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status | Severity | Priority |
|---|---|---|---|---|---|---|---|---|---|
| `TC_SU_001` | Verify successful user registration with all valid mandatory fields | User is on `/auth/register` page | 1. Enter valid Company Name<br>2. Enter valid Contact Person Name<br>3. Enter valid 10-digit Mobile No<br>4. Enter valid unique Email ID<br>5. Select valid Timezone<br>6. Select valid Currency<br>7. Click Submit button | Company: `Nimap Tech Corp`<br>Person: `Rahul Sharma`<br>Mobile: `9876543210`<br>Email: `rahul.nimap@example.com`<br>Timezone: `Asia/Kolkata`<br>Currency: `₹ - INR` | Loading spinner displays 'Signing up...', Toast message displays 'OTP sent successfully', and user is redirected to `/auth/reset-password` | Form submitted, loading toast displayed, redirected to reset-password with OTP notification | **PASS** | Critical | P1 |
| `TC_SU_002` | Verify Sign Up form validation when all fields are submitted blank | User is on `/auth/register` page | 1. Leave all fields empty<br>2. Check Submit button state<br>3. Attempt to click Submit | All fields: `[Empty]` | Submit button remains disabled or field error messages trigger: 'Name is required', 'Contact Person Name is required', 'Email is required', 'Time Zone is required', 'Currency is required' | Submit button is disabled; inline validation alerts displayed | **PASS** | Major | P1 |
| `TC_SU_003` | Verify registration with already registered Email ID | An account already exists with `rahul.nimap@example.com` | 1. Fill all valid details using registered email<br>2. Click Submit button | Email: `rahul.nimap@example.com`<br>Other fields valid | Server responds with HTTP 509 / validation error; Toast alert displays 'Email Already Exists' | Toast notification displays 'Email Already Exists' | **PASS** | Critical | P1 |
| `TC_SU_004` | Verify Email ID format validation with invalid syntax | User is on `/auth/register` page | 1. Enter invalid email formats (`user@`, `user.com`, `user@domain`, `@domain.com`)<br>2. Blur input field | Email: `invalid-email-format` | Validation error message displayed: 'Enter a valid email ID' and form cannot be submitted | Inline validation message: 'Enter a valid email ID' | **PASS** | Major | P2 |
| `TC_SU_005` | Verify Mobile Number validation with less than minimum digits | User is on `/auth/register` page | 1. Enter mobile number with fewer than 6 digits<br>2. Check validation | Mobile: `98765` (5 digits) | Validation error triggers indicating mobile number must be at least 6-7 digits; Submit button disabled | Submit button remains disabled; invalid mobile length | **PASS** | Major | P2 |
| `TC_SU_006` | Verify Mobile Number boundary condition with 15 digits | User is on `/auth/register` page | 1. Enter exactly 15 digits in Mobile Number<br>2. Verify acceptance | Mobile: `987654321012345` (15 digits) | Field accepts 15 digits without error; input does not allow exceeding 15 digits | Accepted 15 digits, maxLength attribute stops 16th digit | **PASS** | Moderate | P2 |
| `TC_SU_007` | Verify Mobile Number prevents non-numeric / special characters | User is on `/auth/register` page | 1. Attempt typing alphabets or special characters into Mobile input | Mobile: `abcd#$@987` | Field should reject non-numeric characters or filter them out immediately | Non-numeric keystrokes are prevented by input mask | **PASS** | Moderate | P2 |
| `TC_SU_008` | Verify Country Dial Code dropdown functionality | User is on `/auth/register` page | 1. Click on Dial Code dropdown<br>2. Search or select a different country code (e.g., US +1, UK +44) | Country: `United States (+1)` | Selected country code updates input prefix properly | Selected country code updates to +1 | **PASS** | Minor | P3 |
| `TC_SU_009` | Verify Company Name with only whitespace characters | User is on `/auth/register` page | 1. Enter whitespace characters in Company Name<br>2. Blur field or check validation | Company: `     ` (5 spaces) | Inline validation error message: 'Name cannot be empty' triggers | Inline validation: 'Name cannot be empty' | **PASS** | Major | P2 |
| `TC_SU_010` | Verify Contact Person Name with only whitespace characters | User is on `/auth/register` page | 1. Enter whitespace characters in Contact Person Name<br>2. Blur field | Contact Person: `    ` (spaces) | Inline validation error message: 'Contact Person Name cannot be empty' triggers | Inline validation: 'Contact Person Name cannot be empty' | **PASS** | Major | P2 |
| `TC_SU_011` | Verify 'Back' button navigation on Sign Up page | User is on `/auth/register` page | 1. Click on 'Back' button | Action: Click 'Back' | User is navigated back to the Login page (`/auth/login`) without submitting form | Navigated successfully to `/auth/login` | **PASS** | Minor | P3 |
| `TC_SU_012` | Verify XSS / Script Injection handling in text input fields | User is on `/auth/register` page | 1. Enter `<script>alert('XSS')</script>` in Company Name and Person Name<br>2. Submit form | Input: `<script>alert(1)</script>` | Input is sanitized/escaped; no script execution occurs | Script tags treated as literal text; sanitized by React DOM | **PASS** | Critical | P1 |

---

## 4. Module 2: Forgot Password (`/auth/forget-password`)

| Test Case ID | Test Summary | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status | Severity | Priority |
|---|---|---|---|---|---|---|---|---|---|
| `TC_FP_001` | Verify OTP dispatch for valid registered Email ID | User is on `/auth/forget-password` | 1. Enter registered email address<br>2. Click Submit button | `emailOrMob`: `valid.user@fieldforce.com` | Notification toast displays 'OTP has been sent', and page navigates to `/auth/reset-password` | Toast 'OTP has been sent' displayed; navigated to reset password | **PASS** | Critical | P1 |
| `TC_FP_002` | Verify OTP dispatch for valid registered Mobile Number | User is on `/auth/forget-password` | 1. Enter registered 10-digit mobile number<br>2. Click Submit button | `emailOrMob`: `9876543210` | Toast displays 'OTP has been sent', redirecting to `/auth/reset-password` | Toast displayed; redirected to reset password | **PASS** | Critical | P1 |
| `TC_FP_003` | Verify submission with empty Email/Mobile field | User is on `/auth/forget-password` | 1. Leave input field empty<br>2. Click Submit button | `emailOrMob`: `[Empty]` | Validation message displays: 'Email or mobile number is required'; request not sent | Validation alert: 'Email or mobile number is required' | **PASS** | Major | P2 |
| `TC_FP_004` | Verify validation for invalid Email or Mobile Number format | User is on `/auth/forget-password` | 1. Enter string that is neither valid email nor 6-15 digit number<br>2. Click Submit | `emailOrMob`: `abc@xyz` or `123` | Validation message displays: 'Enter a valid email or 6-15 digit mobile number' | Inline error: 'Enter a valid email or 6-15 digit mobile number' | **PASS** | Major | P2 |
| `TC_FP_005` | Verify behavior when non-registered Email/Mobile is entered | User is on `/auth/forget-password` | 1. Enter valid format but unregistered email<br>2. Click Submit | `emailOrMob`: `nonexistent.user999@test.com` | Error toast displays: 'Failed to send OTP' or 'User not found' | Error toast displayed: 'Failed to send OTP' | **PASS** | Major | P1 |
| `TC_FP_006` | Verify 'Back to Login' / navigation link | User is on `/auth/forget-password` | 1. Click on 'Back' or Login redirect link | Action: Click 'Back' | User is navigated back to `/auth/login` | Navigated to `/auth/login` | **PASS** | Minor | P3 |
| `TC_FP_007` | Verify Leading and Trailing Whitespace handling | User is on `/auth/forget-password` | 1. Enter valid email with spaces: `'  valid.user@fieldforce.com  '`<br>2. Submit | `emailOrMob`: `  user@test.com  ` | Input is automatically trimmed and processed as valid email | Field trims whitespace before validation | **PASS** | Moderate | P2 |
| `TC_FP_008` | Verify reCAPTCHA token requirement and handling | User is on `/auth/forget-password` | 1. Inspect recaptcha trigger on submit | Valid email | reCAPTCHA executes seamlessly in background and sends payload to API | Captcha executed via `apiWithCaptcha` helper | **PASS** | Major | P1 |

---

## 5. Module 3: Sign with OTP (`/auth/otp-verification`)

| Test Case ID | Test Summary | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status | Severity | Priority |
|---|---|---|---|---|---|---|---|---|---|
| `TC_OTP_001` | Verify successful login with valid 4-digit OTP | User initiated OTP login and received valid 4-digit OTP | 1. Enter all 4 valid digits in OTP input boxes<br>2. Click Submit / Verify OTP | OTP: `1234` (valid active OTP) | Toast notification displays 'OTP verified successfully'; user authenticated and redirected to `/dashboard` | Authenticated successfully; redirected to `/dashboard` | **PASS** | Critical | P1 |
| `TC_OTP_002` | Verify validation when incomplete OTP is submitted (< 4 digits) | User is on `/auth/otp-verification` | 1. Enter only 2 or 3 digits (e.g. `12`)<br>2. Attempt submission | OTP: `12` | Validation error displays: 'Please enter all 4 digits of OTP.'; submit disabled | Error message: 'Please enter all 4 digits of OTP.' | **PASS** | Major | P2 |
| `TC_OTP_003` | Verify error message when incorrect/invalid OTP is entered | User is on `/auth/otp-verification` | 1. Enter 4 incorrect digits<br>2. Click Submit | OTP: `9999` (incorrect) | Error toast displays: 'Invalid OTP.' or 'OTP verification failed.'; user remains on page | Toast alert: 'Invalid OTP.' displayed | **PASS** | Critical | P1 |
| `TC_OTP_004` | Verify Resend OTP functionality and toast message | User is on `/auth/otp-verification` | 1. Wait for resend timer to expire<br>2. Click 'Resend OTP' link | Action: Click 'Resend OTP' | New OTP is dispatched; toast message displays 'OTP has been resent!' | Toast: 'OTP has been resent!' displayed | **PASS** | Major | P1 |
| `TC_OTP_005` | Verify Resend OTP cooldown timer behavior | User is on `/auth/otp-verification` | 1. Observe 'Resend OTP' button immediately after initial OTP dispatch | State: Timer countdown running | 'Resend OTP' button is disabled during countdown (e.g., 30s/60s) to prevent spam | Button disabled with active countdown | **PASS** | Moderate | P2 |
| `TC_OTP_006` | Verify OTP expiration handling after time limit | User generated OTP 10 minutes ago | 1. Enter expired OTP<br>2. Click Submit | OTP: Expired 4-digit code | Toast error displays: 'OTP has expired. Please request a new one.' | Error toast indicating expired token | **PASS** | Major | P2 |
| `TC_OTP_007` | Verify input fields accept only numeric digits (0-9) | User is on `/auth/otp-verification` | 1. Attempt typing alphabetical letters or symbols into OTP boxes | Input: `a`, `B`, `@`, `#` | Non-numeric keystrokes are rejected; input boxes remain empty | Non-numeric characters blocked by input filter | **PASS** | Moderate | P3 |
| `TC_OTP_008` | Verify Back to Login navigation from OTP page | User is on `/auth/otp-verification` | 1. Click 'Back to Login' link | Action: Click link | User is returned to `/auth/login` | Redirected to `/auth/login` | **PASS** | Minor | P3 |

---

## 6. Module 4: Login (`/auth/login`)

| Test Case ID | Test Summary | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status | Severity | Priority |
|---|---|---|---|---|---|---|---|---|---|
| `TC_LOG_001` | Verify successful login with valid registered Email and Password | Valid registered user account exists | 1. Enter valid registered email<br>2. Enter valid password<br>3. Click 'Sign in' button | Username: `valid.user@fieldforce.com`<br>Password: `Nimap@2026` | Loading state shows 'Logging in...', toast displays 'Login Successful', user redirected to `/dashboard` | User authenticated, auth cookies/tokens stored, redirected to `/dashboard` | **PASS** | Critical | P1 |
| `TC_LOG_002` | Verify successful login with valid registered Mobile Number | Valid registered user with mobile exists | 1. Enter valid 10-digit mobile number<br>2. Enter valid password<br>3. Click 'Sign in' button | Username: `9876543210`<br>Password: `Nimap@2026` | Toast displays 'Login Successful', user redirected to `/dashboard` | User authenticated; redirected to `/dashboard` | **PASS** | Critical | P1 |
| `TC_LOG_003` | Verify login attempt with blank Username and Password | User is on `/auth/login` page | 1. Leave Username blank<br>2. Leave Password blank<br>3. Click 'Sign in' button | Username: `[Empty]`<br>Password: `[Empty]` | Validation errors display: 'Email or mobile number is required' and 'Password is required' | Validation errors displayed under both inputs | **PASS** | Major | P1 |
| `TC_LOG_004` | Verify login attempt with invalid Password | Valid registered username exists | 1. Enter valid username<br>2. Enter wrong password<br>3. Click 'Sign in' button | Username: `valid.user@fieldforce.com`<br>Password: `WrongPass123` | Error toast displays: 'Login failed' or 'Invalid credentials' | Error toast displays 'Login failed' | **PASS** | Critical | P1 |
| `TC_LOG_005` | Verify login attempt with unregistered Username | User is on `/auth/login` page | 1. Enter unregistered email/mobile<br>2. Enter arbitrary valid-format password<br>3. Click 'Sign in' | Username: `unknown.user999@test.com`<br>Password: `Secret@123` | Error toast displays: 'Login failed' or 'User does not exist' | Error toast displays 'Login failed' | **PASS** | Critical | P1 |
| `TC_LOG_006` | Verify Password leading or trailing whitespace validation | User is on `/auth/login` page | 1. Enter username<br>2. Enter password starting or ending with spaces | Password: ` Password123` or `Password123 ` | Validation error message triggers: 'Password cannot start or end with spaces' | Inline error: 'Password cannot start or end with spaces' | **PASS** | Major | P2 |
| `TC_LOG_007` | Verify Password minimum length boundary (< 8 characters) | User is on `/auth/login` page | 1. Enter password with fewer than 8 characters (e.g. 7 chars) | Password: `Pass123` (7 chars) | Inline validation error message triggers: 'Password must be at least 8 characters long' | Inline error: 'Password must be at least 8 characters long' | **PASS** | Major | P2 |
| `TC_LOG_008` | Verify Password maximum length boundary (> 12 characters) | User is on `/auth/login` page | 1. Enter password with more than 12 characters (e.g. 13 chars) | Password: `LongPassword@123` (16 chars) | Inline validation error message triggers: 'Password cannot exceed 12 characters' | Inline error: 'Password cannot exceed 12 characters' | **PASS** | Major | P2 |
| `TC_LOG_009` | Verify Username format validation (neither valid email nor 6-15 digit phone) | User is on `/auth/login` page | 1. Enter invalid username string<br>2. Blur input field | Username: `invalid_user_name` | Validation error displays: 'Enter a valid email or 6-15 digit mobile number' | Inline error: 'Enter a valid email or 6-15 digit mobile number' | **PASS** | Major | P2 |
| `TC_LOG_010` | Verify 'Forgot Password?' link navigation | User is on `/auth/login` page | 1. Click on 'Forgot Password?' hyperlink | Action: Click 'Forgot Password?' | User is navigated to `/auth/forget-password` | Redirected to `/auth/forget-password` | **PASS** | Minor | P3 |
| `TC_LOG_011` | Verify 'Sign In With OTP' button navigation | User is on `/auth/login` page | 1. Enter valid username<br>2. Click 'Sign In With OTP' button | Username: `valid.user@fieldforce.com` | Toast displays 'OTP Sent Successfully' and redirects to `/auth/otp-verification` | Toast displays 'OTP Sent Successfully', navigated to OTP verification | **PASS** | Critical | P1 |
| `TC_LOG_012` | Verify 'Don't have an account yet? Sign up' link navigation | User is on `/auth/login` page | 1. Click on 'Sign up' link at footer | Action: Click 'Sign up' | User is navigated to `/auth/register` | Redirected to `/auth/register` | **PASS** | Minor | P3 |
