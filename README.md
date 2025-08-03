# Insurance Claim Processing System

## Project Overview
The **Insurance Claim Processing System** was developed to provide fully integrated health and insurance plans for the citizens based on their circumstances. This intranet-based system is accessible exclusively by DHS (Department of Human Services) offices, where case workers assist citizens in applying for various government plans.


## 📌 Table of Contents

- [Project Overview](#project-overview)
- [Key Features](#key-features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Modules](#modules)
- [Setup Instructions](#setup-instructions)
- [Postman & Swagger](#postman--swagger)
- [Developer Info](#developer-info)
- [License](#license)

---

## 📖 Project Overview

The **Claims Processing System** (CPS) serves as an internal platform used by Department of Human Services (DHS) caseworkers to process claims and determine eligibility for various government plans like:

- SNAP (Supplemental Nutrition Assistance Program)
- CCAP (Child Care Assistance Program)
- Medicaid & Medicare
- QHP (Qualified Health Plan)

It automates claim registration, eligibility determination, benefit issuance, and communication with applicants, using modern enterprise technologies in a scalable setup.

---

## 🌟 Key Features

- Claim submission and tracking
- Citizen registration and data verification
- Eligibility checks using rule-based logic
- Kafka-based async notifications
- Benefit issuance and correspondence
- Detailed report generation

---

## 🏗️ Architecture

![Project Architecture](./f93833e7-bfbd-494c-b688-81b5f701f1a6.png)

---

## 🧰 Tech Stack

| Layer            | Technologies                                        |
|------------------|-----------------------------------------------------|
| Backend          | Java, Spring Boot, Spring Security, J2EE           |
| Frontend         | Angular                                             |
| Database         | MySQL                                               |
| Caching          | Redis                                               |
| Messaging Queue  | Apache Kafka                                        |
| AuthN/AuthZ      | OAuth2, JWT                                         |
| DevOps           | Docker, Kubernetes, Jenkins, GitLab CI/CD          |
| Monitoring       | Splunk                                              |
| Documentation    | Swagger/OpenAPI, Postman                            |
| Cloud            | AWS                                                 |

---

## 🧩 Modules

### Core Services
1. **Claims Service** – Manages submission, processing, and tracking of insurance claims  
2. **User Service** – Handles citizen/user data and plan linking  
3. **Report Service** – Generates claim reports, approvals, denials, and benefit history  
4. **Redis Cache** – Improves performance by storing frequently accessed data  
5. **Kafka Service** – Enables asynchronous, decoupled communication between modules  

### Eligibility System Components
6. **Admin Module** – Manages insurance plans and caseworker accounts  
7. **Application Registration** – Validates NJ state residency and registers applications  
8. **Data Collection** – Gathers financial/personal info to assess eligibility  
9. **Eligibility Engine** – Rule-based checks for each plan’s eligibility criteria  
10. **Correspondence** – Notifies citizens about application status  
11. **Benefit Issuance** – Issues benefits for approved plans  

---
## Key Features
The system offers several plans:
- **SNAP**: A food assistance program for citizens with no or low income.
- **CCAP**: A childcare assistance program for low-income families.
- **Medicaid**: A health plan for people with limited income.
- **Medicare**: A health plan for citizens aged 65 and above.
- **QHP**: A commercial health plan that citizens can purchase.


## My Role
I was responsible for backend development, specifically designing and implementing microservices to support the system's architecture.

## Challenges
One of the major challenges was ensuring the system correctly applied plan rules during the eligibility determination phase. Each plan had unique rules, and we had to ensure citizens' data exactly matched the specific plan requirements, utilising a rule-based engine to handle this complexity.

## Impact
This project significantly streamlined the process for the state government, making it easier for case workers to assist citizens, accurately determine eligibility, and ensure that eligible citizens received their benefits promptly.


## How to Run the Project

### Prerequisites
Before running the project, ensure you have the following installed:
- **Java JDK 8+**
- **MySQL** (or any other compatible database)
- **Apache Maven** (for building the project)
- **Node.js** and **npm** (for running the Angular frontend)
- **AWS CLI** (if deploying on AWS)
  
### Steps to Set Up and Run the Project

#### 1. Clone the Repository
```bash
git clone https://github.com/your-username/insurance-eligibility-system.git
cd insurance-eligibility-system


**## Backend Setup**
1. Configure Database:

Create a MySQL database.
Update the database configuration in src/main/resources/application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/your-database-name
spring.datasource.username=your-username
spring.datasource.password=your-password

2. Build the Backend:
Run the following command to build the backend using Maven
mvn clean install

3. Run the Backend:
Start the Spring Boot application


👨‍💻 Developer Info
**Arbaz Sayyad** 
Full Stack Java Developer | Open to New Opportunities
📧 arbaz4sayyad@gmail.com
🔗 [LinkedIn](https://www.linkedin.com/in/arbaz-sayyad/)
🌐 [Portfolio](https://arbaz4sayyad.github.io/MyPortfolio/)


## 📄 License

This project is for demonstration purposes and not intended for production use.
