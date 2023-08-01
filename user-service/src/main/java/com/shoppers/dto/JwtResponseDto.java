package com.shoppers.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtResponseDto {

    private String jwtToken;
    private UserDto user;
}
