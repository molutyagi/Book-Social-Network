package com.booknetwork.api.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public Optional<User> findByName(String name) {
		return this.userRepository.findByName(name);
	}
	
	// Implement user-related business logic here
}
