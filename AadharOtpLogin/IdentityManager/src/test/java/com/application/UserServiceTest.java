package com.application;

import com.application.dto.UserDetailsDto;
import com.application.entities.UserDetails;
import com.application.repos.UserDetailsRepository;
import com.application.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    private UserService userService;

    private UserDetailsDto userDetailsDto;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsDto = new UserDetailsDto();
        userDetails = new UserDetails();
    }

    @Test
    void addUser_ValidUserDetailsDto_UserAddedSuccessfully() {
        when(modelMapper.map(userDetailsDto, UserDetails.class)).thenReturn(userDetails);

        userService.addUser(userDetailsDto);

        verify(userDetailsRepository, times(1)).save(userDetails);
    }

    @Test
    void getUser_ValidMobileNumber_ReturnsUserDetailsDto() {
        String mobileNumber = "1234567890";
        userDetails.setMobileNumber(mobileNumber);
        when(userDetailsRepository.findByMobileNumber(mobileNumber)).thenReturn(userDetails);

        UserDetailsDto expectedUserDetailsDto = new UserDetailsDto();
        expectedUserDetailsDto.setMobileNumber(mobileNumber);
        when(modelMapper.map(userDetails, UserDetailsDto.class)).thenReturn(expectedUserDetailsDto);

        UserDetailsDto userDetailsDto = userService.getUser(mobileNumber);
        assertEquals(userDetailsDto.getMobileNumber(), mobileNumber);
    }

    @Test
    void getUser_ValidAadharNumber_ReturnsUserDetailsDto() {
        String aadharNumber = "12345678901234";
        userDetails.setAadharNumber(aadharNumber);
        when(userDetailsRepository.findByAadharNumber(aadharNumber)).thenReturn(userDetails);

        UserDetailsDto expectedUserDetailsDto = new UserDetailsDto();
        expectedUserDetailsDto.setAadharNumber(aadharNumber);
        when(modelMapper.map(userDetails, UserDetailsDto.class)).thenReturn(expectedUserDetailsDto);

        UserDetailsDto userDetailsDto = userService.getUser(aadharNumber);
        assertEquals(userDetailsDto.getAadharNumber(), aadharNumber);
    }

    @Test
    void getUser_InvalidData_ThrowsRuntimeException() {
        String value = "12345";
        when(userDetailsRepository.findByMobileNumber(value)).thenReturn(null);
        when(userDetailsRepository.findByAadharNumber(value)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.getUser(value));
    }
}
