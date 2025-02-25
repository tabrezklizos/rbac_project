package com.klizo.RoleBasedRestApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klizo.RoleBasedRestApi.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User>findByUsername(String username);

}
