package com.application.controllers;

import com.application.dto.UserDetailsDto;
import com.application.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    private final UserService userService;
    UserController(UserService userService){
        this.userService=userService;
    }
    @PostMapping(value = "/add-user")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDetailsDto userDetailsDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body("Validation error: " + String.join(", ", errorMessages));
        }
        UserDetailsDto userDetails=userService.addUser(userDetailsDto);
        return new ResponseEntity<>("User Identity "+userDetailsDto.getAadharNumber()+" is added.", HttpStatus.CREATED);
    }

    @GetMapping(value = "/get-user/{data}")
    public ResponseEntity<?> getUser(@PathVariable String data){
        try {
            UserDetailsDto userDetailsDto=userService.getUser(data);
            return ResponseEntity.status(HttpStatus.OK).body(userDetailsDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
