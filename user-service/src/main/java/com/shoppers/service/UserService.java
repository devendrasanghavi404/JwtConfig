package com.shoppers.service;

import com.shoppers.dto.UserDto;
import com.shoppers.model.UserEntity;

import java.util.List;


public interface UserService {

    UserDto add(UserDto userDto);
    List<UserDto> getAll();
    UserDto deleteUser(Integer id);
}
