package com.klizo.RoleBasedRestApi.dto;

import com.klizo.RoleBasedRestApi.model.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Role role;

}
