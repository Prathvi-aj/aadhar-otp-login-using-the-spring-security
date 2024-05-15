package com.application.services;

import com.application.dtos.CustomerLoginInfoDto;
import com.application.dtos.UserDetailsDto;
import com.application.entities.CustomerLoginInfo;
import com.application.exceptions.exception.AccountLockedException;
import com.application.exceptions.exception.InvalidDataException;
import com.application.exceptions.exception.InvalidOtpException;
import com.application.exceptions.exception.OtpExpiredException;
import com.application.repos.CustomerLoginInfoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class LoginService {
    private final ModelMapper modelMapper;
    private final CustomerLoginInfoRepository customerLoginInfoRepository;
    private final UserWebClientService userWebClientService;

    LoginService(ModelMapper modelMapper, CustomerLoginInfoRepository customerLoginInfoRepository, UserWebClientService userWebClientService) {
        this.modelMapper = modelMapper;
        this.customerLoginInfoRepository = customerLoginInfoRepository;
        this.userWebClientService = userWebClientService;
    }

    public String generateOtp(String aadharNumber) {
        CustomerLoginInfo customerLoginInfo = customerLoginInfoRepository.findByAadharNumber(aadharNumber);
        if (Objects.nonNull(customerLoginInfo) && customerLoginInfo.getIsLocked()) {
            throw new AccountLockedException("Your account is currently locked. Please unlock your account and try again.");
        }
        if (Objects.isNull(customerLoginInfo)) {
            customerLoginInfo = new CustomerLoginInfo();
        }

        UserDetailsDto userDetailsDto = userWebClientService.fetchCustomerData(aadharNumber).block();
        String otp = generateOTP();
        System.out.println("OTP sent to mobile number " + userDetailsDto.getMobileNumber() + ", otp is " + otp);

        customerLoginInfo.setOtp(otp);
        customerLoginInfo.setCreatedAt(LocalDateTime.now());
        customerLoginInfo.setIsLocked(Boolean.FALSE);
        customerLoginInfo.setFailedAttempt(0);
        customerLoginInfo.setMobileNumber(userDetailsDto.getMobileNumber());
        customerLoginInfo.setAadharNumber(aadharNumber);
        customerLoginInfoRepository.save(customerLoginInfo);
        return maskPhoneNumber(customerLoginInfo.getMobileNumber());
    }


    public Boolean verifyOtp(CustomerLoginInfoDto customerLoginInfoDto) {
        CustomerLoginInfo customerLoginInfo = customerLoginInfoRepository.findByAadharNumber(customerLoginInfoDto.getAadharNumber());
        if (Objects.isNull(customerLoginInfo) || customerLoginInfo.getIsLocked()) {
            throw new AccountLockedException("Account is locked or does not exist, Enter correct details or try again after 1 hour");
        }
        if (!customerLoginInfo.getOtp().equals(customerLoginInfoDto.getOtp())) {
            int failedAttempts = customerLoginInfo.getFailedAttempt() + 1;
            customerLoginInfo.setFailedAttempt(failedAttempts);

            if (failedAttempts >= 3) {
                lockAccount(customerLoginInfo);
                throw new AccountLockedException("Account is locked due to too many failed attempts, try again after 1 hour.");
            }
            customerLoginInfoRepository.save(customerLoginInfo);
            throw new InvalidOtpException("Invalid OTP, try again. You have " + (3 - failedAttempts) + " more attempts.");
        }

        LocalDateTime otpGenerationTime = customerLoginInfo.getCreatedAt();
        LocalDateTime currentTimestamp = LocalDateTime.now();

        if (currentTimestamp.minusSeconds(180).isAfter(otpGenerationTime)) {
            throw new OtpExpiredException("OTP has expired");
        }
        resetFailedAttemptsAndUnlockAccount(customerLoginInfo);
        return true;
    }

    private void lockAccount(CustomerLoginInfo customerLoginInfo) {
        customerLoginInfo.setIsLocked(true);
        customerLoginInfo.setAccountLockedTime(LocalDateTime.now());
    }

    private void resetFailedAttemptsAndUnlockAccount(CustomerLoginInfo customerLoginInfo) {
        customerLoginInfo.setFailedAttempt(0);
        customerLoginInfo.setOtp(null);
        customerLoginInfo.setIsLocked(Boolean.FALSE);
        customerLoginInfoRepository.save(customerLoginInfo);
    }

    public void unlockAccount(String aadharNumber) {
        CustomerLoginInfo customerLoginInfo = customerLoginInfoRepository.findByAadharNumber(aadharNumber);
        if (Objects.isNull(customerLoginInfo)) {
            throw new InvalidDataException("Aadhar Number " + aadharNumber + " is not present, please enter correct details.");
        } else if (!customerLoginInfo.getIsLocked()) {
            throw new InvalidDataException("Your Account is already Unlocked.");
        }
        LocalDateTime oneHourAhead = customerLoginInfo.getAccountLockedTime().plusHours(1);
        long minutesLeft = Duration.between(LocalDateTime.now(), oneHourAhead).toMinutes();
        if (minutesLeft > 0) {
            throw new AccountLockedException("please try to unlock after " + minutesLeft + " minutes");
        }

        customerLoginInfo.setIsLocked(Boolean.FALSE);
        customerLoginInfo.setAccountLockedTime(null);
        customerLoginInfo.setFailedAttempt(0);

        customerLoginInfoRepository.save(customerLoginInfo);
    }


    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public String maskPhoneNumber(String phoneNumber) {
        int length = phoneNumber.length();
        String lastThreeDigits = phoneNumber.substring(length - 3);
        String maskedDigits = new String(new char[length - 3]).replace("\0", "*");
        return maskedDigits + lastThreeDigits;
    }

    public List<CustomerLoginInfo> getAllLoginDetails() {
        return customerLoginInfoRepository.findAll();
    }
}
