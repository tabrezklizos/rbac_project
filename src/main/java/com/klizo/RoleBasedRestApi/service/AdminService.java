package com.klizo.RoleBasedRestApi.service;

import com.klizo.RoleBasedRestApi.model.Role;
import com.klizo.RoleBasedRestApi.model.User;
import com.klizo.RoleBasedRestApi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Helper method to get the logged-in user
    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found."));
    }

    // ✅ Admin can create a USER (not Admin/Super Admin)
    public User createUser(User user) {
        User loggedInUser = getLoggedInUser();

        if (loggedInUser.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only Admins can create users.");
        }

        if (user.getRole() != Role.USER) {
            throw new IllegalArgumentException("Admin can only create users with role USER.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // ✅ Admin can only view USERS (not Admin/Super Admin)
    public List<User> getAllUsers() {
        validateAdminAccess();
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.USER)
                .toList();
    }

    // ✅ Admin can get a USER by ID (not Admin/Super Admin)
    public User getUserById(Integer id) {
        validateAdminAccess();
        return userRepository.findById(id)
                .filter(user -> user.getRole() == Role.USER)
                .orElseThrow(() -> new IllegalArgumentException("User not found or cannot be accessed."));
    }

    // ✅ Admin can update a USER (not Admin/Super Admin)
    public User updateUser(Integer id, User updatedUser) {
        validateAdminAccess();
        User existingUser = getUserById(id);
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        return userRepository.save(existingUser);
    }

    // ✅ Admin can delete a USER (not Admin/Super Admin)
    public void deleteUser(Integer id) {
        validateAdminAccess();
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // ✅ Helper method to validate if the logged-in user is an Admin
    private void validateAdminAccess() {
        if (getLoggedInUser().getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only Admins can perform this action.");
        }
    }
}
