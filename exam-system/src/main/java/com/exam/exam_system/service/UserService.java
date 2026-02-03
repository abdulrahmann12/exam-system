package com.exam.exam_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.*;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.events.EmailChangeEvent;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.mapper.UserMapper;
import com.exam.exam_system.repository.*;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static com.exam.exam_system.rabbitconfig.RabbitConstants.*;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final CollegeRepository collegeRepository;
	private final DepartmentRepository departmentRepository;
	private final UserMapper userMapper;
	private final RabbitTemplate rabbitTemplate;
	private final AuthService authService;

	@Transactional(readOnly = true)
	public UserProfileResponseDTO getProfile(User user) {
		User freshUser = userRepository.findById(user.getUserId()).orElseThrow(UserNotFoundException::new);

		return userMapper.toUserProfileResponseDTO(freshUser);
	}

	@Transactional
	public UserProfileResponseDTO updateProfile(User user, @Valid UserUpdateProfileRequestDTO dto) {

		if (dto.getPhone() != null && userRepository.existsByPhoneAndUserIdNot(dto.getPhone(), user.getUserId())) {
			throw new PhoneAlreadyExistsException();
		}

		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setPhone(dto.getPhone());

		return userMapper.toUserProfileResponseDTO(userRepository.save(user));
	}

	@Transactional
	public void changeUsername(User user, @Valid ChangeUsernameRequestDTO dto) {

		if (dto.getNewUsername().equals(user.getUsername())) {
			return;
		}

		if (userRepository.existsByUsernameAndUserIdNot(dto.getNewUsername(), user.getUserId())) {
			throw new UsernameAlreadyExistsException();
		}

		user.setUsername(dto.getNewUsername());
		userRepository.save(user);

		authService.logoutAllTokensForUser(user.getUserId());
	}

	@Transactional
	public void requestEmailChange(User user, @Valid ChangeEmailRequestDTO dto) {

		if (userRepository.existsByEmail(dto.getNewEmail())) {
			throw new EmailAlreadyExistsException();
		}

		if (user.getRequestCode() != null && user.getRequestCodeExpiry() != null
				&& user.getRequestCodeExpiry().isAfter(LocalDateTime.now())) {
			throw new TooManyRequestsException();
		}

		String code = authService.generateConfirmationCode();

		user.setRequestCode(code);
		user.setPendingEmail(dto.getNewEmail());
		user.setRequestCodeExpiry(LocalDateTime.now().plusMinutes(10));
		userRepository.save(user);

		rabbitTemplate.convertAndSend(AUTH_EXCHANGE, EMAIL_CHANGE_KEY,
				new EmailChangeEvent(dto.getNewEmail(), user.getUsernameField(), code));
	}

	@Transactional
	public void confirmEmailChange(User user, @Valid VerifyEmailChangeRequestDTO dto) {

		if (user.getRequestCode() == null || user.getPendingEmail() == null) {
			throw new InvalidEmailChangeRequestException();
		}

		if (!dto.getCode().equals(user.getRequestCode())) {
			throw new InvalidVerificationCodeException();
		}

		if (user.getRequestCodeExpiry().isBefore(LocalDateTime.now())) {
			throw new VerificationCodeExpiredException();
		}

		user.setEmail(user.getPendingEmail());
		user.setPendingEmail(null);
		user.setRequestCode(null);
		user.setRequestCodeExpiry(null);

		userRepository.save(user);

		authService.logoutAllTokensForUser(user.getUserId());
	}

	@Transactional(readOnly = true)
	public UserResponseDTO getUserById(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		return userMapper.toDTO(user);
	}

	@Transactional(readOnly = true)
	public Page<UserResponseDTO> getAllUsers(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("username"));

		return userRepository.findAll(pageable).map(userMapper::toDTO);
	}

	@Transactional
	public void deactivateUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		if (!user.getIsActive()) {
			throw new UserAlreadyDeactivatedException();
		}
		user.setIsActive(false);
		userRepository.save(user);
	}

	@Transactional
	public UserResponseDTO adminUpdateUser(Long userId, @Valid AdminUserUpdateRequestDTO dto) {

		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(RoleNotFoundException::new);

		College college = dto.getCollegeId() == null ? null
				: collegeRepository.findById(dto.getCollegeId()).orElseThrow(CollegeNotFoundException::new);

		Department department = dto.getDepartmentId() == null ? null
				: departmentRepository.findById(dto.getDepartmentId()).orElseThrow(DepartmentNotFoundException::new);

		if (!department.getCollege().getCollegeId().equals(college.getCollegeId())) {
			throw new DepartmentCollegeMismatchException();
		}
		user.setRole(role);
		user.setCollege(college);
		user.setDepartment(department);
		user.setIsActive(dto.getIsActive());

		return userMapper.toDTO(userRepository.save(user));
	}

	@Transactional(readOnly = true)
	public Page<UserResponseDTO> getUsersByRole(String roleName, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("role.roleName"));

		return userRepository.findByRole_RoleName(roleName, pageable).map(userMapper::toDTO);
	}

	@Transactional(readOnly = true)
	public Page<UserResponseDTO> getUsersByCollege(Long collegeId, int page, int size) {

		if (!collegeRepository.existsById(collegeId)) {
			throw new CollegeNotFoundException();
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("username"));

		return userRepository.findByCollege_CollegeId(collegeId, pageable).map(userMapper::toDTO);
	}

	@Transactional(readOnly = true)
	public Page<UserResponseDTO> getUsersByDepartment(Long departmentId, int page, int size) {

		if (!departmentRepository.existsById(departmentId)) {
			throw new DepartmentNotFoundException();
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("username"));

		return userRepository.findByDepartment_DepartmentId(departmentId, pageable).map(userMapper::toDTO);
	}

	@Transactional(readOnly = true)
	public Page<UserResponseDTO> searchUsers(String keyword, String role, Long collegeId, Long departmentId,
			Boolean isActive, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("username"));

		return userRepository.searchUsers(keyword, role, collegeId, departmentId, isActive, pageable)
				.map(userMapper::toDTO);
	}

}
