package com.shoppers.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleDto {

    private String roleId;
    private String roleName;
}
