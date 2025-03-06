package com.klizo.RoleBasedRestApi.service;

import java.util.Optional;

import com.klizo.RoleBasedRestApi.model.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klizo.RoleBasedRestApi.model.AuthenticationResponse;
import com.klizo.RoleBasedRestApi.model.Role;
import com.klizo.RoleBasedRestApi.repository.UserRepository;

@Service
public class AuthenticationService {
 
	
	private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
	
    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

    //for new user registration
	public AuthenticationResponse register(UserEntity request) {

		UserEntity user = new UserEntity();
    	user.setFirstName(request.getFirstName());
    	user.setLastName(request.getLastName());
    	user.setUsername(request.getUsername());
    	user.setPassword(passwordEncoder.encode(request.getPassword()));
    	
    	user.setRole(Role.USER);
    	
    	user=repository.save(user);
    	
    	String token = jwtService.generateToken(user);
    	
    	return new AuthenticationResponse(token);
    	
    }
	
	//for login
	
	public AuthenticationResponse authenticate(UserEntity request) {
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(), 
						request.getPassword()
						)
				);

		UserEntity user=repository.findByUsername(request.getUsername()).orElseThrow();
		String token = jwtService.generateToken(user);
		
		return new AuthenticationResponse(token);
	}
	
	
	
    
}
















