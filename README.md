# Insurance Eligibility System (IES)

## Project Overview
The **Insurance Eligibility System (IES)** was developed to provide fully integrated health and insurance plans for the citizens based on their circumstances. This intranet-based system is accessible exclusively by DHS (Department of Human Services) offices, where case workers assist citizens in applying for various government plans.

## Key Features
The system offers several plans:
- **SNAP**: A food assistance program for citizens with no or low income.
- **CCAP**: A childcare assistance program for low-income families.
- **Medicaid**: A health plan for people with limited income.
- **Medicare**: A health plan for citizens aged 65 and above.
- **QHP**: A commercial health plan that citizens can purchase.

## Tech Stack
The project was developed using the following technologies:
- **Languages**: Java, J2EE
- **Frameworks**: Spring Boot, Microservices
- **Database**: MySQL
- **Cloud Platform**: AWS
- **Front-End**: Angular

This stack ensured the system's scalability and security.

## Modules and Functionalities
The system comprised several modules:
1. **Admin Module**: Managed plans and case worker accounts.
2. **User Management Module**: Handled user login, registration, and profiles.
3. **Application Registration**: Verified citizenship for New Jersey state residents.
4. **Data Collection**: Collected personal and income data from citizens applying for plans.
5. **Eligibility Determination**: Checked citizen eligibility based on plan rules using a rule-based engine.
6. **Correspondence**: Sent notices to citizens about their application status.
7. **Benefit Issuance**: Issued monthly benefits to approved citizens.
8. **Reports**: Generated daily reports, including data on approved or denied citizens.

## My Role
I was responsible for backend development, specifically designing and implementing microservices to support the system's architecture.

## Challenges
One of the major challenges was ensuring the system correctly applied plan rules during the eligibility determination phase. Each plan had unique rules, and we had to ensure citizens' data exactly matched the specific plan requirements, utilizing a rule-based engine to handle this complexity.

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

