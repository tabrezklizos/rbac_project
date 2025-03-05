package com.klizo.RoleBasedRestApi.service;

import com.klizo.RoleBasedRestApi.dto.UserDto;
import com.klizo.RoleBasedRestApi.exception.InvalidRoleException;
import com.klizo.RoleBasedRestApi.exception.UserExistException;
import com.klizo.RoleBasedRestApi.exception.UserNotFoundException;
import com.klizo.RoleBasedRestApi.model.User;
import com.klizo.RoleBasedRestApi.repository.UserRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;

    }


    public UserDto getUserById(Integer userId) {

        // creating for redis cache
        final String key = "user_"+userId;
        final ValueOperations<String,Object>operations = redisTemplate.opsForValue();
        final boolean hasKey = redisTemplate.hasKey(key);

        if(hasKey){
            final UserDto user = (UserDto)operations.get(key);
            return user;
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new UserNotFoundException("User with ID " + userId + " not found"));

        UserDto userResponse= convertToDTO(user);
        operations.set(key,userResponse,600,TimeUnit.SECONDS);

        return userResponse;
    }

    public UserDto getUserProfile(String username) {

        final String key =username;
        final ValueOperations<String,Object>operations=redisTemplate.opsForValue();
        final boolean hasKey=redisTemplate.hasKey(key);

        if(hasKey){
            final UserDto user = (UserDto) operations.get(key);
            return user;
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        UserDto userResponse = convertToDTO(user);
        operations.set(key, userResponse, 600, TimeUnit.SECONDS);

        return userResponse;
    }
    @Cacheable(value = "usersValue",key="#userId")
    public List<UserDto> getAllUsers() {
      /*  final String key = "allUsers";
        final ValueOperations<String, Object>operations=redisTemplate.opsForValue();
        final boolean hasKey = redisTemplate.hasKey(key);
*/
//        if(hasKey){
//          final List<UserDto> listOfUsers = (List<UserDto>)operations.get(key);
//          return listOfUsers;
//        }

        List<UserDto> listOfUsers = userRepository.findAll().stream().map(this::convertToDTO).toList();

        operations.set(key, listOfUsers, 10, TimeUnit.MINUTES);

        return listOfUsers;
    }

    /*public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream().map(this::convertToDTO).toList();
    }*/

//    @CachePut(value="userValue",key = "#user.id")
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

    public UserDto updateUser(Integer userId, User updatedUser) {
        final String key ="user_"+userId;
        final boolean hasKey = redisTemplate.hasKey(key);

        if(hasKey){
            redisTemplate.delete(key);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new UserNotFoundException("User with ID " + userId + " not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());

        return convertToDTO(userRepository.save(user));
    }

    public void deleteUser(Integer userId) {
        final String key ="user_"+userId;
        final boolean hasKey= redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
        }

        if(getUserById(userId)!=null) {
            userRepository.deleteById(userId);
        }
    }
//userDto:userResponse
    private UserDto convertToDTO(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getRole());
    }
}
