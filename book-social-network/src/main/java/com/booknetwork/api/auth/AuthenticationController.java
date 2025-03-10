package com.booknetwork.api.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

	private final AuthenticationService authService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
		authService.register(request);
		return ResponseEntity.accepted().body(
				"Account created successfully. An Account Activation email has been sent to you. Please check your mail.");
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request)
			throws MessagingException {
		return ResponseEntity.ok(this.authService.authenticate(request));
	}

	@GetMapping("/activate-account")
	public ResponseEntity<String> confirm(@RequestParam String token) throws MessagingException {
		return ResponseEntity.ok(this.authService.confirm(token));
	}

}
