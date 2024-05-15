package com.application.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class CustomerLoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String otp;
    private String aadharNumber;
    private String mobileNumber;
    private LocalDateTime createdAt;
    private Integer failedAttempt;
    private Boolean isLocked;
    private LocalDateTime accountLockedTime;


}
