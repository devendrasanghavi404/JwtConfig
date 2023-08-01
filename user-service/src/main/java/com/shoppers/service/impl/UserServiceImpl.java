package com.shoppers.service.impl;

import com.shoppers.dto.RoleDto;
import com.shoppers.dto.UserDto;
import com.shoppers.model.Role;
import com.shoppers.model.UserEntity;
import com.shoppers.repository.RoleRepository;
import com.shoppers.repository.UserRepository;
import com.shoppers.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${normal.role.id}")
    private String normalRoleId;

    @Override
    public UserDto add(UserDto userDto) {
        UserEntity user = new UserEntity();
        user.setUserId(userDto.getUserId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.getRole();
        //fetch role and set to User Entity
        Role role = roleRepository.findById(normalRoleId).get();
        user.getRole().add(role);
        UserEntity saveUser = userRepository.save(user);
        userDto.setUserId(saveUser.getUserId());
        //map roles to roleDto object
        Set<RoleDto> roleDto = saveUser.getRole().stream().map((element)->modelMapper.map(element,RoleDto.class)).collect(Collectors.toSet());
        userDto.setRole(roleDto);
        return userDto;
    }

    @Override
    public List<UserDto> getAll() {
        List<UserEntity> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        UserDto userDto;
        for(UserEntity user : userList){
            userDto = new UserDto();
            userDto.setUserId(user.getUserId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            //set role
            Set<RoleDto> roleDto = user.getRole().stream().map(role-> new RoleDto(role.getRoleId(),role.getRoleName())).collect(Collectors.toSet());
            userDto.setRole(roleDto);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public UserDto deleteUser(Integer id) {
        // Step 1: Check if the user exists with the provided id
        Optional<UserEntity> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            // Step 2: Delete the user from the database
            userRepository.deleteById(id);

            // Step 3: Map the deleted user to UserDto
            UserEntity deletedUser = optionalUser.get();
            UserDto deletedUserDto = new UserDto();
            deletedUserDto.setUserId(deletedUser.getUserId());
            deletedUserDto.setFirstName(deletedUser.getFirstName());
            deletedUserDto.setLastName(deletedUser.getLastName());
            deletedUserDto.setEmail(deletedUser.getEmail());
            deletedUserDto.setPassword(deletedUser.getPassword());

            // Step 4: Map roles to RoleDto object
            Set<RoleDto> roleDto = deletedUser.getRole().stream()
                    .map(role -> new RoleDto(role.getRoleId(), role.getRoleName()))
                    .collect(Collectors.toSet());
            deletedUserDto.setRole(roleDto);

            return deletedUserDto;
        } else {
            // If the user doesn't exist with the provided id, return null or throw an exception.
            return null;
        }
    }
}
