package com.klizo.RoleBasedRestApi.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="user")
public class UserEntity implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
    @NotBlank(message = "First name cannot be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
	@Column(name="first_name")
	private String firstName;
	
    @NotBlank(message = "Last name cannot be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
	@Column(name="last_name")
	private String lastName;
	
    @NotBlank(message = "Username cannot be blank")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{4,15}$", 
             message = "Username must start with a letter and be 5-16 characters long")
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="role")
	@Enumerated(value=EnumType.STRING)
	private Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return List.of(new SimpleGrantedAuthority(role.name()));
	}



}