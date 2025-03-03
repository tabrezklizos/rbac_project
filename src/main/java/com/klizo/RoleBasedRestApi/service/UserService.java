package com.klizo.RoleBasedRestApi.service;

import com.klizo.RoleBasedRestApi.dto.UserDto;
import com.klizo.RoleBasedRestApi.exception.InvalidRoleException;
import com.klizo.RoleBasedRestApi.exception.UserExistException;
import com.klizo.RoleBasedRestApi.exception.UserNotFoundException;
import com.klizo.RoleBasedRestApi.model.User;
import com.klizo.RoleBasedRestApi.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    
    @Autowired
    private RedisService redisService;


   @Cacheable(value = "userValue",key="#userId")
    public UserDto getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new UserNotFoundException("User with ID " + userId + " not found"));
        return convertToDTO(user);
    }

    public UserDto getUserProfile(String username) {
    	
    	UserDto userResponse;

        userResponse=redisService.get(username,UserDto.class);

    	if(userResponse!=null) {
    		return userResponse;
    	}
    	else {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

           userResponse = convertToDTO(user);

        if(userResponse!=null) {
        	redisService.set(username, userResponse, 300L);
        }
        
        return userResponse;
    	}
    }
    //@Cacheable(value = "usersValue",key="#userId")
    /*public List<UserDto> getAllUsers() {
          redisService.getAll();


        List<UserDto> listOfUsers = userRepository.findAll().stream().map(this::convertToDTO).toList();


        return null;


    }*/
    @CachePut(value="userValue",key = "#user.id")
    public UserDto createUser(User user) {

        if(getUserProfile(user.getUsername())!=null){
            throw new UserExistException("Already user existed with username");
        }

    	  if (user.getRole().name().equals("SUPER_ADMIN")) {
    	        throw new InvalidRoleException("Admins are not allowed to create Super Admins.");
    	    }
    	    
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return convertToDTO(userRepository.save(user));
    }

    @CachePut(value="userValue",key="#userId")
    public UserDto updateUser(Integer userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new UserNotFoundException("User with ID " + userId + " not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());

        return convertToDTO(userRepository.save(user));
    }

    @CacheEvict(value="userValue",key = "#userId")
    public void deleteUser(Integer userId) {
        if(getUserById(userId)!=null) {
            userRepository.deleteById(userId);
        }
    }

    private UserDto convertToDTO(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getRole());
    }
}
