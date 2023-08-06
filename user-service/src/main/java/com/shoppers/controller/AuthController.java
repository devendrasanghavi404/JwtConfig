package com.shoppers.controller;

import com.shoppers.dto.JwtRequestDto;
import com.shoppers.dto.JwtResponseDto;
import com.shoppers.dto.UserDto;
import com.shoppers.model.UserEntity;
import com.shoppers.repository.UserRepository;
import com.shoppers.security.JwtHelper;
import com.shoppers.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto jwtRequestDto){
        this.doAuthenticate(jwtRequestDto.getEmail(),jwtRequestDto.getPassword());
        //generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequestDto.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);
        //token to be stored
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        JwtResponseDto jwtResponseDto = JwtResponseDto.builder().jwtToken(token).user(userDto).build();
        return new ResponseEntity<>(jwtResponseDto, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,password);
        try{
            manager.authenticate(authentication);
        }
        catch (BadCredentialsException e){
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/createUser")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity createdUser = userRepository.save(user);
        return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
    }
}
