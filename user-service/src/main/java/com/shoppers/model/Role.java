package com.shoppers.model;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Role {

    @Id
    private String roleId;
    private String roleName;
}
