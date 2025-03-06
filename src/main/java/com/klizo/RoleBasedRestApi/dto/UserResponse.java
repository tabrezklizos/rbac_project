package com.klizo.RoleBasedRestApi.dto;

import com.klizo.RoleBasedRestApi.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private Role role;

}
