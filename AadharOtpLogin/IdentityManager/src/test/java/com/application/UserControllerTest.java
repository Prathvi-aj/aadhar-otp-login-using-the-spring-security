package com.application;

import com.application.controllers.UserController;
import com.application.dto.UserDetailsDto;
import com.application.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUser_ValidUser_ReturnsCreated() {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setMobileNumber("1234567890");
        userDetailsDto.setAadharNumber("12345678901234");
        when(userService.addUser(userDetailsDto)).thenReturn(userDetailsDto);

        ResponseEntity<String> response = userController.addUser(userDetailsDto, mock(BindingResult.class));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User Identity 12345678901234 is added.", response.getBody());
        verify(userService, times(1)).addUser(userDetailsDto);
    }

    @Test
    void addUser_InvalidUser_ReturnsBadRequest() {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("userDetailsDto", "Invalid user details")));

        ResponseEntity<String> responseEntity = userController.addUser(userDetailsDto, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Validation error: Invalid user details", responseEntity.getBody());
        verifyNoInteractions(userService);
    }

    @Test
    void getUser_ExistingUser_ReturnsUserDetails() {
        String mobileNumber = "1234567890";
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setMobileNumber(mobileNumber);
        when(userService.getUser(mobileNumber)).thenReturn(userDetailsDto);

        ResponseEntity<?> response = userController.getUser(mobileNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDetailsDto, response.getBody());
        verify(userService, times(1)).getUser(mobileNumber);
    }

    @Test
    void getUser_NonExistingUser_ReturnsNotFound() {
        String mobileNumber = "1234567890";
        when(userService.getUser(mobileNumber)).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<?> response = userController.getUser(mobileNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
        verify(userService, times(1)).getUser(mobileNumber);
    }
}
