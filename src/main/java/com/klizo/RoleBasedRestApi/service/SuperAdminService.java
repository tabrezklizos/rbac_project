package com.klizo.RoleBasedRestApi.service;

import com.klizo.RoleBasedRestApi.model.Role;
import com.klizo.RoleBasedRestApi.model.User;
import com.klizo.RoleBasedRestApi.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SuperAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Get the currently logged-in user and ensure they are a Super Admin
    private User getLoggedInSuperAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found."));

        if (loggedInUser.getRole() != Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("Only Super Admins can perform this action.");
        }

        return loggedInUser;
    }

    // ✅ Super Admin can create Admins and Users (but NOT other Super Admins)
    public User createUser(User user) {
        getLoggedInSuperAdmin(); // Ensure only Super Admins can create users

        if (user.getRole() == Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("Super Admin cannot create other Super Admins.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // ✅ Super Admin can get all Users & Admins (excluding other Super Admins)
    public List<User> getAllUsers() {
        getLoggedInSuperAdmin(); // Ensure only Super Admins can fetch users
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() != Role.SUPER_ADMIN)
                .toList();
    }

    // ✅ Super Admin can fetch any User/Admin by ID (excluding other Super Admins)
    public User getUserById(Integer id) {
        getLoggedInSuperAdmin(); // Ensure only Super Admins can fetch users
        return userRepository.findById(id)
                .filter(user -> user.getRole() != Role.SUPER_ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("User not found or cannot be accessed."));
    }

    // ✅ Super Admin can update any User/Admin (but NOT other Super Admins)
    public User updateUser(Integer id, User updatedUser) {
        getLoggedInSuperAdmin(); // Ensure only Super Admins can update users
        User existingUser = getUserById(id);

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setRole(updatedUser.getRole()); // ✅ Super Admin can change roles

        return userRepository.save(existingUser);
    }

    // ✅ Super Admin can delete any User/Admin (but NOT other Super Admins)
    public void deleteUser(Integer id) {
        getLoggedInSuperAdmin(); // Ensure only Super Admins can delete users
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
