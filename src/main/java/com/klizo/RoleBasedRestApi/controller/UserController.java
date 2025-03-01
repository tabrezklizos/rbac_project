package com.klizo.RoleBasedRestApi.controller;

import com.klizo.RoleBasedRestApi.dto.UserDto;
import com.klizo.RoleBasedRestApi.model.User;
import com.klizo.RoleBasedRestApi.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getUserProfile(username));
    }
}
