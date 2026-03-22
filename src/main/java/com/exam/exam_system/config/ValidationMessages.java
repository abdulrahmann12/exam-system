package com.exam.exam_system.config;

/**
 * Centralized validation messages for all DTO field constraints.
 * <p>
 * Use these constants in Jakarta Bean Validation annotations
 * ({@code @NotBlank}, {@code @NotNull}, {@code @Size}, etc.)
 * to keep messages consistent across the entire API.
 */
public class ValidationMessages {

	private ValidationMessages() {
		// Prevent instantiation
	}

	// ==================== Common / Shared ====================
	public static final String USERNAME_REQUIRED = "Username is required.";
	public static final String USERNAME_SIZE = "Username must be between 3 and 30 characters.";
	public static final String EMAIL_REQUIRED = "Email is required.";
	public static final String EMAIL_INVALID = "Please provide a valid email address.";
	public static final String PASSWORD_REQUIRED = "Password is required.";
	public static final String PASSWORD_SIZE = "Password must be at least 8 characters.";
	public static final String FIRST_NAME_REQUIRED = "First name is required.";
	public static final String LAST_NAME_REQUIRED = "Last name is required.";
	public static final String PHONE_INVALID = "Please provide a valid Egyptian phone number (e.g., 01012345678).";

	// ==================== Auth / Login ====================
	public static final String USERNAME_OR_EMAIL_REQUIRED = "Username or email is required.";
	public static final String CURRENT_PASSWORD_REQUIRED = "Current password is required.";
	public static final String NEW_PASSWORD_REQUIRED = "New password is required.";
	public static final String NEW_PASSWORD_SIZE = "New password must be at least 8 characters.";
	public static final String RESET_CODE_REQUIRED = "Reset code is required.";
	public static final String CONFIRMATION_CODE_REQUIRED = "Confirmation code is required.";
	public static final String VERIFICATION_CODE_REQUIRED = "Verification code is required.";

	// ==================== Username / Email Change ====================
	public static final String NEW_USERNAME_REQUIRED = "New username is required.";
	public static final String NEW_EMAIL_REQUIRED = "New email is required.";

	// ==================== IDs ====================
	public static final String USER_ID_REQUIRED = "User ID is required.";
	public static final String ROLE_ID_REQUIRED = "Role ID is required.";
	public static final String COLLEGE_ID_REQUIRED = "College ID is required.";
	public static final String DEPARTMENT_ID_REQUIRED = "Department ID is required.";
	public static final String SUBJECT_ID_REQUIRED = "Subject ID is required.";
	public static final String EXAM_ID_REQUIRED = "Exam ID is required.";
	public static final String QUESTION_ID_REQUIRED = "Question ID is required for updates.";
	public static final String CHOICE_ID_REQUIRED = "Choice ID is required for updates.";
	public static final String SESSION_ID_REQUIRED = "Session ID is required.";

	// ==================== College ====================
	public static final String COLLEGE_NAME_REQUIRED = "College name is required.";

	// ==================== Department ====================
	public static final String DEPARTMENT_NAME_REQUIRED = "Department name is required.";

	// ==================== Subject ====================
	public static final String SUBJECT_NAME_REQUIRED = "Subject name is required.";
	public static final String SUBJECT_CODE_REQUIRED = "Subject code is required.";

	// ==================== Role ====================
	public static final String ROLE_NAME_REQUIRED = "Role name is required.";
	public static final String ROLE_NAME_SIZE = "Role name must be between 3 and 50 characters.";

	// ==================== Permission ====================
	public static final String PERMISSION_MODULE_REQUIRED = "Permission module is required.";
	public static final String PERMISSION_ACTION_REQUIRED = "Permission action is required.";
	public static final String PERMISSION_DESCRIPTION_SIZE = "Description must not exceed 255 characters.";

	// ==================== Exam ====================
	public static final String EXAM_TITLE_REQUIRED = "Exam title is required.";
	public static final String EXAM_DESCRIPTION_REQUIRED = "Exam description is required.";
	public static final String EXAM_DURATION_MIN = "Duration must be at least 1 minute.";
	public static final String EXAM_PER_QUESTION_TIME_MIN_30 = "Per-question time must be at least 30 seconds.";
	public static final String EXAM_PER_QUESTION_TIME_MIN_10 = "Per-question time must be at least 10 seconds.";
	public static final String EXAM_QUESTIONS_REQUIRED = "Exam must contain at least one question.";

	// ==================== Question ====================
	public static final String QUESTION_TEXT_REQUIRED = "Question text is required.";
	public static final String QUESTION_TYPE_REQUIRED = "Question type is required (MCQ, TRUE_FALSE, or ESSAY).";
	public static final String QUESTION_ORDER_REQUIRED = "Question order is required.";
	public static final String QUESTION_MARKS_REQUIRED = "Marks value is required.";
	public static final String QUESTION_MARKS_MIN = "Marks must be at least 1.";

	// ==================== Choice ====================
	public static final String CHOICE_TEXT_REQUIRED = "Choice text is required.";
	public static final String CHOICE_IS_CORRECT_REQUIRED = "The 'isCorrect' field is required. Set to true for the correct answer.";
	public static final String CHOICE_ORDER_REQUIRED = "Choice order is required.";
	public static final String CHOICE_ORDER_MIN = "Choice order must be at least 1.";

	// ==================== Student ====================
	public static final String STUDENT_CODE_REQUIRED = "Student code is required.";
	public static final String ACADEMIC_YEAR_REQUIRED = "Academic year is required.";

	// ==================== Exam Session / QR ====================
	public static final String QR_TOKEN_REQUIRED = "QR token is required.";
	public static final String SESSION_CODE_REQUIRED = "Session code is required.";
}
