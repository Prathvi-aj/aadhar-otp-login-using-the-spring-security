# Secure Identity and OTP Management Application
This application provides secure identity management and OTP (One-Time Password) functionality. It consists of two main services: IdentityManager and OTPManager.
The IdentityManager service manages user identities, storing Aadhar numbers and mobile numbers, while the OTPManager service handles OTP generation, verification, and account unlocking.
## IdentityManager Service

##### Endpoints

- **Add User**: `POST /add-user`
  - Description: Add a new user identity with Aadhar number and mobile number.
  - Request Body: UserDetailsDto
  - Response:
    - Success: HTTP 201 Created, returns a success message.
    - Error: HTTP 400 Bad Request with validation error messages if request body is invalid.

- **Get User**: `GET /get-user/{data}`
  - Description: Retrieve user details by providing either Aadhar number or mobile number.
  - Path Variable: Aadhar number or mobile number
  - Response:
    - Success: HTTP 200 OK, returns UserDetailsDto.
    - Error: HTTP 404 Not Found if user details are not found.

## OTPManager Service

##### Endpoints

- **Generate OTP**: `GET /generate-otp/{aadharNumber}`
  - Description: Generate OTP for the provided Aadhar number.
  - Path Variable: Aadhar number
  - Response:
    - Success: HTTP 200 OK, returns the OTP sent to the user's mobile number.
    - Error: HTTP 404 Not Found if Aadhar number is not found.

- **Verify OTP**: `POST /verify-otp`
  - Description: Verify the OTP provided by the user.
  - Request Body: CustomerLoginInfoDto
  - Response:
    - Success: HTTP 200 OK with a welcome message.
    - Error: HTTP 406 Not Acceptable if OTP verification fails.

- **Unlock Account**: `GET /unlock-account/{aadharNumber}`
  - Description: Unlock the user account associated with the provided Aadhar number.
  - Path Variable: Aadhar number
  - Response:
    - Success: HTTP 200 OK with a success message.
    - Error: HTTP 406 Not Acceptable if the account is already unlocked or invalid data provided, HTTP 404 Not Found if Aadhar number is not found.

- **Get All Login Details**: `GET /get-all-login-details`
  - Description: Retrieve all login details.
  - Response:
    - Success: HTTP 200 OK, returns a list of CustomerLoginInfo objects.
    - Error: HTTP 404 Not Found if no login details are found.

## Spring Security

Both services secured using Spring Security to authenticate and authorize access to endpoints.
Ensure proper configuration of authentication and authorization mechanisms according requirements.

## Notes
After cloning the repo first configure the database and when you start the running services(check each service documentation(readme file) to know how to run), don't open both services in single intellij tab So you don't need to build both services together because both services contains some common classes so conflict will occure while running the main classes. For solving that issue you can open both service in different different intllij tab Like this :
![image](https://github.com/Prathvi-aj/aadhar-otp-login-using-the-spring-security/assets/56496325/05f861ae-409d-4cde-9230-5b280ccbb08c)

