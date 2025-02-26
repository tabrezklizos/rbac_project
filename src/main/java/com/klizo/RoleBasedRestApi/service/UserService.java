package com.klizo.RoleBasedRestApi.service;

import com.klizo.RoleBasedRestApi.dto.UserDto;
import com.klizo.RoleBasedRestApi.exception.InvalidRoleException;
import com.klizo.RoleBasedRestApi.exception.UserNotFoundException;
import com.klizo.RoleBasedRestApi.model.User;
import com.klizo.RoleBasedRestApi.repository.UserRepository;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public UserDto getUserById(Integer id) {    	
        User user = userRepository.findById(id)
                .orElseThrow(() ->new UserNotFoundException("User with ID " + id + " not found"));
        return convertToDTO(user);
    }

    public UserDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        return convertToDTO(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public UserDto createUser(User user) {
    
    	  if (user.getRole().name().equals("SUPER_ADMIN")) {
    	        throw new InvalidRoleException("Admins are not allowed to create Super Admins.");
    	    }
    	    
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return convertToDTO(userRepository.save(user));
    }

    public UserDto updateUser(Integer userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());

        return convertToDTO(userRepository.save(user));
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    private UserDto convertToDTO(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getRole());
    }
}
