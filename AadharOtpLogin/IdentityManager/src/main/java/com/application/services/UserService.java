package com.application.services;

import com.application.dto.UserDetailsDto;
import com.application.entities.UserDetails;
import com.application.exceptions.exception.IdenityNotFoundException;
import com.application.repos.UserDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final ModelMapper modelMapper;
    private final UserDetailsRepository userDetailsRepository;
    UserService(ModelMapper modelMapper,UserDetailsRepository userDetailsRepository){
        this.modelMapper=modelMapper;
        this.userDetailsRepository=userDetailsRepository;
    }

    public UserDetailsDto addUser(UserDetailsDto userDetailsDto) {
        UserDetails userDetails=userDetailsRepository.findByAadharNumber(userDetailsDto.getAadharNumber());
        if(Objects.isNull(userDetails)) {
            userDetails=modelMapper.map(userDetailsDto, UserDetails.class);
        }
        else{
            userDetails.setMobileNumber(userDetailsDto.getMobileNumber());
        }
        userDetailsRepository.save(userDetails);
        return userDetailsDto;
    }

    public UserDetailsDto getUser(String data) {
        UserDetails userDetails;
        if(data.length()==10) {
            userDetails= userDetailsRepository.findByMobileNumber(data);
        }
        else{
            userDetails=userDetailsRepository.findByAadharNumber(data);
        }
        if(Objects.isNull(userDetails)){
            throw new IdenityNotFoundException("User Identity not found with given data");
        }

        return modelMapper.map(userDetails,UserDetailsDto.class);
    }
}
