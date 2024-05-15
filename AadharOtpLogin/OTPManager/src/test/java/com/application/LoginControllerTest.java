package com.application;

import com.application.controllers.LoginController;
import com.application.dtos.CustomerLoginInfoDto;
import com.application.services.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Test
    public void testGenerateOtp() {
        when(loginService.generateOtp(anyString())).thenReturn("1234567890");
        ResponseEntity<String> responseEntity = loginController.generateOtp("123456789012");

        verify(loginService, times(1)).generateOtp("123456789012");

        assert (responseEntity.getStatusCode()).equals(HttpStatus.OK);
        assert (responseEntity.getBody()).contains("Otp send to you mobile number");
    }

    @Test
    public void testVerifyOtp() {
        when(loginService.verifyOtp(any(CustomerLoginInfoDto.class))).thenReturn(true);
        CustomerLoginInfoDto customerLoginInfoDto = new CustomerLoginInfoDto();
        ResponseEntity<String> responseEntity = loginController.verifyOtp(customerLoginInfoDto);

        verify(loginService, times(1)).verifyOtp(customerLoginInfoDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Welcome to Aadhar World"));
    }

    @Test
    public void testUnlockAccount() {
        doNothing().when(loginService).unlockAccount(anyString());
        ResponseEntity<String> responseEntity = loginController.unlockAccount("123456789012");

        verify(loginService, times(1)).unlockAccount("123456789012");

        assert (responseEntity.getStatusCode()).equals(HttpStatus.OK);
        assert (responseEntity.getBody()).contains("Account is Unlocked");
    }
}

