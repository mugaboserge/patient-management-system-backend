Patient Management System Backend



&#x20;1. Student Information



Student Name: MUGABO Serge

Student ID:26065

Project Name: Patient Management System Backend

Course: INSY 8322 — Web Technology and Internet



2\. Project Description



The Patient Management System Backend is a REST API developed using Spring Boot and PostgreSQL.



It provides endpoints for managing hospital wards, nurses, qualifications, inpatients, admissions, and billing items.



3\. Technologies Used



\- Java

\- Spring Boot

\- Spring Data JPA

\- Hibernate

\- PostgreSQL

\- Maven

\- Postman

\- Git and GitHub



4\. Prerequisites



Before running the application, install:



1\. Java Development Kit (JDK) compatible with the project configuration.

2\. PostgreSQL.

3\. Maven, or use the included Maven Wrapper.

4\. Postman for API testing.



5\. Database Configuration



Create a PostgreSQL database named `PMS\_db` if it does not already exist.



Configure the database connection using environment variables:



DB\_URL: jdbc:postgresql://localhost:5432/PMS\_db

DB\_USERNAME`: 

DB\_PASSWORD`: admin







Step 5: Run the application



On Windows PowerShell:



powershell

.\\mvnw.cmd spring-boot:run





The application uses port `8080` by default unless configured otherwise.



7\. API Endpoints



The following endpoints are available in the current implementation.



| Resource | HTTP Method | Endpoint | Purpose |

|---|---|---|---|

| Wards | POST | `/api/wards/save` | Create a ward |

| Wards | GET | `/api/wards/all` | Retrieve all wards |

| Wards | GET | `/api/wards/{id}` | Retrieve a ward by ID |

| Wards | PUT | `/api/wards/update/{id}` | Update a ward |

| Wards | DELETE | `/api/wards/delete/{id}` | Delete a ward |

| Qualifications | POST | `/api/qualifications/save` | Create a qualification |

| Qualifications | GET | `/api/qualifications/all` | Retrieve all qualifications |

| Qualifications | GET | `/api/qualifications/unused` | Retrieve unassigned qualifications |

| Qualifications | GET | `/api/qualifications/{id}` | Retrieve a qualification by ID |

| Qualifications | PUT | `/api/qualifications/update/{id}` | Update a qualification |

| Qualifications | DELETE | `/api/qualifications/delete/{id}` | Delete a qualification |

| Nurses | GET | `/api/nurses/all` | Retrieve all nurses |

| Nurses | GET | `/api/nurses/by-qualification?name=...` | Search nurses by qualification |

| Inpatients | GET | `/api/inpatients/all` | Retrieve all inpatients |

| Inpatients | GET | `/api/inpatients/by-last-name?lastName=...` | Search inpatients by last name |

| Admissions | GET | `/api/admissions/all` | Retrieve all admissions |

| Admissions | GET | `/api/admissions/by-status?status=ADMITTED` | Filter admissions by status |

| Admissions | GET | `/api/admissions/between?start=YYYY-MM-DD\&end=YYYY-MM-DD` | Retrieve admissions between dates |

| Admissions | GET | `/api/admissions/inpatients/of-nurse/{nurseId}` | Retrieve inpatients associated with a nurse |

| Billing Items | GET | `/api/billing-items/all` | Retrieve all billing items |

| Billing Items | GET | `/api/billing-items/{id}` | Retrieve a billing item by ID |

| Billing Items | GET | `/api/billing-items/inpatient/{fileNumber}` | Retrieve billing items by inpatient file number |



Other endpoints are implemented for creating, updating, deleting, and managing resources. Refer to the corresponding controllers for the complete route definitions.



8\. Postman Testing



The Postman collection will be provided in the project root as:



`patient-management-system-midterm.postman\_collection.json`







MUGABO Serge  

ID: 26065

