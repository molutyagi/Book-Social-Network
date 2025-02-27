package com.booknetwork.api.user;

import java.util.Optional;

public interface UserService {
	Optional<User> findByName(String name);
}
