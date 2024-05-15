# IdentityManager Service

## Overview

The IdentityManager service is responsible for managing user identities, including Aadhar numbers and mobile numbers.

## Getting Started

To run the IdentityManager service, you need:

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

   b. Execute the main class IdentityManagerApplication
   
   c. service will start and you can access application at port 8085, you change the port in resource/config/application.yml file
3. Using the command
   
    
    b. Navigate to the IdentityManager directory:
     ```
     cd IdentityManager
     ```
    c. Build the project using Maven:
     ```
     mvn clean install
     ```
    d. Run the application:
     ```
     java -jar target/IdentityManager.jar
     ```

## API Access

The IdentityManager service runs on port 8085 by default. You can access the API using the following endpoints:

- **Add User**: `POST /api/user/add-user`
  ```
  curl --location 'localhost:8085/api/user/add-user' \
  --header 'Authorization: Basic YWRtaW46cGFzc3dvcmQ=' \
  --header 'Content-Type: application/json' \
  --data '{
      "aadharNumber":"433330005784",
      "mobileNumber":"7777880000"
  }'
  ```

- **Get User**: `GET /api/user/get-user/{data}`
  ```
  curl --location 'localhost:8085/api/user/get-user/433330005784' \
  --header 'Authorization: Basic YWRtaW46cGFzc3dvcmQ=' \
  ```


## Authentication

Authentication is required to access the IdentityManager service, To aacess application api you need to pass username and password 
    
     
     username: admin
     password: password
     

![image](https://github.com/Prathvi-aj/aadhar-otp-login-using-the-spring-security/assets/56496325/214a837d-8a5f-4801-9629-247858016186)

Or you can pass the Authorization Key in header 
    
    
     Authorization: Basic YWRtaW46cGFzc3dvcmQ=
     

![image](https://github.com/Prathvi-aj/aadhar-otp-login-using-the-spring-security/assets/56496325/53c27abe-0eed-43c8-9f0b-64ae4fd02582)


## Testing

The IdentityManager service includes JUnit test cases for testing the controller, services, and repository functionality.
You can run all unit test cases using IntelliJ maven tool or using below command.
```
mvn test
```
