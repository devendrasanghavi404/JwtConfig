package com.shoppers.controller;

import com.shoppers.dto.UserDto;
import com.shoppers.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/api")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "user")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        logger.info("UserController:start");
        return new ResponseEntity<>(userService.add(userDto), HttpStatus.OK);
    }

    @GetMapping(value = "users")
    public ResponseEntity<List<UserDto>> getAllUser(){
        return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "user/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Integer id){
        return new ResponseEntity<>(userService.deleteUser(id),HttpStatus.GONE);
    }

}
