package com.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerLoginInfoDto {

    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;

    @NotBlank(message = "Adhar Number is required")
    @Size(min = 12, max = 12, message = "Adhar number must be 10 digits")
    private String aadharNumber;

    private String mobileNumber;
    private LocalDateTime createdAt;
    private Integer failedAttempt;
    private Boolean isLocked;

}
