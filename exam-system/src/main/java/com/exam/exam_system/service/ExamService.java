package com.exam.exam_system.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.*;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.mapper.*;
import com.exam.exam_system.repository.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class ExamService {

	private final ExamRepository examRepository;
	private final CollegeRepository collegeRepository;
	private final DepartmentRepository departmentRepository;
	private final SubjectRepository subjectRepository;
	private final UserRepository userRepository;

	private final ExamMapper examMapper;
	private final QuestionMapper questionMapper;
	private final ChoiceMapper choiceMapper;

	private final UserService userService;
	private final QrCodeService qrCodeService;
	private final ImageService imageService;

	private final QrProperties qrProperties;

	@Transactional
	public ExamResponseDTO createExam(@Valid CreateExamRequestDTO dto) {
		College college = collegeRepository.findById(dto.getCollegeId())
				.orElseThrow(() -> new CollegeNotFoundException());

		Department department = departmentRepository.findById(dto.getDepartmentId())
				.orElseThrow(() -> new DepartmentNotFoundException());

		Subject subject = subjectRepository.findById(dto.getSubjectId())
				.orElseThrow(() -> new SubjectNotFoundException());

		if (!department.getCollege().getCollegeId().equals(college.getCollegeId())) {
			throw new DepartmentCollegeMismatchException();
		}
		if (!subject.getDepartment().getDepartmentId().equals(department.getDepartmentId())) {
			throw new SubjectDepartmentMismatchException();
		}

		if (dto.getStartTime().isAfter(dto.getEndTime())) {
			throw new InvalidExamTimeException();
		}

		User user = userService.getCurrentUser();

		if (!user.getCollege().getCollegeId().equals(college.getCollegeId())) {
			throw new UserCollegeMismatchException();
		}

		if (!user.getDepartment().getDepartmentId().equals(department.getDepartmentId())) {
			throw new UserDepartmentMismatchException();
		}

		Exam exam = examMapper.toEntity(dto);

		exam.setCollege(college);
		exam.setDepartment(department);
		exam.setSubject(subject);
		exam.setCreatedBy(user);
		exam.setTotalQuestions(dto.getQuestions().size());

		Set<Integer> orders = new HashSet<>();

		for (CreateQuestionRequestDTO q : dto.getQuestions()) {
			if (!orders.add(q.getQuestionOrder())) {
				throw new DuplicateQuestionOrderException();
			}
		}

		List<Question> questions = dto.getQuestions().stream().map(qDto -> {
			Question q = questionMapper.toEntity(qDto);
			q.setExam(exam);

			QuestionType type = QuestionType.valueOf(qDto.getQuestionType().name());
			q.setQuestionType(type);

			validateQuestion(qDto, type);

			if (qDto.getChoices() != null) {
				List<Choice> choices = qDto.getChoices().stream().map(cDto -> {
					Choice c = choiceMapper.toEntity(cDto);
					c.setQuestion(q);
					return c;
				}).collect(Collectors.toList());

				q.getChoices().clear();
				q.getChoices().addAll(choices);

			}

			return q;
		}).collect(Collectors.toList());

		exam.setQuestions(questions);

		exam.setQrCodeUrl(null);

		Exam savedExam = examRepository.save(exam);
		return examMapper.toDto(savedExam);

	}

	@Transactional
	public ExamResponseDTO updateExam(Long examId, @Valid UpdateExamRequestDTO dto) {

		Exam exam = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);

		if (LocalDateTime.now().isAfter(exam.getStartTime())) {
			throw new ExamLockedException();
		}

		// ===== basic fields =====
		exam.setTitle(dto.getTitle());
		exam.setDescription(dto.getDescription());
		exam.setDurationMinutes(dto.getDurationMinutes());
		exam.setPerQuestionTimeSeconds(dto.getPerQuestionTimeSeconds());
		exam.setAllowBackNavigation(dto.getAllowBackNavigation());
		exam.setRandomizeQuestions(dto.getRandomizeQuestions());
		exam.setStartTime(dto.getStartTime());
		exam.setEndTime(dto.getEndTime());
		exam.setIsActive(dto.getIsActive());

		College college = collegeRepository.findById(dto.getCollegeId()).orElseThrow(CollegeNotFoundException::new);

		Department department = departmentRepository.findById(dto.getDepartmentId())
				.orElseThrow(DepartmentNotFoundException::new);

		Subject subject = subjectRepository.findById(dto.getSubjectId()).orElseThrow(SubjectNotFoundException::new);

		// ✔ relations validation
		if (!department.getCollege().getCollegeId().equals(college.getCollegeId())) {
			throw new DepartmentCollegeMismatchException();
		}

		if (!subject.getDepartment().getDepartmentId().equals(department.getDepartmentId())) {
			throw new SubjectDepartmentMismatchException();
		}

		exam.setCollege(college);
		exam.setDepartment(department);
		exam.setSubject(subject);

		// ===== duplicate question order =====
		Set<Integer> orders = new HashSet<>();
		for (UpdateQuestionRequestDTO q : dto.getQuestions()) {
			if (!orders.add(q.getQuestionOrder())) {
				throw new DuplicateQuestionOrderException();
			}
		}

		// ===== existing questions =====
		Map<Long, Question> existingQuestions = exam.getQuestions().stream()
				.collect(Collectors.toMap(Question::getQuestionId, q -> q));

		List<Question> updatedQuestions = dto.getQuestions().stream().map(qDto -> {

			Question q;

			if (qDto.getQuestionId() != null) {
				q = existingQuestions.get(qDto.getQuestionId());
				if (q == null) {
					throw new QuestionNotBelongToExamException();
				}
			} else {
				q = new Question();
				q.setExam(exam);
			}

			q.setQuestionText(qDto.getQuestionText());
			q.setQuestionOrder(qDto.getQuestionOrder());
			q.setMarks(qDto.getMarks());
			q.setQuestionType(qDto.getQuestionType());

			validateQuestion(qDto, q.getQuestionType());

			// ===== choices =====
			Map<Long, Choice> existingChoices = q.getChoices() == null ? new HashMap<>()
					: q.getChoices().stream().collect(Collectors.toMap(Choice::getChoiceId, c -> c));

			List<Choice> updatedChoices = qDto.getChoices().stream().map(cDto -> {

				Choice c;

				if (cDto.getChoiceId() != null) {
					c = existingChoices.get(cDto.getChoiceId());
					if (c == null) {
						throw new ChoiceNotBelongToQuestionException();
					}
				} else {
					c = new Choice();
					c.setQuestion(q);
				}

				c.setChoiceText(cDto.getChoiceText());
				c.setIsCorrect(cDto.getIsCorrect());
				c.setChoiceOrder(cDto.getChoiceOrder());

				return c;
			}).collect(Collectors.toList());

			q.getChoices().clear();
			q.getChoices().addAll(updatedChoices);

			return q;

		}).collect(Collectors.toList());

		exam.getQuestions().clear();
		exam.getQuestions().addAll(updatedQuestions);
		exam.setTotalQuestions(updatedQuestions.size());

		return examMapper.toDto(examRepository.save(exam));
	}

	public ExamResponseDTO getExamById(Long examId) {
		Exam exam = examRepository.findExamFull(examId).orElseThrow(ExamNotFoundException::new);

		return examMapper.toDto(exam);
	}

	public Page<ExamResponseDTO> getExamsByCollege(Long collegeId, int page, int size) {
		if (!collegeRepository.existsById(collegeId)) {
			throw new CollegeNotFoundException();
		}

		Pageable pageable = PageRequest.of(page, size);
		return examRepository.findByCollegeId(collegeId, pageable).map(examMapper::toDto);
	}

	public Page<ExamResponseDTO> getExamsByDepartment(Long departmentId, int page, int size) {
		if (!departmentRepository.existsById(departmentId)) {
			throw new DepartmentNotFoundException();
		}
		Pageable pageable = PageRequest.of(page, size);
		return examRepository.findByDepartmentId(departmentId, pageable).map(examMapper::toDto);
	}

	public Page<ExamResponseDTO> getExamsByUser(Long userId, int page, int size) {
		if (!userRepository.existsById(userId)) {
			throw new UserNotFoundException();
		}
		Pageable pageable = PageRequest.of(page, size);
		return examRepository.findByUserId(userId, pageable).map(examMapper::toDto);
	}

	public Page<ExamResponseDTO> getMyExams(int page, int size) {
		User user = userService.getCurrentUser();
		Pageable pageable = PageRequest.of(page, size);
		return examRepository.findByUserId(user.getUserId(), pageable).map(examMapper::toDto);
	}

	public Page<ExamResponseDTO> getExamsBySubject(Long subjectId, int page, int size) {
		if (!subjectRepository.existsById(subjectId)) {
			throw new SubjectNotFoundException();
		}
		Pageable pageable = PageRequest.of(page, size);
		return examRepository.findBySubjectId(subjectId, pageable).map(examMapper::toDto);
	}

	public Page<ExamResponseDTO> searchExams(String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		return examRepository.searchExams(keyword, pageable).map(examMapper::toDto);
	}

	public Page<ExamResponseDTO> getAllActiveExams(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return examRepository.findAllActive(pageable).map(examMapper::toDto);
	}

	public Page<ExamResponseDTO> getAllExams(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return examRepository.findAll(pageable).map(examMapper::toDto);
	}

	@Transactional
	public void deActivateExam(Long examId) {
		Exam exam = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);

		if (LocalDateTime.now().isAfter(exam.getStartTime())) {
			throw new ExamDeletionNotAllowedException();
		}
		if (!exam.getIsActive()) {
			throw new ExamAlreadyDeactivatedException();
		}
		exam.setIsActive(false);

		examRepository.save(exam);
	}

	@Transactional
	public void deleteExam(Long examId) {

		Exam exam = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);

		if (LocalDateTime.now().isAfter(exam.getStartTime())) {
			throw new ExamDeletionNotAllowedException();
		}

		examRepository.delete(exam);
	}

	private void validateQuestion(CreateQuestionRequestDTO dto, QuestionType type) {

		List<CreateChoiceRequestDTO> choices = dto.getChoices();

		if (choices != null && !choices.isEmpty()) {
			Set<Integer> choiceOrders = new HashSet<>();

			for (CreateChoiceRequestDTO c : choices) {
				if (!choiceOrders.add(c.getChoiceOrder())) {
					throw new DuplicateChoiceOrderException();
				}
			}
		}

		switch (type) {

		case MCQ -> {
			if (choices == null || choices.size() < 2) {
				throw new InvalidMCQChoicesException();
			}

			long correctCount = choices.stream().filter(CreateChoiceRequestDTO::getIsCorrect).count();

			if (correctCount != 1) {
				throw new InvalidMCQChoicesException();
			}
		}

		case TRUE_FALSE -> {
			if (choices == null || choices.size() != 2) {
				throw new InvalidTrueFalseChoicesException();
			}

			long correctCount = choices.stream().filter(CreateChoiceRequestDTO::getIsCorrect).count();

			if (correctCount != 1) {
				throw new InvalidTrueFalseChoicesException();
			}
		}

		case ESSAY -> {
			if (choices != null && !choices.isEmpty()) {
				throw new EssayQuestionHasChoicesException();
			}
		}

		default -> throw new InvalidQuestionTypeException();
		}
	}

	private void validateQuestion(UpdateQuestionRequestDTO dto, QuestionType type) {

		List<UpdateChoiceRequestDTO> choices = dto.getChoices();

		if (choices != null && !choices.isEmpty()) {
			Set<Integer> choiceOrders = new HashSet<>();

			for (UpdateChoiceRequestDTO c : choices) {
				if (!choiceOrders.add(c.getChoiceOrder())) {
					throw new DuplicateChoiceOrderException();
				}
			}
		}

		switch (type) {
		case MCQ -> {
			if (choices == null || choices.size() < 2)
				throw new InvalidMCQChoicesException();
			long correctCount = choices.stream().filter(UpdateChoiceRequestDTO::getIsCorrect).count();
			if (correctCount != 1)
				throw new InvalidMCQChoicesException();
		}
		case TRUE_FALSE -> {
			if (choices == null || choices.size() != 2)
				throw new InvalidTrueFalseChoicesException();
			long correctCount = choices.stream().filter(UpdateChoiceRequestDTO::getIsCorrect).count();
			if (correctCount != 1)
				throw new InvalidTrueFalseChoicesException();
		}
		case ESSAY -> {
			if (choices != null && !choices.isEmpty())
				throw new EssayQuestionHasChoicesException();
		}
		default -> throw new InvalidQuestionTypeException();
		}
	}

	@Transactional
	public ExamQrResponseDTO generateQrForExam(Long examId) {

		Exam exam = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);

		if (exam.getQrExpiresAt() != null && LocalDateTime.now().isBefore(exam.getQrExpiresAt())) {

			return ExamQrResponseDTO.builder().examId(exam.getExamId()).qrCodeUrl(exam.getQrCodeUrl())
					.expiresAt(exam.getQrExpiresAt()).build();
		}

		// generate new token
		String token = UUID.randomUUID().toString();

		LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(qrProperties.getExpirationMinutes());

		String qrContent = "https://app.com/exams/" + exam.getExamId() + "/enter?token=" + token;

		byte[] qrImage = qrCodeService.generateQrCode(qrContent);
		String qrUrl = imageService.uploadImage(qrImage);

		exam.setQrToken(token);
		exam.setQrCodeUrl(qrUrl);
		exam.setQrExpiresAt(expiresAt);
		examRepository.save(exam);

		return ExamQrResponseDTO.builder().examId(exam.getExamId()).qrCodeUrl(qrUrl).expiresAt(expiresAt).build();
	}

}
