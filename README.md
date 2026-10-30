Patient Management System Backend



. Student Information



Student Name: MUGABO Serge

Student ID:26065

Project Name: Patient Management System Backend

Course: INSY 8322 — Web Technology and Internet



# Patient Management System Backend

## 1. Project Overview

The Patient Management System is a RESTful backend application developed
for the **INSY 8322 Midterm Project Examination**. It manages
inpatients, nurses, qualifications, wards, admissions, and billing
items.

The backend provides CRUD operations, derived database queries, JPQL
queries, business-rule validation, billing operations, admission
statistics, bulk updates, a dashboard summary, and paginated admission
records.

## 2. Technologies

-   Java 17
-   Spring Boot
-   Spring Data JPA
-   Hibernate
-   PostgreSQL
-   Maven
-   Postman
-   Git and GitHub

> Check `pom.xml` for the exact Spring Boot and dependency versions used
> by this copy of the project.

## 3. Main Features

### Part A --- CRUD Operations

CRUD operations are implemented for:

-   Qualification
-   Ward
-   Nurse
-   Inpatient
-   Admission

### Part B --- Derived Queries

-   Search inpatients by last name, case-insensitively, ordered by first
    name.
-   Find admissions by status, ordered by admission date.
-   Find admissions between two dates, inclusive.

### Part C --- JPQL Queries

-   Find nurses by qualification name.
-   Find wards that are not assigned to a nurse.
-   Find qualifications that are not assigned to any nurse.
-   Find distinct inpatients associated with a nurse's admissions.

### Part D --- Business Rules

-   Validate admission dates and prevent conflicting nurse admissions.
-   Control allowed admission-status transitions.
-   Add a qualification to a nurse without removing existing
    qualifications.
-   Move a nurse to a ward while preventing assignment conflicts.

### Part E --- Billing Items

-   Create billing items for discharged admissions.
-   Retrieve billing items for an inpatient.
-   Delete billing items.
-   Prevent deletion of an admission while billing items are associated
    with it.

### Part F --- Statistics and Bulk Updates

-   Count admissions grouped by status.
-   Bulk-update eligible admissions for a nurse on a specified date,
    excluding discharged admissions.

### Bonus Features

-   Dashboard summary.
-   Paginated admission records.

## 4. Project Structure

The main source packages are organized under:

`com.Serge.patient_management_system`

Typical package responsibilities:

Package        Responsibility
  -------------- --------------------------------------------
`controller`   REST API endpoints
`domain`       JPA entity classes
`enums`        Enumeration types such as admission status
`repository`   Spring Data JPA repositories and queries
`service`      Application logic and business rules

Application configuration is stored in:

`src/main/resources/application.properties`

## 5. Prerequisites

Install the following before running the application:

1.  JDK 17 (or the Java version specified in `pom.xml`).
2.  PostgreSQL.
3.  Maven, or use the Maven Wrapper included in the project.
4.  Postman for API testing.

Verify Java:

``` bash
java -version
```

Verify Maven if installed separately:

``` bash
mvn -version
```

## 6. PostgreSQL Database Setup

### Step 1 --- Create the database

Open pgAdmin or the PostgreSQL command line and create the database.

Example database name:

``` sql
mid_db;
```

Use the database name already configured by the project if it is
different. Do not create a second database unnecessarily.

### Step 2 --- Configure the connection

Open:

`src/main/resources/application.properties`

Set the JDBC URL, PostgreSQL username, and password to match your local
PostgreSQL installation.

Example only:

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5432/patient_management_system_db
spring.datasource.username=YOUR_POSTGRES_USERNAME
spring.datasource.password=YOUR_POSTGRES_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Replace the example database name and credentials with the values used
on your computer. Preserve any working settings already present in
`application.properties`; do not overwrite project-specific
configuration blindly.

**Security:** Never commit a real database password, API key, or other
secret to a public GitHub repository. Use environment variables or a
local, untracked configuration file for private credentials.

### Step 3 --- Confirm the database is available

Before starting Spring Boot, confirm that:

-   PostgreSQL is running.
-   The configured database exists.
-   The username and password are correct.
-   The PostgreSQL JDBC dependency is present in `pom.xml`.


## 7. Run the Application

Open PowerShell or a terminal in the project root (the directory
containing `pom.xml`).

### Windows --- Maven Wrapper

``` powershell
.\mvnw.cmd spring-boot:run
```

### Maven installed separately

``` bash
mvn spring-boot:run
```

To build the project:

``` powershell
.\mvnw.cmd clean package
```

Or:

``` bash
mvn clean package
```

the base URL
is:

`http://localhost:8080`

Wait until the Spring Boot application reports that it has started
successfully. Check the console for database or application errors.

## 8. API Endpoint Reference

The following routes are based on the project requirements. Replace
placeholder IDs, dates, and file numbers with real values from my
database.

### Part B --- Derived Queries

  -----------------------------------------------------------------------------------------------------------
Method                  Endpoint                                                    Purpose
  ----------------------- ----------------------------------------------------------- -----------------------
GET                     `/api/inpatients/by-last-name?lastName=uwase`               Search inpatients by
last name

GET                     `/api/admissions/by-status?status=ADMITTED`                 Find admissions by
status

GET                     `/api/admissions/between?start=2026-10-01&end=2026-10-31`   Find admissions between
dates
  -----------------------------------------------------------------------------------------------------------

### Part C --- JPQL Queries

  -----------------------------------------------------------------------------------------------
Method                  Endpoint                                        Purpose
  ----------------------- ----------------------------------------------- -----------------------
GET                     `/api/nurses/by-qualification?name=maternity`   Find nurses by
qualification

GET                     `/api/wards/free`                               Find free wards

GET                     `/api/qualifications/unused`                    Find unused
qualifications

GET                     `/api/inpatients/of-nurse/{nurseId}`            Find inpatients
associated with a nurse
  -----------------------------------------------------------------------------------------------

### Part D --- Business Rules

  ----------------------------------------------------------------------------------------------------------
Method                  Endpoint                                                   Purpose
  ----------------------- ---------------------------------------------------------- -----------------------
POST                    `/api/admissions/save`                                     Save an admission with
validation

PATCH                   `/api/admissions/{id}/status?status=UNDER_TREATMENT`       Change admission status

PUT                     `/api/nurses/{nurseId}/qualifications/{qualificationId}`   Add a qualification to
a nurse

PATCH                   `/api/nurses/{nurseId}/ward?wardNumber=4`                  Change a nurse's ward
----------------------------------------------------------------------------------------------------------

### Part E --- Billing Items

  -----------------------------------------------------------------------------------------------------
Method                  Endpoint                                              Purpose
  ----------------------- ----------------------------------------------------- -----------------------
POST                    `/api/billing-items/save?admissionId={admissionId}`   Create a billing item

GET                     `/api/billing-items/inpatient/{fileNumber}`           Retrieve an inpatient's
billing items

DELETE                  `/api/billing-items/delete/{id}`                      Delete a billing item
-----------------------------------------------------------------------------------------------------

### Part F --- Statistics and Bulk Updates

  ----------------------------------------------------------------------------------------------------------------
Method                  Endpoint                                                         Purpose
  ----------------------- ---------------------------------------------------------------- -----------------------
GET                     `/api/admissions/stats/by-status`                                Count admissions by
status

PATCH                   `/api/admissions/cancel-day?nurseId={nurseId}&date=2026-10-20`   Refer eligible
admissions for a nurse
on a date
  ----------------------------------------------------------------------------------------------------------------

### Bonus

  --------------------------------------------------------------------------------------------------------------
Method                  Endpoint                                                       Purpose
  ----------------------- -------------------------------------------------------------- -----------------------
GET                     `/api/dashboard/summary`                                       Retrieve dashboard
statistics

GET                     `/api/admissions/page?page=0&size=5&sort=admissionDate,desc`   Retrieve paginated
admissions
  --------------------------------------------------------------------------------------------------------------


## 9. Test with Postman

Import the collection file:

`patient-management-system-midterm.postman_collection.json`

In Postman:

1.  Select **Import**.
2.  Choose **Files**.
3.  Select the collection JSON file.
4.  Import the collection.
5.  Set the base URL to `http://localhost:8080`, if required.
6.  Run the requests and inspect the response status and JSON body.

note here i have only save test


## 10. Common HTTP Status Codes

Code   Meaning
  ------ -----------------------------------------------------
200    Request succeeded
201    Resource created
400    Invalid request or business-rule validation failure
404    Requested resource not found
409    Conflict with an existing resource or business rule
500    Unexpected server-side error

The actual response depends on the endpoint and its exception handling.

## 11. Troubleshooting

**Database connection error** - Confirm PostgreSQL is running. - Verify
the database name, username, password, and port in
`application.properties`.

**Application does not start** - Check the terminal output for
compilation errors, dependency problems, or database errors. - Confirm
that the Java version matches the project configuration.

**Endpoint returns 404** - Verify the URL, HTTP method, controller
mapping, and application port.

**Endpoint returns 400 or 409** - Check request parameters, JSON body,
and the business rule associated with that endpoint.

**Expected records are missing** - Confirm that the application is
connected to the intended database and that the required seed data has
been inserted.

## 12. Submission Checklist

-   Java source code.
-   `pom.xml` and Maven Wrapper files, if included in the project.
-   `README.md`.
-   `patient-management-system-midterm.postman_collection.json`.
-   All required Postman requests, including successful and error-case
    tests.
-   No passwords, private keys, or other secrets.

## 13. Project Information

-   **Project:** Patient Management System Backend
-   **Course:** INSY 8322
-   **Assessment:** Midterm Project Examination
-   **Backend:** Java / Spring Boot
-   **Database:** PostgreSQL







MUGABO Serge  

ID: 26065

