package com.application.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDetailsDto {
    @NotBlank(message = "Mobile Number is required")
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Aadhar Number is required")
    @Size(min = 12, max = 12, message = "Aadhar number must be 12 digits")
    private String aadharNumber;
}
