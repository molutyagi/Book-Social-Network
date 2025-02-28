package com.booknetwork.api.auth;

import jakarta.mail.MessagingException;

public interface AuthenticationService {

    AuthenticationResponse register(RegistrationRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void confirm(String token) throws MessagingException;

}
