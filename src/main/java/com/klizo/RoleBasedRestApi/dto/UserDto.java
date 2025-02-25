package com.klizo.RoleBasedRestApi.dto;

import com.klizo.RoleBasedRestApi.model.Role;

public class UserDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private Role role;

    public UserDto(Integer id, String firstName, String lastName, String username, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}
