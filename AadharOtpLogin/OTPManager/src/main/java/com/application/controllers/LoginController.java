package com.application.controllers;

import com.application.dtos.CustomerLoginInfoDto;
import com.application.entities.CustomerLoginInfo;
import com.application.exceptions.exception.InvalidDataException;
import com.application.services.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login/")
public class LoginController {
    private final LoginService loginService;
    LoginController(LoginService loginService){
        this.loginService=loginService;

    }
    @GetMapping(value = "/generate-otp/{aadharNumber}")
    public ResponseEntity<String> generateOtp(@PathVariable String aadharNumber)
    {
        try {
            String mobileNumber = loginService.generateOtp(aadharNumber);
            return new ResponseEntity<>("Otp send to you mobile number : "+mobileNumber, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping(value="/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody CustomerLoginInfoDto customerLoginInfoDto){
      try{
           loginService.verifyOtp(customerLoginInfoDto);
           return new ResponseEntity<>("Welcome to Aadhar World, where all data is secure.", HttpStatus.OK);
       }
       catch (Exception e){
           return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
       }
    }

    @GetMapping(value = "/unlock-account/{aadharNumber}")
    public ResponseEntity<String> unlockAccount(@PathVariable String aadharNumber)
    {
        try {
            loginService.unlockAccount(aadharNumber);
            return new ResponseEntity<>("Account is Unlocked, you can login now.", HttpStatus.OK);
        } catch (InvalidDataException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping(value = "/get-all-login-details")
    public ResponseEntity<List<CustomerLoginInfo>> getAllLoginDetails(@PathVariable String aadharNumber)
    {
         return new ResponseEntity<>(loginService.getAllLoginDetails(),HttpStatus.OK);
    }
}
