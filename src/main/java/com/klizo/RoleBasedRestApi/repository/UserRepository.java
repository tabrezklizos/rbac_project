package com.klizo.RoleBasedRestApi.repository;

import java.util.Optional;

import com.klizo.RoleBasedRestApi.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	Optional<UserEntity>findByUsername(String username);

}
