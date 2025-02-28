package com.booknetwork.api.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.webauthn.api.AuthenticatorResponse;
import org.springframework.stereotype.Service;

import com.booknetwork.api.email.EmailService;
import com.booknetwork.api.email.EmailTemplateName;
import com.booknetwork.api.role.Role;
import com.booknetwork.api.role.RoleRepository;
import com.booknetwork.api.security.JwtService;
import com.booknetwork.api.user.Token;
import com.booknetwork.api.user.TokenRepository;
import com.booknetwork.api.user.User;
import com.booknetwork.api.user.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@Value("${application.mailing.frontend.activation-url}")
	private String activationUrl;

	@Override
	public AuthenticationResponse register(RegistrationRequest request) throws MessagingException {
		// get USER role
		Role userRole = roleRepository.findByName("USER")
				.orElseThrow(() -> new RuntimeException("User role not found."));

		// create new user
		User user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).accountLocked(false)
				.accountEnabled(false).roles(Set.of(userRole)).build();
		userRepository.save(user);

		// send validation message
		sendValidationEmail(user);
		return null;
	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		Map<String, Object> claims = new HashMap<String, Object>();
		User user = (User) authenticate.getPrincipal();
		claims.put("fullname", user.getFullName());

		String accessToken = this.jwtService.generateAccessToken(claims, user);
		String refreshToken = this.jwtService.generateRefreshToken(claims, user);

		return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();

	}

	@Override
	// @Transactional
	public void confirm(String token) throws MessagingException {
		Token savedToken = tokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Token not found."));
		if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
			sendValidationEmail(savedToken.getUser());
			throw new RuntimeException("Token is already expired... A new token has been sent to your email address.");
		}
		if (savedToken.isValidated()) {
			throw new RuntimeException("This Token has already been validated.");
		}

		// update user status and set validated flag to true and validated at timestamp
		savedToken.setValidated(true);
		savedToken.setValidatedAt(LocalDateTime.now());
		tokenRepository.save(savedToken);

		// enable user account
		User user = this.userRepository.findById(savedToken.getUser().getId())
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));
		user.setAccountEnabled(true);
		userRepository.save(user);
	}

	private void sendValidationEmail(User user) throws MessagingException {
		var activationToken = generateAndSaveActivationToken(user);
		// send email with activation link
		emailService.sendEmail(user.getEmail(), "Account Activation: Book Social Network", user.getFullName(),
				EmailTemplateName.ACTIVATE_ACCOUNT, activationUrl, activationToken);

	}

	private String generateAndSaveActivationToken(User user) {
		// generate token
		String generatedToken = generateActivationCode(6);
		// create token object
		Token tokenEntity = Token.builder().token(generatedToken).user(user).createdAt(LocalDateTime.now())
				.expiresAt(LocalDateTime.now().plusMinutes(5)).validated(false).build();
		// save token to database
		this.tokenRepository.save(tokenEntity);
		return generatedToken;
	}

	private String generateActivationCode(int lengthOfCode) {
		String characters = "0123456789";
		StringBuilder sb = new StringBuilder();
		SecureRandom secureRandom = new SecureRandom();
		for (int i = 0; i < lengthOfCode; i++) {
			int index = secureRandom.nextInt(characters.length());
			sb.append(characters.charAt(index));
		}
		return sb.toString();
	}

}
