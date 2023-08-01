package com.shoppers.dto;

import com.shoppers.model.Role;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<RoleDto> role = new HashSet<>();
}
