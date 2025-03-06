package com.klizo.RoleBasedRestApi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.klizo.RoleBasedRestApi.model.AuthenticationResponse;
import com.klizo.RoleBasedRestApi.model.UserEntity;
import com.klizo.RoleBasedRestApi.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

	private final AuthenticationService authService;

	public AuthenticationController(AuthenticationService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(
			@Valid @RequestBody UserEntity request
			) {
		
		return ResponseEntity.ok(authService.register(request));
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(
		 @RequestBody UserEntity request
			) {
		
		return ResponseEntity.ok(authService.authenticate(request));
	}
	
	
	
}
