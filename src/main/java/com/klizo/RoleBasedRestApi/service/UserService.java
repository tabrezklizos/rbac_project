package com.klizo.RoleBasedRestApi.service;

import java.util.List;

import com.klizo.RoleBasedRestApi.dto.UserRequest;
import com.klizo.RoleBasedRestApi.model.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klizo.RoleBasedRestApi.dto.UserResponse;
import com.klizo.RoleBasedRestApi.exception.InvalidRoleException;
import com.klizo.RoleBasedRestApi.exception.UserExistException;
import com.klizo.RoleBasedRestApi.exception.UserNotFoundException;
import com.klizo.RoleBasedRestApi.repository.UserRepository;


@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	
	}

	@Cacheable(value = "usersValue", key = "'allUsers'")
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(this::convertToDTO).toList();
	}

	@Cacheable(value = "userValue",key="#userId")
	public UserResponse getUserById(Integer userId) {

		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

		UserResponse userResponse = convertToDTO(user);
	
		return userResponse;
	}



    @Cacheable(value = "userValue",key="#userName")
    public UserResponse getUserProfile(String username) {
		UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        return convertToDTO(user);
    }

	@Cacheable(value="userValue",key = "#user.id")
	public UserResponse createUser(UserRequest user) {
		if (getUserProfile(user.getUsername()) != null) {
			throw new UserExistException("Already user existed with username");
		}
		if (user.getRole().name().equals("SUPER_ADMIN")) {
			throw new InvalidRoleException("Admins are not allowed to create Super Admins.");
		}
		// Convert DTO to Entity
		UserEntity userEntity = convertToEntity(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return convertToDTO(userRepository.save(userEntity));
	}

    
    @CachePut(value="userValue",key="#userId")
    public UserResponse updateUser(Integer userId, UserRequest updatedUser) {
		UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        return convertToDTO(userRepository.save(user));
    }

    
    @CacheEvict(value="userValue",key = "#userId")
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
        if(getUserById(userId)!=null) {
            userRepository.deleteById(userId);
        }
    }

	//BeanUtils
//userDto:userResponse

	private UserResponse convertToDTO(UserEntity user) {
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(user, userResponse);
		return userResponse;
	}

	private UserEntity convertToEntity(UserRequest userRequest) {
		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(userRequest, user);
		return user;
	}

}
