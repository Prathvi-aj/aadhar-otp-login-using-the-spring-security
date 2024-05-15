package com.application;

import com.application.dtos.CustomerLoginInfoDto;
import com.application.dtos.UserDetailsDto;
import com.application.entities.CustomerLoginInfo;
import com.application.exceptions.exception.AccountLockedException;
import com.application.exceptions.exception.InvalidOtpException;
import com.application.exceptions.exception.OtpExpiredException;
import com.application.repos.CustomerLoginInfoRepository;
import com.application.services.LoginService;
import com.application.services.UserWebClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private CustomerLoginInfoRepository customerLoginInfoRepository;

    @Mock
    private UserWebClientService userWebClientService;

    @InjectMocks
    private LoginService loginService;

    private CustomerLoginInfoDto customerLoginInfoDto;
    private UserDetailsDto userDetailsDto;
    private CustomerLoginInfo customerLoginInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerLoginInfoDto = new CustomerLoginInfoDto();
        customerLoginInfoDto.setAadharNumber("1234567890");
        customerLoginInfoDto.setOtp("123456");

        userDetailsDto = new UserDetailsDto();
        userDetailsDto.setMobileNumber("9876543210");

        customerLoginInfo = new CustomerLoginInfo();
        customerLoginInfo.setMobileNumber(userDetailsDto.getMobileNumber());
        customerLoginInfo.setAadharNumber(customerLoginInfoDto.getAadharNumber());
    }

    @Test
    void testGenerateOtp() {
        when(userWebClientService.fetchCustomerData(customerLoginInfoDto.getAadharNumber())).thenReturn(Mono.just(userDetailsDto));
        when(customerLoginInfoRepository.findByAadharNumber(customerLoginInfoDto.getAadharNumber())).thenReturn(null);
        when(customerLoginInfoRepository.save(any())).thenReturn(customerLoginInfo);

        String mobileNumber = loginService.generateOtp(customerLoginInfoDto.getAadharNumber());
        assertNotNull(mobileNumber);
        assertEquals("*******210", loginService.maskPhoneNumber(mobileNumber));
    }

    @Test
    void testVerifyOtp() {
        customerLoginInfo.setOtp("123456");
        customerLoginInfo.setIsLocked(Boolean.FALSE);
        customerLoginInfo.setCreatedAt(LocalDateTime.now().minusSeconds(180));

        when(customerLoginInfoRepository.findByAadharNumber(customerLoginInfoDto.getAadharNumber())).thenReturn(customerLoginInfo);
        assertThrows(OtpExpiredException.class, () -> loginService.verifyOtp(customerLoginInfoDto));

        customerLoginInfo.setCreatedAt(LocalDateTime.now());
        assertTrue(loginService.verifyOtp(customerLoginInfoDto));
    }

    @Test
    void testUnlockAccount() {
        customerLoginInfo.setIsLocked(Boolean.TRUE);
        customerLoginInfo.setAccountLockedTime(LocalDateTime.now());

        when(customerLoginInfoRepository.findByAadharNumber(customerLoginInfoDto.getAadharNumber())).thenReturn(customerLoginInfo);
        assertThrows(AccountLockedException.class, () -> loginService.unlockAccount(customerLoginInfoDto.getAadharNumber()));

        customerLoginInfo.setAccountLockedTime(LocalDateTime.now().minusHours(1));
        assertDoesNotThrow(() -> loginService.unlockAccount(customerLoginInfoDto.getAadharNumber()));
        assertFalse(customerLoginInfo.getIsLocked());
        assertNull(customerLoginInfo.getAccountLockedTime());
        assertEquals(0, customerLoginInfo.getFailedAttempt());
    }

    @Test
    void verifyOtp_InvalidOtp_ReturnsInvalidOtpException() {
        customerLoginInfo.setOtp("654789");
        customerLoginInfo.setCreatedAt(LocalDateTime.now());
        customerLoginInfo.setIsLocked(Boolean.FALSE);
        customerLoginInfo.setFailedAttempt(0);

        when(customerLoginInfoRepository.findByAadharNumber(any(String.class))).thenReturn(customerLoginInfo);
        assertThrows(InvalidOtpException.class, () -> loginService.verifyOtp(customerLoginInfoDto));

        verify(customerLoginInfoRepository, times(1)).save(customerLoginInfo);
    }

    @Test
    void verifyOtp_ExceededFailedAttempts_AccountLocked() {
        customerLoginInfo.setOtp("124556");
        customerLoginInfo.setCreatedAt(LocalDateTime.now());
        customerLoginInfo.setFailedAttempt(2);
        customerLoginInfo.setIsLocked(Boolean.FALSE);

        when(customerLoginInfoRepository.findByAadharNumber(any(String.class))).thenReturn(customerLoginInfo);
        assertThrows(AccountLockedException.class, () -> loginService.verifyOtp(customerLoginInfoDto));

        verify(customerLoginInfoRepository, never()).save(customerLoginInfo);
    }
}
