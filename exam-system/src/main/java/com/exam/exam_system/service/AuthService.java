package com.exam.exam_system.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private final RefreshTokenProperties refreshTokenProperties;

	@Transactional
	public AuthResponse login(@Valid LoginRequestDTO loginRequestDTO) {
		User user = userRepository.findByUsername(loginRequestDTO.getUsernameOrEmail())
				.or(() -> userRepository.findByEmail(loginRequestDTO.getUsernameOrEmail()))
				.orElseThrow(UserNotFoundException::new);

		if (!user.getIsActive()) {
			throw new UserDeactivatedException();
		}

		String accessToken = jwtService.generateToken(user);

		String refreshToken = generateRefreshToken();

		String hashedRefresh = hashToken(refreshToken);

		LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(refreshTokenProperties.getExpirationMinutes());

		tokenRepository.revokeAllRefreshTokensByUser(user.getUserId());

		tokenRepository.save(Token.builder().user(user).token(hashedRefresh).expired(false).revoked(false)
				.expiresAt(expiresAt).build());

		return new AuthResponse(accessToken, refreshToken);
	}

	@Transactional
	public UserResponseDTO register(@Valid CreateUserRequestDTO createUserRequestDTO) {
		User user = userMapper.toEntity(createUserRequestDTO);

		if (userRepository.existsByUsername(createUserRequestDTO.getUsername())) {
			throw new UsernameAlreadyExistsException();
		}
		if (userRepository.existsByEmail(createUserRequestDTO.getEmail())) {
			throw new EmailAlreadyExistsException();
		}

		if (createUserRequestDTO.getPhone() != null && userRepository.existsByPhone(createUserRequestDTO.getPhone())) {
			throw new PhoneAlreadyExistsException();
		}
		Department department = departmentRepository.findById(createUserRequestDTO.getDepartmentId())
				.orElseThrow(DepartmentNotFoundException::new);

		College college = collegeRepository.findById(createUserRequestDTO.getCollegeId())
				.orElseThrow(CollegeNotFoundException::new);

		Role role = roleRepository.findById(createUserRequestDTO.getRoleId()).orElseThrow(RoleNotFoundException::new);

		if (!department.getCollege().getCollegeId().equals(college.getCollegeId())) {
			throw new DepartmentCollegeMismatchException();
		}
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
		user.setRequestCodeExpiry(LocalDateTime.now().plusMinutes(10));

		User savedUser = userRepository.save(user);

		PasswordResetRequestedEvent passwordResetRequestedEvent = new PasswordResetRequestedEvent(savedUser.getUserId(),
				savedUser.getUsername(), savedUser.getEmail(), resetCode, LocalDateTime.now());
		rabbitTemplate.convertAndSend(AUTH_EXCHANGE, PASSWORD_RESET_KEY, passwordResetRequestedEvent);
	}

	public void resetPassword(@Valid ResetPasswordRequestDTO resetPasswordDTO) {
		User user = userRepository.findByEmail(resetPasswordDTO.getEmail()).orElseThrow(UserNotFoundException::new);

		if (user.getRequestCodeExpiry() == null || user.getRequestCodeExpiry().isBefore(LocalDateTime.now())) {
			throw new ExpiredResetCodeException();
		}

		if (!resetPasswordDTO.getCode().equals(user.getRequestCode())) {
			throw new InvalidResetCodeException();
		}

		user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
		user.setRequestCode(null);
		user.setRequestCodeExpiry(null);
		userRepository.save(user);
	}

	public void changePassword(User currentUser, @Valid UserChangePasswordRequestDTO changePasswordRequestDTO) {
		User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(() -> new UserNotFoundException());

		if (!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
			throw new InvalidCurrentPasswordException();
		}
		user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
		userRepository.save(user);
	}

	public void reGenerateCode(User currentUser) {
		User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(UserNotFoundException::new);

		String newCode = generateConfirmationCode();
		user.setRequestCode(newCode);
		user.setRequestCodeExpiry(LocalDateTime.now().plusMinutes(10));

		User savedUser = userRepository.save(user);

		CodeRegeneratedEvent codeRegeneratedEvent = new CodeRegeneratedEvent(savedUser.getEmail(),
				savedUser.getUsernameField(), newCode, LocalDateTime.now());
		rabbitTemplate.convertAndSend(AUTH_EXCHANGE, CODE_REGENERATED_KEY, codeRegeneratedEvent);
	}

	public String generateConfirmationCode() {
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

		String hashed = hashToken(refreshToken);

		Token storedToken = tokenRepository.findByToken(hashed)
				.orElseThrow(() -> new InvalidTokenException(Messages.INVALID_REFRESH_TOKEN));

		if (storedToken.isExpired() || storedToken.isRevoked()) {
			throw new InvalidTokenException(Messages.INVALID_REFRESH_TOKEN);
		}

		if (storedToken.isExpired() || storedToken.isRevoked()
				|| storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new InvalidTokenException(Messages.INVALID_REFRESH_TOKEN);
		}

		storedToken.setExpired(true);
		storedToken.setRevoked(true);
		tokenRepository.save(storedToken);

		User user = storedToken.getUser();

		String newAccessToken = jwtService.generateToken(user);
		String newRefreshToken = generateRefreshToken();

		tokenRepository.save(Token.builder().user(user).token(hashToken(newRefreshToken)).expired(false).revoked(false)
				.expiresAt(LocalDateTime.now().plusMinutes(refreshTokenProperties.getExpirationMinutes())).build());

		return new AuthResponse(newAccessToken, newRefreshToken);

	}

	@Transactional
	public void logoutByRequest(HttpServletRequest request) {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new InvalidTokenException(Messages.TOKEN_NOT_FOUND);
		}

		String refreshToken = authHeader.substring(7);
		String hash = hashToken(refreshToken);
		Token token = tokenRepository.findByToken(hash)
				.orElseThrow(() -> new InvalidTokenException(Messages.INVALID_REFRESH_TOKEN));

		token.setExpired(true);
		token.setRevoked(true);
		tokenRepository.save(token);

	}

	@Transactional
	public void logoutAllTokensForUser(Long userId) {
		List<Token> tokens = tokenRepository.findAllValidTokensByUser(userId);

		for (Token token : tokens) {
			token.setExpired(true);
			token.setRevoked(true);
			tokenRepository.save(token);
		}

		SecurityContextHolder.clearContext();
	}

	private String generateRefreshToken() {
		byte[] bytes = new byte[64];
		new SecureRandom().nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

	private String hashToken(String token) {
		return DigestUtils.sha256Hex(token);
	}

}
