package com.shoppers.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtRequestDto {

    private String email;
    private String password;

}
