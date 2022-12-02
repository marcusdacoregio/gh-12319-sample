package com.example.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticatedUser extends User {

	private static final long serialVersionUID = 1L;

	private final Set<String> roles = new HashSet<>();

	private String firstName;
	private String lastName;

	public AuthenticatedUser(String username, Collection<String> roles, Collection<? extends GrantedAuthority> authorities) {
		super(username, "", authorities);
		this.roles.addAll(roles);
	}

	public Set<String> getRoles() {
		return roles;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
