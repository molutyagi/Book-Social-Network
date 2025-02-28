package com.booknetwork.api.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public Optional<User> findByName(String email) {
		return this.userRepository.findByEmail(email);
	}

	// Implement user-related business logic here
}
