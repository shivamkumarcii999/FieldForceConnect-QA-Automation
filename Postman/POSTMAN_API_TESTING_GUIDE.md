# Postman API Testing Guide - Field Force Connect
**Target Application:** [https://test.fieldforceconnect.com/](https://test.fieldforceconnect.com/)  
**Environment:** FieldForceConnect_Test_Environment  
**Document Version:** 1.0.0  
**Prepared For:** Nimap Machine Test  
**Author:** QA Engineering Team  
**Date:** November 2026  

---

## 1. Overview
This directory contains the complete Postman API automation test suite for **Field Force Connect**, meeting all requirements specified in the Nimap QA Machine Test:
1. **Setting up environments and base URLs:** Defined in `FieldForceConnect.postman_environment.json`.
2. **Creating and using variables for authentication:** Extracting `authToken` dynamically from the Login response and injecting it into subsequent API requests.
3. **Sending requests to test APIs:** Covering both `GET` and `POST` methods.
4. **Execution Scenarios:**
   - Login with valid credentials
   - Login with invalid credentials
   - Add a customer from dashboard page

---

## 2. Included Files

| File | Description |
|---|---|
| [`FieldForceConnect.postman_collection.json`](file:///c:/Users/Admin/OneDrive/Desktop/New%20folder/Postman/FieldForceConnect.postman_collection.json) | The Postman v2.1.0 collection containing all organized test requests and assertion scripts. |
| [`FieldForceConnect.postman_environment.json`](file:///c:/Users/Admin/OneDrive/Desktop/New%20folder/Postman/FieldForceConnect.postman_environment.json) | The Postman environment containing `baseUrl`, credentials, and dynamic token placeholders. |

---

## 3. How to Import and Run in Postman GUI

### Step 1: Import Files
1. Open the **Postman** desktop application.
2. Click the **Import** button in the top-left corner.
3. Drag and drop both files:
   - `FieldForceConnect.postman_collection.json`
   - `FieldForceConnect.postman_environment.json`
4. Confirm the import.

### Step 2: Select Environment
1. In the top-right corner environment selector dropdown, select **FieldForceConnect_Test_Environment**.
2. Verify that `baseUrl` is set to `https://test.fieldforceconnect.com`.

### Step 3: Run the Collection
1. Click on the collection name **Field Force Connect - API Automation Suite**.
2. Click **Run collection** (Collection Runner).
3. Ensure all requests are checked:
   - `1. Login with Valid Credentials` (POST)
   - `2. Login with Invalid Credentials` (POST)
   - `3. GET Country List` (GET)
   - `4. GET Customer Stages / Lead Status` (GET)
   - `5. Add Customer from Dashboard Page` (POST)
4. Click **Run Field Force Connect - API Automation Suite**.
5. Observe all test assertions pass with green checkmarks.

---

## 4. API Request Specifications

### 1. Login with Valid Credentials (POST)
* **Endpoint:** `POST {{baseUrl}}/api/Account/Login`
* **Headers:** `Content-Type: application/json`
* **Body:**
  ```json
  {
    "username": "{{validUsername}}",
    "password": "{{validPassword}}"
  }
  ```
* **Post-response Script (Token Extraction):**
  ```javascript
  pm.test("Status code is 200 OK", function () {
      pm.response.to.have.status(200);
  });
  var jsonData = pm.response.json();
  var token = jsonData.access_token || jsonData.token || jsonData.accessToken;
  if (token) {
      pm.environment.set("authToken", "Bearer " + token);
  } else {
      var basicAuth = "Basic " + btoa(pm.environment.get("validUsername") + ":" + pm.environment.get("validPassword"));
      pm.environment.set("authToken", basicAuth);
  }
  ```

---

### 2. Login with Invalid Credentials (POST)
* **Endpoint:** `POST {{baseUrl}}/api/Account/Login`
* **Headers:** `Content-Type: application/json`
* **Body:**
  ```json
  {
    "username": "{{invalidUsername}}",
    "password": "{{invalidPassword}}"
  }
  ```
* **Assertions:**
  - Verify status code is 400, 401, or 200 with `success: false`.
  - Verify error message payload returned.

---

### 3. Master Data Verification (GET)
* **Endpoints:**
  - `GET {{baseUrl}}/api/Account/GetCountryList`
  - `GET {{baseUrl}}/api/CRM/LeadStatus`
* **Headers:** `Authorization: {{authToken}}`
* **Assertions:**
  - Status code is 200 OK.
  - Returns array of data.

---

### 4. Add Customer from Dashboard Page (POST)
* **Endpoint:** `POST {{baseUrl}}/api/CRM/AddLead`
* **Headers:**
  - `Authorization: {{authToken}}`
  - `Content-Type: application/json`
* **Pre-request Script (Dynamic Data Generator):**
  ```javascript
  var randomSuffix = Math.floor(Math.random() * 90000) + 10000;
  pm.variables.set("dynamicCustomerName", pm.environment.get("newCustomerName") + " - " + randomSuffix);
  pm.variables.set("dynamicMobile", "98" + (Math.floor(Math.random() * 90000000) + 10000000));
  ```
* **Body:**
  ```json
  {
    "LeadName": "{{dynamicCustomerName}}",
    "PersonName": "{{contactPersonName}}",
    "MobileNo": "{{dynamicMobile}}",
    "Email": "{{contactEmail}}",
    "Address": "{{address}}",
    "CountryId": 101,
    "StateId": 1,
    "CityId": 1,
    "LeadSourceId": 1,
    "LeadStageId": 1,
    "LeadTypeId": 1,
    "IsVisibleToAll": true
  }
  ```
* **Assertions:**
  - Status code 200 or 201 Created.
  - Response contains success confirmation message.

---

## 5. Running via Newman CLI (Headless / CI/CD)
To run in CI/CD pipeline or command line:
```bash
newman run Postman/FieldForceConnect.postman_collection.json \
  -e Postman/FieldForceConnect.postman_environment.json \
  --reporters cli,htmlextra
```
