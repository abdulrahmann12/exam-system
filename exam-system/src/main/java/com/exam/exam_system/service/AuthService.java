package com.exam.exam_system.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.*;
import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.events.CodeRegeneratedEvent;
import com.exam.exam_system.events.PasswordResetRequestedEvent;
import com.exam.exam_system.events.UserRegisteredEvent;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.mapper.*;
import com.exam.exam_system.repository.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static com.exam.exam_system.rabbitconfig.RabbitConstants.*;

@RequiredArgsConstructor
@Service
@Validated
public class AuthService {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final DepartmentRepository departmentRepository;
	private final CollegeRepository collegeRepository;
	private final RoleRepository roleRepository;
	private final RabbitTemplate rabbitTemplate;
	private final TokenRepository tokenRepository;

	public AuthResponse login(@Valid LoginRequestDTO loginRequestDTO) {
		User user = userRepository.findByUsername(loginRequestDTO.getUsernameOrEmail())
				.or(() -> userRepository.findByEmail(loginRequestDTO.getUsernameOrEmail()))
				.orElseThrow(UserNotFoundException::new);

		String jwtToken = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		jwtService.revokeAllUserTokens(user);
		jwtService.saveUserToken(user, jwtToken);
		return new AuthResponse(jwtToken, refreshToken);
	}

	@Transactional
	public UserResponseDTO register(@Valid CreateUserRequestDTO createUserRequestDTO) {
		User user = userMapper.toEntity(createUserRequestDTO);

		if (userRepository.existsByUsername(createUserRequestDTO.getUsername())) {
			throw new EmailAlreadyExistsException();
		}
		if (userRepository.existsByEmail(createUserRequestDTO.getEmail())) {
			throw new EmailAlreadyExistsException();
		}

		Department department = departmentRepository.findById(createUserRequestDTO.getDepartmentId())
				.orElseThrow(DepartmentNotFoundException::new);

		College college = collegeRepository.findById(createUserRequestDTO.getCollegeId())
				.orElseThrow(CollegeNotFoundException::new);

		Role role = roleRepository.findById(createUserRequestDTO.getRoleId()).orElseThrow(RoleNotFoundException::new);

		user.setDepartment(department);
		user.setCollege(college);
		user.setRole(role);
		user.setPassword(passwordEncoder.encode(createUserRequestDTO.getPassword()));

		User savedUser = userRepository.save(user);

		UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent(savedUser.getUserId(), savedUser.getEmail(),
				savedUser.getFirstName(), LocalDateTime.now());
		rabbitTemplate.convertAndSend(AUTH_EXCHANGE, USER_REGISTERED_KEY, userRegisteredEvent);
		return userMapper.toDTO(savedUser);
	}

	public void forgetPassword(@Valid EmailRequestDTO emailRequestDTO) {
		User user = userRepository.findByEmail(emailRequestDTO.getEmail()).orElseThrow(UserNotFoundException::new);

		String resetCode = generateConfirmationCode();
		user.setRequestCode(resetCode);

		User savedUser = userRepository.save(user);

		PasswordResetRequestedEvent passwordResetRequestedEvent = new PasswordResetRequestedEvent(savedUser.getUserId(),
				savedUser.getUsername(), savedUser.getEmail(), resetCode, LocalDateTime.now());
		rabbitTemplate.convertAndSend(AUTH_EXCHANGE, PASSWORD_RESET_KEY, passwordResetRequestedEvent);
	}

	public void resetPassword(@Valid ResetPasswordRequestDTO resetPasswodDTO) {
		User user = userRepository.findByEmail(resetPasswodDTO.getEmail()).orElseThrow(UserNotFoundException::new);

		if (!resetPasswodDTO.getCode().equals(user.getRequestCode())) {
			throw new InvalidResetCodeException();
		}
		user.setPassword(passwordEncoder.encode(resetPasswodDTO.getNewPassword()));
		user.setRequestCode(null);
		userRepository.save(user);
	}

	public void changePassword(String email, @Valid UserChangePasswordRequestDTO changePasswordRequestDTO) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());

		if (!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
			throw new InvalidCurrentPasswordException();
		}
		user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
		userRepository.save(user);
	}

	public void reGenerateCode(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

		String newCode = generateConfirmationCode();
		user.setRequestCode(newCode);

		User savedUser = userRepository.save(user);

		CodeRegeneratedEvent codeRegeneratedEvent = new CodeRegeneratedEvent(savedUser.getEmail(),
				savedUser.getUsernameField(), newCode);
		rabbitTemplate.convertAndSend(AUTH_EXCHANGE, CODE_REGENERATED_KEY, codeRegeneratedEvent);
	}

	private String generateConfirmationCode() {
		SecureRandom random = new SecureRandom();
		int code = 10000 + random.nextInt(90000);
		return String.valueOf(code);
	}

	public AuthResponse refreshToken(HttpServletRequest request) {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new InvalidTokenException(Messages.INVALID_REFRESH_TOKEN);
		}

		final String refreshToken = authHeader.substring(7);
		final String userEmail = jwtService.extractUsername(refreshToken);

		if (userEmail == null) {
			throw new InvalidTokenException(Messages.INVALID_REFRESH_TOKEN);
		}

		User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);

		if (!jwtService.validateToken(refreshToken, user)) {
			throw new InvalidTokenException(Messages.INVALID_REFRESH_TOKEN);
		}

		String accessToken = jwtService.generateToken(user);
		jwtService.revokeAllUserTokens(user);
		jwtService.saveUserToken(user, accessToken);
		return new AuthResponse(accessToken, refreshToken);
	}

	@Transactional
	public void logout(String token) {
		var storedToken = tokenRepository.findByToken(token).orElse(null);
		if (storedToken != null && !storedToken.isExpired() && !storedToken.isRevoked()) {
			storedToken.setExpired(true);
			storedToken.setRevoked(true);
			tokenRepository.save(storedToken);
		} else {
			throw new InvalidTokenException(Messages.ALREADY_LOGGED_OUT);
		}
	}

	@Transactional
	public void logoutByRequest(HttpServletRequest request) {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new InvalidTokenException(Messages.TOKEN_NOT_FOUND);
		}

		final String token = authHeader.substring(7);
		logout(token);
	}
}
