package com.klizo.RoleBasedRestApi.controller;

import com.klizo.RoleBasedRestApi.model.User;
import com.klizo.RoleBasedRestApi.service.SuperAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/superadmin")
@PreAuthorize("hasRole('SUPER_ADMIN')") // ✅ Only SUPER ADMIN can access these endpoints
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    // ✅ Super Admin can create Admins and Users (not other Super Admins)
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(superAdminService.createUser(user));
    }

    // ✅ Super Admin can get all users (including Admins but not Super Admins)
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(superAdminService.getAllUsers());
    }

    // ✅ Super Admin can fetch any user by ID (excluding other Super Admins)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(superAdminService.getUserById(id));
    }

    // ✅ Super Admin can update any user (excluding other Super Admins)
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        return ResponseEntity.ok(superAdminService.updateUser(id, user));
    }

    // ✅ Super Admin can delete any user (excluding other Super Admins)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        superAdminService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
