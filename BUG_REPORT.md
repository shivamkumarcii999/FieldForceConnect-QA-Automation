# Formal Defect Report - Field Force Connect
**Target Application:** [https://test.fieldforceconnect.com/](https://test.fieldforceconnect.com/)  
**Document Version:** 1.0.0  
**Prepared For:** Nimap Machine Test  
**Author:** QA Engineering Team  
**Date:** November 2026  
**Status:** Approved for Review  

---

## 1. Executive Defect Summary

During the functional, validation, and exploratory testing of **Field Force Connect** across the core authentication, attendance, and customer management modules, a total of **8 defects** were identified, documented, and classified.

### Defect Distribution by Severity & Priority

| Severity | High Priority (P1) | Medium Priority (P2) | Low Priority (P3) | Total | Percentage |
|---|:---:|:---:|:---:|:---:|:---:|
| **Critical** | 0 | 0 | 0 | **0** | 0% |
| **Major** | 2 | 3 | 0 | **5** | 62.5% |
| **Moderate** | 0 | 2 | 0 | **2** | 25.0% |
| **Minor** | 0 | 0 | 1 | **1** | 12.5% |
| **Total** | **2** | **5** | **1** | **8** | **100%** |

### Summary Matrix

| Bug ID | Module | Defect Title | Severity | Priority | Status |
|---|---|---|:---:|:---:|:---:|
| `BUG_FFC_001` | Authentication | Password maximum length capped at 12 characters restricts secure passwords | Major | P2 | Open |
| `BUG_FFC_002` | Login | Absence of Password Visibility (Eye) Toggle on Password fields | Moderate | P3 | Open |
| `BUG_FFC_003` | Sign with OTP | 4-Digit OTP input does not support clipboard paste across input boxes | Moderate | P2 | Open |
| `BUG_FFC_004` | Sign Up | Form submit button remains silently disabled without highlighting invalid fields | Major | P2 | Open |
| `BUG_FFC_005` | Forgot Password | Case-sensitivity in Email comparison triggers 'Failed to send OTP' | Major | P1 | Open |
| `BUG_FFC_006` | Attendance / Punch In | Toast Notification auto-dismiss duration is too brief (< 2 seconds) | Minor | P3 | Open |
| `BUG_FFC_007` | Add Customer | Mobile Number field allows non-numeric characters on clipboard paste | Major | P2 | Open |
| `BUG_FFC_008` | Session / Navigation | Session token expiration results in white blank screen without redirect | Major | P1 | Open |

---

## 2. Detailed Defect Reports

### Defect 1: `BUG_FFC_001`
* **Defect Title:** Password maximum length is hard-capped at 12 characters, violating modern password standards
* **Module:** Authentication (`/auth/login`, `/auth/reset-password`)
* **Severity:** Major
* **Priority:** P2
* **Environment:** Chrome 120+, Windows 11, Web App
* **Preconditions:** User is on the Login or Reset Password page
* **Steps to Reproduce:**
  1. Navigate to `https://test.fieldforceconnect.com/auth/login`
  2. In the Password input field, enter a strong passphrase longer than 12 characters (e.g., `NimapTechSecure@2026!`)
  3. Observe inline validation message
* **Expected Result:** Application should allow passwords up to at least 64 characters (per NIST SP 800-63B standards) to support secure passphrases and password managers.
* **Actual Result:** Field displays validation error: `Password cannot exceed 12 characters`, blocking secure generated passwords.
* **Impact:** Users with password managers generating 16-32 character passwords cannot authenticate or register.
* **Suggested Fix:** Increase password maximum constraint in React validation schema (`maxLength: 64`) and database schema to 64 or 128 characters.

---

### Defect 2: `BUG_FFC_002`
* **Defect Title:** Absence of Password Visibility Toggle ('Show/Hide Password' eye icon)
* **Module:** Login (`/auth/login`)
* **Severity:** Moderate
* **Priority:** P3
* **Environment:** Cross-Browser (Chrome, Edge, Firefox)
* **Preconditions:** User is on `/auth/login`
* **Steps to Reproduce:**
  1. Navigate to `https://test.fieldforceconnect.com/auth/login`
  2. Type a password in the Password field
  3. Look for an eye icon to preview the entered password
* **Expected Result:** A clickable eye icon toggle should be present at the end of the password input to allow toggling between masked `type="password"` and plain `type="text"`.
* **Actual Result:** No visibility toggle icon is available. Input remains strictly masked with dots.
* **Impact:** Increases login failures due to typos, especially on mobile or touch devices.
* **Suggested Fix:** Add Material-UI `InputAdornment` with `IconButton` and visibility toggle state in `Login-bCrUzcYN.js`.

---

### Defect 3: `BUG_FFC_003`
* **Defect Title:** 4-Digit OTP input does not support pasting full OTP string into the first box
* **Module:** Sign with OTP (`/auth/otp-verification`)
* **Severity:** Moderate
* **Priority:** P2
* **Environment:** Chrome 120+, Windows 11
* **Preconditions:** User has requested an OTP and has copied the 4-digit code (`"4821"`) to clipboard
* **Steps to Reproduce:**
  1. Navigate to `/auth/otp-verification`
  2. Right-click or press `Ctrl+V` on the first OTP digit input box
  3. Observe input distribution across the 4 boxes
* **Expected Result:** The clipboard string `"4821"` should automatically populate Box 1=`4`, Box 2=`8`, Box 3=`2`, Box 4=`1`, and focus shifts to the submit button.
* **Actual Result:** Only the first digit `'4'` is pasted into Box 1; remaining digits are discarded.
* **Impact:** Degraded user experience, forcing users to manually copy/type each character.
* **Suggested Fix:** Attach `onPaste` event listener to split clipboard text and distribute to index `0..3`.

---

### Defect 4: `BUG_FFC_004`
* **Defect Title:** Sign Up Submit button remains silently disabled when optional-looking fields are unfulfilled
* **Module:** Sign Up (`/auth/register`)
* **Severity:** Major
* **Priority:** P2
* **Environment:** Chrome 120+, Windows 11
* **Preconditions:** User is on `/auth/register`
* **Steps to Reproduce:**
  1. Navigate to `https://test.fieldforceconnect.com/auth/register`
  2. Enter Company Name, Contact Person Name, Mobile Number, and Email ID
  3. Leave Timezone or Currency untouched if not pre-selected
  4. Attempt to click 'Submit' button
* **Expected Result:** Submit button should either be enabled and highlight invalid fields with red border on click, or show a clear tooltip explaining why it is disabled.
* **Actual Result:** Submit button is disabled (`disabled={!isValid}`) without feedback, leaving users unsure why form cannot be submitted.
* **Impact:** Causes drop-offs during onboarding.
* **Suggested Fix:** Keep button clickable and invoke React Hook Form `trigger()` on click to show error highlights under unfilled fields.

---

### Defect 5: `BUG_FFC_005`
* **Defect Title:** Case-sensitive Email comparison triggers 'Failed to send OTP' on Forgot Password
* **Module:** Forgot Password (`/auth/forget-password`)
* **Severity:** Major
* **Priority:** P1
* **Environment:** Chrome 120+, Windows 11
* **Preconditions:** User is registered with email `tester@fieldforce.com`
* **Steps to Reproduce:**
  1. Navigate to `/auth/forget-password`
  2. In the input box, enter `Tester@fieldforce.com` (with capitalized 'T')
  3. Click 'Submit'
* **Expected Result:** System normalizes email to lowercase (`.toLowerCase()`) and sends password reset OTP.
* **Actual Result:** API returns error and toast displays `Failed to send OTP`.
* **Impact:** Users who have auto-capitalization on their phone keyboards or enter mixed case emails cannot reset passwords.
* **Suggested Fix:** Normalize `emailOrMob.trim().toLowerCase()` prior to API dispatch.

---

### Defect 6: `BUG_FFC_006`
* **Defect Title:** Punch In Toast Notification auto-dismiss duration is too brief (< 2 seconds)
* **Module:** Attendance / Punch In (`/attendance`, `/dashboard`)
* **Severity:** Minor
* **Priority:** P3
* **Environment:** Chrome 120+, Windows 11
* **Preconditions:** User is logged in and punches in attendance
* **Steps to Reproduce:**
  1. Click 'Punch In' button
  2. Observe the success toast popup alert
* **Expected Result:** Toast alert should persist for 4-5 seconds and provide a close 'X' button or progress bar.
* **Actual Result:** Toast disappears in under 2 seconds, making it easy for users on slow connections to miss confirmation.
* **Impact:** Users are unsure if punch-in succeeded and may repeatedly click the action.
* **Suggested Fix:** Increase `react-hot-toast` or toast duration setting to `4000ms`.

---

### Defect 7: `BUG_FFC_007`
* **Defect Title:** Add Customer Mobile Number field allows non-numeric characters on clipboard paste
* **Module:** Customer Management (`/customers`)
* **Severity:** Major
* **Priority:** P2
* **Environment:** Chrome 120+, Windows 11
* **Preconditions:** User is logged in and opens the Add Customer drawer
* **Steps to Reproduce:**
  1. Navigate to `/customers`
  2. Click 'Actions' -> 'New Customer'
  3. Copy string `"98765-ABC-12"` to clipboard
  4. Paste into the Mobile Number input field
  5. Click 'Submit'
* **Expected Result:** The paste event should sanitize non-numeric characters or display an inline error `Mobile Number must contain digits only`.
* **Actual Result:** Non-numeric characters remain in the input field until form submission fails with a generic server error.
* **Impact:** Corrupted phone data submitted to backend leads to unhandled API 500 errors.
* **Suggested Fix:** Add regex input sanitizer `value.replace(/\D/g, '')` on paste/change handler.

---

### Defect 8: `BUG_FFC_008`
* **Defect Title:** Expired session token leads to blank white screen instead of redirecting to Login
* **Module:** Session Management & Routing
* **Severity:** Major
* **Priority:** P1
* **Environment:** Chrome 120+, Windows 11
* **Preconditions:** User has an active tab on Dashboard, and auth cookie expires or is cleared
* **Steps to Reproduce:**
  1. Log into Dashboard
  2. Clear `currentUser` cookie in DevTools or wait for token expiry
  3. Click any navigation link (e.g., 'Customers' or 'Attendance')
* **Expected Result:** React Router Auth Guard catches HTTP 401 and smoothly navigates user to `/auth/login?returnUrl=/customers`.
* **Actual Result:** Application crashes to an unhandled exception state showing a blank white screen with console error `TypeError: Cannot read properties of null (reading 'roles')`.
* **Impact:** User is forced to manually clear cache or hard-refresh the URL to regain access.
* **Suggested Fix:** Wrap Root component with React Error Boundary and handle 401 response interceptor in Axios instance.

---

## 3. General Quality Recommendations
1. **Adopt NIST SP 800-63B Guidelines:** Remove the 12-character password upper limit and implement passphrase support.
2. **Standardize Input Sanitation:** Normalize all email inputs with `.trim().toLowerCase()` consistently on both client and server layers.
3. **Enhance User Feedback:** Ensure form submit buttons are either accompanied by field error tooltips or keep buttons clickable and trigger instant validation highlighting.
4. **Resilience & Error Boundaries:** Implement global React Error Boundaries to prevent blank screen crashes on expired auth tokens.
