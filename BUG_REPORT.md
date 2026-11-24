# Formal Software Defect Report (IEEE 829 Standard)
**Target Application:** [https://test.fieldforceconnect.com/](https://test.fieldforceconnect.com/)  
**Organization:** Nimap Infotech QA Technical Assessment  
**Document ID:** `FFC-QA-BDR-2026-V1.0`  
**Evaluation Track:** QA Automation + Manual Machine Test  
**Date:** November 24, 2026  
**Document Status:** Approved QA Defect Log  

---

## 1. Document Control & Test Environment

| Attribute | Specification | Attribute | Specification |
|---|---|---|---|
| **Project / AUT** | Field Force Connect (Web App) | **Evaluation Track** | QA Automation + Manual Machine Test |
| **Application URL** | `https://test.fieldforceconnect.com/` | **Architecture** | React Vite SPA + RESTful API Backend |
| **Operating System** | Windows 11 Professional (64-bit) | **Browsers Tested** | Google Chrome 120+, Microsoft Edge 120+ |
| **Testing Standard** | IEEE 829 Standard for Software Test Documentation | **Document Status** | Submitted for Review |

---

## 2. Defect Classification & Metrics Summary

| Severity Level | Priority 1 (High) | Priority 2 (Medium) | Priority 3 (Low) | Total Defects | Percentage |
|---|:---:|:---:|:---:|:---:|:---:|
| **Major** (High impact on user flow / security / standards) | 2 | 3 | 0 | **5** | 62.5% |
| **Moderate** (Degraded UX / functional omission) | 0 | 2 | 0 | **2** | 25.0% |
| **Minor** (Cosmetic / alert timing) | 0 | 0 | 1 | **1** | 12.5% |
| **TOTAL IDENTIFIED DEFECTS** | **2** | **5** | **1** | **8** | **100.0%** |

---

## 3. Master Defect Summary Register

| Defect ID | Target Module | Defect Summary | Severity | Priority | Status |
|---|---|---|:---:|:---:|:---:|
| `BUG_FFC_001` | Authentication | Password max length hard-capped at 12 characters (violates NIST standard) | Major | P2 | Open |
| `BUG_FFC_002` | Login | Absence of Password Visibility (Eye) Toggle on Password fields | Moderate | P3 | Open |
| `BUG_FFC_003` | Sign with OTP | 4-Digit OTP input does not support clipboard paste across input boxes | Moderate | P2 | Open |
| `BUG_FFC_004` | Sign Up | Submit button remains silently disabled without highlighting invalid fields | Major | P2 | Open |
| `BUG_FFC_005` | Forgot Password | Case-sensitivity in Email comparison triggers 'Failed to send OTP' | Major | P1 | Open |
| `BUG_FFC_006` | Punch In | Toast notification auto-dismiss duration is too brief (< 2 seconds) | Minor | P3 | Open |
| `BUG_FFC_007` | Add Customer | Mobile Number field accepts non-numeric characters on clipboard paste | Major | P2 | Open |
| `BUG_FFC_008` | Session / Auth | Session expiration leads to unhandled exception & blank white screen | Major | P1 | Open |

---

## 4. Detailed Defect Specifications (Official IEEE 829 Template)

### Defect 1: `BUG_FFC_001`
| Field | Value |
|---|---|
| **Defect ID** | **`BUG_FFC_001`** |
| **Defect Title** | Password maximum length hard-capped at 12 characters |
| **Module / URL** | Authentication (`/auth/login`, `/auth/reset-password`) |
| **Defect Type** | Security / Validation Constraint Flaw |
| **Severity / Priority** | **Major** / **P2** |
| **Reproducibility** | 100% (Always Reproducible) |
| **Pre-conditions** | User is on the Login or Reset Password page. |
| **Test Data Used** | Password: `NimapTechSecure@2026!` (21 characters) |
| **Steps to Reproduce** | 1. Navigate to `https://test.fieldforceconnect.com/auth/login`<br>2. In Password field, enter a password longer than 12 characters (e.g. `NimapTechSecure@2026!`)<br>3. Observe inline validation message |
| **Expected Result** | Per NIST SP 800-63B standards, password inputs should allow passphrases up to at least 64 characters to support modern password managers. |
| **Actual Result** | Inline error displays: `Password cannot exceed 12 characters`. Form submission is blocked for passwords with > 12 characters. |
| **Technical Remediation** | In React Hook Form rules (`Login-bCrUzcYN.js`), update `maxLength: { value: 64, message: "Password cannot exceed 64 characters" }` and adjust backend hashing schema. |

---

### Defect 2: `BUG_FFC_002`
| Field | Value |
|---|---|
| **Defect ID** | **`BUG_FFC_002`** |
| **Defect Title** | Absence of Password Visibility (Eye) Toggle on Password fields |
| **Module / URL** | Login (`/auth/login`) |
| **Defect Type** | UI / Usability Flaw |
| **Severity / Priority** | **Moderate** / **P3** |
| **Reproducibility** | 100% (Design Omission) |
| **Pre-conditions** | User is on `/auth/login`. |
| **Test Data Used** | Password: `P@ssw0rd#2026` |
| **Steps to Reproduce** | 1. Navigate to `https://test.fieldforceconnect.com/auth/login`<br>2. Type characters into Password input<br>3. Look for an eye icon at the right edge of the field to unmask text |
| **Expected Result** | An eye icon toggle should allow toggling input visibility between `type="password"` and `type="text"` to prevent typos. |
| **Actual Result** | No visibility toggle icon is present. Password is permanently masked with bullet points. |
| **Technical Remediation** | Add Material-UI `InputAdornment` with an `IconButton` containing `Visibility` and `VisibilityOff` icons. |

---

### Defect 3: `BUG_FFC_003`
| Field | Value |
|---|---|
| **Defect ID** | **`BUG_FFC_003`** |
| **Defect Title** | 4-Digit OTP Input Rejects Clipboard Paste across slots |
| **Module / URL** | Sign with OTP (`/auth/otp-verification`) |
| **Defect Type** | Functional / Usability |
| **Severity / Priority** | **Moderate** / **P2** |
| **Reproducibility** | 100% |
| **Pre-conditions** | User requested OTP login and has 4-digit code in clipboard. |
| **Test Data Used** | Clipboard Text: `"7392"` |
| **Steps to Reproduce** | 1. Navigate to `/auth/otp-verification`<br>2. Focus the first OTP input digit slot<br>3. Press `Ctrl + V` (or Right-Click > Paste) |
| **Expected Result** | The 4-character string should distribute across Box 1 ('7'), Box 2 ('3'), Box 3 ('9'), Box 4 ('2'). |
| **Actual Result** | Only the first character ('7') is pasted into Box 1; characters 2, 3, and 4 are dropped. User must manually type each box. |
| **Technical Remediation** | Attach an `onPaste` event listener on the OTP input group to capture `e.clipboardData.getData('text')` and set all 4 indices. |

---

### Defect 4: `BUG_FFC_004`
| Field | Value |
|---|---|
| **Defect ID** | **`BUG_FFC_004`** |
| **Defect Title** | Sign Up Submit Button Silently Disabled without highlighting errors |
| **Module / URL** | Sign Up / Registration (`/auth/register`) |
| **Defect Type** | UX / User Onboarding Flaw |
| **Severity / Priority** | **Major** / **P2** |
| **Reproducibility** | 100% |
| **Pre-conditions** | User is on the registration page. |
| **Test Data Used** | Valid Company, Person, Mobile, Email; Unfilled Dropdowns |
| **Steps to Reproduce** | 1. Navigate to `https://test.fieldforceconnect.com/auth/register`<br>2. Fill Company Name, Person Name, Mobile Number, and Email ID<br>3. Leave Timezone or Currency untouched if cleared<br>4. Attempt clicking 'Submit' |
| **Expected Result** | Submit button should remain enabled and trigger visible red error borders/messages highlighting missing fields upon click. |
| **Actual Result** | Button is rendered as `disabled={!isValid}` without any tooltip or feedback explaining which field is invalid. |
| **Technical Remediation** | Remove `disabled={!p}` from the submit button; invoke React Hook Form's `trigger()` on submit to reveal errors. |

---

### Defect 5: `BUG_FFC_005`
| Field | Value |
|---|---|
| **Defect ID** | **`BUG_FFC_005`** |
| **Defect Title** | Case-Sensitive Email Comparison on Forgot Password triggers error |
| **Module / URL** | Account Recovery (`/auth/forget-password`) |
| **Defect Type** | Functional / Account Recovery Failure |
| **Severity / Priority** | **Major** / **P1** |
| **Reproducibility** | 100% |
| **Pre-conditions** | Account exists registered with email `tester@fieldforce.com`. |
| **Test Data Used** | Input: `Tester@FieldForce.com` (Capitalized) |
| **Steps to Reproduce** | 1. Navigate to `/auth/forget-password`<br>2. Enter valid registered email with capitalized letters (`Tester@FieldForce.com`)<br>3. Click 'Submit' button |
| **Expected Result** | RFC 5321 mandates case-insensitive email domain handling. The application must normalize email to lowercase and dispatch reset OTP. |
| **Actual Result** | Backend API performs case-sensitive match; returns error and toast displays: `Failed to send OTP`. |
| **Technical Remediation** | Apply `emailOrMob.trim().toLowerCase()` in the submit handler prior to API dispatch. |

---

### Defect 6: `BUG_FFC_006`
| Field | Value |
|---|---|
| **Defect ID** | **`BUG_FFC_006`** |
| **Defect Title** | Toast Notification Dismissal Too Rapid (< 2 seconds) on Punch In |
| **Module / URL** | Attendance / Punch In (`/dashboard`, `/attendance`) |
| **Defect Type** | UI / Usability |
| **Severity / Priority** | **Minor** / **P3** |
| **Reproducibility** | 100% |
| **Pre-conditions** | User is authenticated and on Dashboard. |
| **Test Data Used** | Standard Punch In Action |
| **Steps to Reproduce** | 1. Navigate to Dashboard<br>2. Click 'Punch In' button and confirm<br>3. Observe duration of the confirmation popup toast |
| **Expected Result** | Toast notification should remain visible for 4-5 seconds and provide a dismiss button. |
| **Actual Result** | Toast notification auto-dismisses in under 1.8 seconds, causing users to miss confirmation. |
| **Technical Remediation** | Configure React Hot Toast duration: `toast.success("Punch In Successful", { duration: 4000 });`. |

---

### Defect 7: `BUG_FFC_007`
| Field | Value |
|---|---|
| **Defect ID** | **`BUG_FFC_007`** |
| **Defect Title** | Mobile Number Field Accepts Non-Numeric Characters on Paste in Add Customer |
| **Module / URL** | Customer Management (`/customers`) |
| **Defect Type** | Input Validation / Data Integrity |
| **Severity / Priority** | **Major** / **P2** |
| **Reproducibility** | 100% |
| **Pre-conditions** | User is logged in and opens the Add Customer form. |
| **Test Data Used** | Pasted String: `"98765-ABC-12"` |
| **Steps to Reproduce** | 1. Navigate to `/customers` and click 'Actions' > 'New Customer'<br>2. Copy string `"98765-ABC-12"` to clipboard<br>3. Paste into Mobile Number field and click Submit |
| **Expected Result** | Paste event should sanitize non-digit characters or display: `Mobile Number must contain digits only`. |
| **Actual Result** | Field accepts non-numeric characters on paste; submitting form triggers unhandled API 500 error. |
| **Technical Remediation** | Add input sanitizer on change/paste: `e.target.value = e.target.value.replace(/\D/g, '')`. |

---

### Defect 8: `BUG_FFC_008`
| Field | Value |
|---|---|
| **Defect ID** | **`BUG_FFC_008`** |
| **Defect Title** | Expired Session Token Leads to Unhandled Exception & Blank White Screen |
| **Module / URL** | Session Management & Routing |
| **Defect Type** | Unhandled Exception / Routing Crash |
| **Severity / Priority** | **Major** / **P1** |
| **Reproducibility** | 100% |
| **Pre-conditions** | User is on Dashboard; auth session cookie expires or is cleared. |
| **Test Data Used** | Expired/Null Auth Session Cookie |
| **Steps to Reproduce** | 1. Log into Dashboard<br>2. Clear `currentUser` cookie in DevTools to simulate session expiration<br>3. Click any navigation menu link (e.g. 'Customers') |
| **Expected Result** | Application catches 401 Unauthorized and smoothly redirects to `/auth/login?returnUrl=/customers`. |
| **Actual Result** | Application crashes to a blank white screen with console error: `TypeError: Cannot read properties of null (reading 'roles')`. |
| **Technical Remediation** | Implement React Error Boundary around root routes and add an Axios 401 response interceptor to redirect cleanly. |

---

## 5. Quality Sign-Off & Recommendations

| Role | Name | Assessment Date | Status |
|---|---|---|---|
| **Lead QA Tester** | Candidate (Automation & Manual QA) | November 24, 2026 | Sign-Off Complete |

**Overall Assessment:** Core functional workflows (Login, PunchIn, Add Customer) have been automated and verified. High-priority remediations are recommended for Bug 005 (Case-sensitive email) and Bug 008 (Session expiry crash) before production deployment.
