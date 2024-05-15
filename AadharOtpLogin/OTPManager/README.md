# OTPManager Service

## Overview

The OTPManager service is responsible for managing OTP (One-Time Password) authentication for users.

## Getting Started

To run the OTPManager service, you need:

- JDK 17 or higher version
- Maven for building the project
- MySQL database (you can configure the database connection in the resource/config/application.yml file)

## Database Configuration

IdentityManager service uses a MySQL database running on port 3306.  you can create the schema in your database 
     
     create schema test_schema;
     
Or If you want to add your database and schema, you can configure the database details in the resource/config/application.yml file.

## Starting the Service
1. Clone the repository:
     ```
     git clone https://github.com/Prathvi-aj/aadhar-otp-login-using-the-spring-security.git
     ```
2. Using the Intelij
   
   a. Build the maven project using maven tool

   b. Execute the main class OTPManagerApplication
   
   c. service will start and you can access application at port 8085, you change the port in resource/config/application.yml file
3. Using the command
   
    
    b. Navigate to the OTPManager directory:
     ```
     cd OTPManager
     ```
    c. Build the project using Maven:
     ```
     mvn clean install
     ```
    d. Run the application:
     ```
     java -jar target/OTPManager.jar
     ```

## API Access

The OTPManager service runs on port 8081 by default. You can access the API using the following endpoints:

- **Generate OTP**: `GET /api/otp/generate-otp/{aadharNumber}`
  ```
  curl --location 'localhost:8081/api/otp/generate-otp/433330005784' \
  ```

- **Verify OTP**: `POST /api/otp/verify-otp`
  ```
  curl --location --request POST 'localhost:8081/api/otp/verify-otp' \
  --header 'Content-Type: application/json' \
  --data-raw '{
      "aadharNumber":"433330005784",
      "otp":"123456"
  }'
  ```

- **Unlock Account**: `GET /api/otp/unlock-account/{aadharNumber}`
  ```
  curl --location 'localhost:8081/api/otp/unlock-account/433330005784' \
  ```

- **Get All Login Details**: `GET /api/otp/get-all-login-details`
  ```
  curl --location 'localhost:8081/api/otp/get-all-login-details' \
  --header 'Authorization: Basic YWRtaW46cGFzc3dvcmQ=' \
  ```

## Database Configuration

OTPManager service uses a MySQL database running on port 3306.  you can create the schema in your database 

     create schema test_schema;
     
Or If you want to add your database and schema, you can configure the database connection in the resource/config/application.yml file.
## Authentication

Authentication is not required in login apis like (generate-otp,veriify-otp and unlock-account) but for other apis like get-all-login-details,its required to
access the OTPManager service. You need to provide a valid username and password or Authorization Key in the header when making API requests.

## Testing

The OTPManager service includes JUnit test cases for testing the controller, services, and repository functionality.
You can run all unit test cases using IntelliJ maven tool or using the below command.
```
mvn test
```
