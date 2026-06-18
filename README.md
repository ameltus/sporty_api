# API Automation Framework

Automated API testing framework for the [Zippopotam.us](http://api.zippopotam.us) public API, built with Java, RestAssured, and TestNG.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming Language |
| TestNG 7.12.0 | Test Runner & Assertions |
| RestAssured 5.5.6 | API Testing Library |
| Jackson 2.17.2 | JSON POJO Deserialization |
| Apache POI 5.3.0 | Excel-based Test Data |
| Allure 2.29.0 | Interactive HTML Test Reporting |
| Maven | Build & Dependency Management |

## Project Structure

```
api_automation/
├── pom.xml
├── testng.xml
├── README.md
├── src/
│   ├── main/java/sporty/api_automation/
│   │   ├── endpoints/
│   │   │   └── Routes.java              # API base URL and endpoint definitions
│   │   ├── models/
│   │   │   ├── LocationResponse.java    # POJO for API response (top-level)
│   │   │   └── Place.java              # POJO for place details (nested)
│   │   ├── resources/
│   │   │   ├── location-schema.json     # JSON Schema for schema validation
│   │   │   └── testdata.xlsx            # Excel-driven test data
│   │   └── util/
│   │       └── DataReader.java          # Utility to read test data from Excel
│   └── test/java/sporty/api_automation/tests/
│       ├── TestCases.java               # All test cases
│       ├── Listeners.java               # TestNG listener for logging & retry
│       └── Retry.java                   # Retry analyzer for flaky tests
```

## API Under Test

**Zippopotam.us** — A free public API for looking up postal code information.

- **Base URL**: `http://api.zippopotam.us`
- **Endpoint**: `/{country}/{postal-code}`
- **Source**: [public-apis/public-apis](https://github.com/public-apis/public-apis)

## Test Cases

| # | Test Method | Description | DataProvider | Test Data Source |
|---|---|---|---|---|
| 1 | `testValidZipCodes` | Verify valid country + zip code combinations return HTTP 200, correct JSON content-type, and accurate location data via POJO deserialization | `validZipCodes` (3 rows) | `testdata.xlsx` |
| 2 | `testInvalidZipCodes` | Verify invalid country codes or non-existent zip codes return HTTP 404 | `invalidZipCodes` (3 rows) | `testdata.xlsx` |
| 3 | `testResponseSchema` | Verify the JSON response structure matches the predefined JSON Schema | — | — |
| 4 | `testResponseTime` | Verify the API responds within 3000ms (SLA threshold) | — | — |
| 5 | `testPostMethod` | Verify that calling the API with the POST method is correctly rejected with HTTP 405 Method Not Allowed | — | — |
| 6 | `testPutMethod` | Verify that calling the API with the PUT method is correctly rejected with HTTP 405 Method Not Allowed | — | — |
| 7 | `testPatchMethod` | Verify that calling the API with the PATCH method is correctly rejected with HTTP 405 Method Not Allowed | — | — |
| 8 | `testDeleteMethod` | Verify that calling the API with the DELETE method is correctly rejected with HTTP 405 Method Not Allowed | — | — |

## Validations Used and Why

| Validation | Used In | Why |
|---|---|---|
| **Status Code (200/404/405)** | Test 1, 2, 3, 4, 5, 6, 7, 8 | Fundamental check — ensures the API returns correct HTTP status codes for valid requests (200), invalid lookups (404), and unsupported HTTP methods (405) |
| **Content-Type Header** | Test 1 | Confirms the API returns `application/json`, ensuring the response is parseable |
| **POJO Deserialization** | Test 1 | Deserializes JSON into Java objects (`LocationResponse`, `Place`) using Jackson — validates that the response data maps correctly to typed fields, catching structural changes |
| **Value Assertions** | Test 1 | Verifies specific field values (country name, city name) match expected data — catches data-level regressions |
| **JSON Schema Validation** | Test 3 | Validates the entire response structure (field names, types, required fields) against a JSON Schema — catches breaking API contract changes |
| **Response Time** | Test 4 | Ensures the API meets a performance SLA (< 3 seconds) — catches performance degradations |

## Key Framework Features

- **Data-Driven Testing**: Test data externalized in Excel (`testdata.xlsx`), read via Apache POI — allows adding new test scenarios without code changes
- **DataProvider Parameterization**: TestNG `@DataProvider` reduces code while maintaining high coverage (12 test executions from 8 test methods)
- **POJO Mapping**: Jackson-based deserialization provides type-safe response validation
- **Interactive Allure Reports**: Complete reporting dashboard showing test metrics, categories, severity, behavior hierarchies, and automated inline RestAssured request/response logging
- **Retry Mechanism**: Automatic retry of failed tests (1 retry) via `IRetryAnalyzer` to reduce flakiness
- **Listeners**: Custom `ITestListener` for logging test events and automatically configuring Allure reporting filters
- **Scalable Structure**: Separate packages for endpoints, models, resources, and utilities

## How to Run

### Using IDE (Recommended)
1. Import the project as a Maven project in your IDE
2. Right-click `testng.xml` → **Run As** → **TestNG Suite**

### Using Maven CLI
```bash
mvn clean test
```

## Allure Reporting

The framework integrates **Allure Report** to generate interactive, professional HTML test reports.

### Key Features of Allure Report
- **Global API Logging**: Full HTTP requests and responses (URL, Method, Headers, Cookies, payloads) are automatically logged and attached under each test execution step (powered by `allure-rest-assured`).
- **Test Metadata**: Displays `@Epic`, `@Feature`, `@Story`, `@Severity`, and `@Description` for each test case.
- **Detailed Execution Breakdown**: Shows exact step results, assertions, retry history, and exception stack traces for failures.

### Generating and Serving the Report
Allure reports can be generated and viewed using:

#### Option A: Using Terminal (CLI)
We have configured the `allure-maven` plugin in `pom.xml`. Run the following command:
```bash
mvn allure:serve
```
*Note: This automatically downloads Allure binaries, builds the report from `allure-results`, and opens it in your default web browser.*

#### Option B: Using Eclipse IDE
1. Right-click the project → **Run As** → **Maven build...**
2. In the **Goals** field, enter `allure:serve`
3. Click **Run**

---

## Test Execution Demo

![Test Run Demo](demo.gif)
