package com.example.core;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	public UserDTO findUserById(String userId) {
		return new UserDTO(userId, "John", "Smith");
	}

}
