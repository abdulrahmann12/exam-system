package com.exam.exam_system.config;

public class Messages {

	// ==================== General ====================
	public static final String FETCH_SUCCESS = "Data retrieved successfully.";

	// ==================== Email Messages ====================
	public static final String RESET_PASSWORD = "Reset your password";
	public static final String RESEND_CODE = "A new verification code has been sent to your email.";
	public static final String FAILED_EMAIL = "We could not send the email. Please try again later.";

	// ==================== Auth Messages ====================
	public static final String LOGIN_SUCCESS = "Logged in successfully.";
	public static final String GOOGLE_LOGIN_SUCCESS = "Logged in with Google successfully.";
	public static final String CREATE_NEW_USER = "User account created successfully.";
	public static final String LOGOUT_SUCCESS = "Logged out successfully.";
	public static final String ALREADY_LOGGED_OUT = "You are already logged out.";
	public static final String INVALID_CONFIRM_EMAIL = "The confirmation code is invalid. Please check and try again.";
	public static final String CONFIRM_EMAIL_SUCCESS = "Email address confirmed successfully.";
	public static final String INVALID_PASSWORD = "The current password you entered is incorrect.";
	public static final String CHANGE_PASSWORD = "Password changed successfully.";
	public static final String CODE_SENT = "Verification code sent to your email.";
	public static final String INVALID_VERIFICATION_CODE = "The verification code is invalid. Please check and try again.";
	public static final String INVALID_RESET_CODE = "The password reset code is invalid. Please request a new one.";
	public static final String EXPIRED_RESET_CODE = "The password reset code has expired. Please request a new one.";
	public static final String EMAIL_ALREADY_EXISTS = "An account with this email address already exists.";
	public static final String USERNAME_ALREADY_EXISTS = "This username is already taken. Please choose a different one.";
	public static final String BAD_CREDENTIALS = "Invalid username or password. Please check your credentials.";
	public static final String AUTH_FAILED = "Authentication failed. Please try again.";
	public static final String REQUEST_NOT_SUPPORTED = "This HTTP method is not supported for the requested endpoint.";
	public static final String NOT_SALES_EMPLOYEE = "Access denied. The user does not have the SALES_EMPLOYEE role.";
	public static final String NOT_PURCHASES_EMPLOYEE = "Access denied. The user does not have the PURCHASING_OFFICER role.";
	public static final String PHONE_ALREADY_EXISTS = "An account with this phone number already exists.";
	public static final String TOO_MANY_REQUESTS = "Too many requests. Please wait a moment and try again.";
	public static final String INVALID_EMAIL_CHANGE_REQUEST = "The email change request is invalid or has expired. Please request a new one.";
	public static final String EXPIRED_CODE = "The verification code has expired. Please request a new one.";
	public static final String PROFILE_UPDATED = "Profile updated successfully.";
	public static final String USERNAME_UPDATED = "Username changed successfully. Please log in again with your new username.";
	public static final String EMAIL_CHANGE_REQUESTED = "A verification code has been sent to your new email address.";
	public static final String EMAIL_CHANGED = "Email address updated successfully. Please log in again with your new email.";
	public static final String UNAUTHORIZED = "You are not authorized to perform this action.";

	// ==================== Token Messages ====================
	public static final String INVALID_REFRESH_TOKEN = "The refresh token is invalid or missing. Please log in again.";
	public static final String NEW_TOKEN_GENERATED = "Access token refreshed successfully.";
	public static final String COULD_NOT_EXTRACT_USER = "Unable to extract the username from the token. The token may be malformed.";
	public static final String TOKEN_NOT_FOUND = "The token was not found or is invalid. Please log in again.";
	public static final String TOKEN_EXPIRED_OR_REVOKED = "The token has expired or been revoked. Please log in again.";
	public static final String SESSION_EXPIRED = "Your session has expired. Please log in again.";
	public static final String ACCESS_DENIED = "You do not have permission to access this resource.";
	public static final String MISSING_TOKEN = "Authentication token is missing. Please include a valid JWT token in the request header.";

	// ==================== User Messages ====================
	public static final String USER_NOT_FOUND = "User not found. Please verify the user ID and try again.";
	public static final String USERNAME_NOT_FOUND = "No user found with this username or email: ";
	public static final String USER_NOT_AUTHENTICATED = "You must be logged in to perform this action.";
	public static final String CHANGE_ROLES_ERROR = "Invalid role. Please provide a valid role (e.g., ADMIN, DOCTOR).";
	public static final String USER_UPDATE_PROFILE = "User profile updated successfully.";
	public static final String USER_UPDATE_IMAGE = "Profile image updated successfully.";
	public static final String DEACTIVATE_USER = "User account deactivated successfully.";
	public static final String ACTIVATE_USER = "User account activated successfully.";
	public static final String UPDATE_USER = "User updated successfully.";
	public static final String USER_ALREADY_DEACTIVATE = "This user account is already deactivated.";
	public static final String USER_ALREADY_ACTIVE = "This user account is already active.";
	public static final String USER_DEACTIVATED_EXCEPTION = "Your account has been deactivated. Please contact support for assistance.";
	public static final String USER_DEPARTMENT_MISMATCH = "You are not authorized to perform this action for the selected department.";
	public static final String USER_COLLEGE_MISMATCH = "You are not authorized to perform this action for the selected college.";

	// ==================== Department Messages ====================
	public static final String DEPARTMENT_NOT_FOUND = "Department not found. Please verify the department ID and try again.";
	public static final String DEPARTMENT_ALREADY_EXISTS = "A department with this name already exists.";
	public static final String DEPARTMENT_UPDATE = "Department updated successfully.";
	public static final String DELETE_DEPARTMENT = "Department deleted successfully.";
	public static final String ADD_DEPARTMENT = "Department created successfully.";
	public static final String CANNOT_DELETE_DEPARTMENT = "This department cannot be deleted because it has assigned users. Remove or reassign them first.";
	public static final String CANNOT_DELETE_DEPARTMENT_SUBJECS = "This department cannot be deleted because it has assigned subjects. Remove or reassign them first.";
	public static final String DEPARTMENT_NOT_BELONG_TO_COLLEGE = "This department does not belong to the specified college.";
	public static final String DEPARTMENT_COLLEGE_MISMATCH = "The department does not belong to the selected college. Please select a matching department.";

	// ==================== College Messages ====================
	public static final String COLLEGE_NOT_FOUND = "College not found. Please verify the college ID and try again.";
	public static final String COLLEGE_ALREADY_EXISTS = "A college with this name already exists.";
	public static final String COLLEGE_UPDATE = "College updated successfully.";
	public static final String DELETE_COLLEGE = "College deleted successfully.";
	public static final String ADD_COLLEGE = "College created successfully.";
	public static final String CANNOT_DELETE_COLLEGE_SUBJECS = "This college cannot be deleted because it has assigned subjects. Remove or reassign them first.";
	public static final String CANNOT_DELETE_COLLEGE_DEPARTMENT = "This college cannot be deleted because it has assigned departments. Remove or reassign them first.";

	// ==================== Subject Messages ====================
	public static final String SUBJECT_NOT_FOUND = "Subject not found. Please verify the subject ID and try again.";
	public static final String SUBJECT_ALREADY_EXISTS = "A subject with this name already exists.";
	public static final String SUBJECT_UPDATED = "Subject updated successfully.";
	public static final String SUBJECT_DELETED = "Subject deleted successfully.";
	public static final String SUBJECT_ADDED = "Subject created successfully.";
	public static final String CANNOT_DELETE_SUBJECT_ASSIGNED = "This subject cannot be deleted because it has associated data.";
	public static final String SUBJECT_CODE_ALREADY_EXISTS = "A subject with this code already exists. Please use a different code.";
	public static final String CANNOT_DELETE_SUBJECT_WITH_EXAMS = "This subject cannot be deleted because it has associated exams. Remove the exams first.";
	public static final String SUBJECT_DEPARTMENT_MISMATCH = "The subject does not belong to the selected department. Please select a matching subject.";

	// ==================== Role Messages ====================
	public static final String ROLE_NOT_FOUND = "Role not found. Please verify the role ID and try again.";
	public static final String ROLE_ALREADY_EXISTS = "A role with this name already exists.";
	public static final String ROLE_UPDATE = "Role updated successfully.";
	public static final String DELETE_ROLE = "Role deleted successfully.";
	public static final String ADD_ROLE = "Role created successfully.";
	public static final String CANNOT_DELETE_ROLE = "This role cannot be deleted because it is assigned to one or more users. Remove the assignments first.";

	// ==================== Permission Messages ====================
	public static final String PERMISSION_NOT_FOUND = "Permission not found. Please verify the permission ID and try again.";
	public static final String PERMISSION_ALREADY_EXISTS = "A permission with this code already exists.";
	public static final String PERMISSION_UPDATE = "Permission updated successfully.";
	public static final String DELETE_PERMISSION = "Permission deleted successfully.";
	public static final String ADD_PERMISSION = "Permission created successfully.";
	public static final String CANNOT_DELETE_PERMISSION = "This permission cannot be deleted because it is assigned to one or more roles. Remove the assignments first.";

	// ==================== Error Messages ====================
	public static final String FORMAT_ERROR = "The request body contains malformed JSON. Please check the syntax and try again.";
	public static final String INVALID_DATA = "Invalid request data. Please check the request body and correct any errors.";

	// ==================== Exam Messages ====================
	public static final String EXAM_CREATED = "Exam created successfully.";
	public static final String EXAM_UPDATED = "Exam updated successfully.";
	public static final String EXAM_DELETED = "Exam deleted successfully.";
	public static final String EXAM_NOT_FOUND = "Exam not found. Please verify the exam ID and try again.";
	public static final String EXAM_NOT_ACTIVE = "This exam is not currently active.";
	public static final String EXAM_ALREADY_STARTED = "This exam has already started and cannot be modified.";
	public static final String EXAM_ENDED = "This exam has already ended.";
	public static final String INVALID_EXAM_TIME = "The start time must be before the end time. Please correct the exam schedule.";
	public static final String EXAM_START_IN_PAST = "The exam start time cannot be in the past. Please choose a future date and time.";
	public static final String EXAM_ALREADY_DEACTIVATE = "This exam has already been deactivated.";
	public static final String EMPTY_EXAM_QUESTIONS = "An exam must contain at least one question. Please add questions before saving.";
	public static final String INVALID_QUESTION_TYPE = "The question type is invalid. Allowed types: MCQ, TRUE_FALSE, ESSAY.";
	public static final String INVALID_QUESTION_ORDER = "Each question must have a unique order number greater than zero.";
	public static final String INVALID_QUESTION_MARKS = "Question marks must be a positive number greater than zero.";
	public static final String INVALID_MCQ_CHOICES = "An MCQ question must have at least two choices with exactly one marked as correct.";
	public static final String INVALID_TRUE_FALSE_CHOICES = "A TRUE/FALSE question must have exactly two choices with one marked as correct.";
	public static final String ESSAY_QUESTION_HAS_CHOICES = "An ESSAY question must not have any choices. Remove all choices for this question.";
	public static final String COLLEGE_MISMATCH = "The department or subject does not belong to the selected college. Please verify your selections.";
	public static final String EXAM_DELETION_NOT_ALLOWED = "This exam cannot be deleted because it has already started.";
	public static final String EXAM_LOCKED = "This exam is locked and cannot be modified because it has already started.";
	public static final String DUPLICATE_QUESTION_ORDER = "Duplicate question order detected. Each question within the same exam must have a unique order number.";
	public static final String DUPLICATE_CHOICE_ORDER = "Duplicate choice order detected. Each choice within the same question must have a unique order number.";
	public static final String QUESTION_NOT_BELONG_TO_EXAM = "The specified question does not belong to this exam.";
	public static final String CHOICE_NOT_BELONG_TO_QUESTION = "The specified choice does not belong to this question.";

	// ==================== QR Code Messages ====================
	public static final String QR_GENERATED = "QR code generated successfully.";
	public static final String INVALID_QR = "The QR code is invalid or has expired. Please request a new one.";
	public static final String QR_EXPIRED = "The QR code has expired. Please generate a new one.";
	public static final String QR_GENERATION_FAILED = "Failed to generate the QR code. Please try again.";

	// ==================== Question Messages ====================
	public static final String QUESTION_ADDED = "Question added successfully.";
	public static final String QUESTION_UPDATED = "Question updated successfully.";
	public static final String QUESTION_DELETED = "Question deleted successfully.";
	public static final String QUESTION_NOT_FOUND = "Question not found. Please verify the question ID and try again.";

	// ==================== Answer Messages ====================
	public static final String ANSWER_SAVED = "Answer submitted successfully.";
	public static final String ANSWER_UPDATED = "Answer updated successfully.";
	public static final String ANSWER_NOT_FOUND = "Answer not found. Please verify the answer ID and try again.";

	// ==================== Exam Attempt / Session Messages ====================
	public static final String EXAM_SESSION_STARTED = "Exam session started.";
	public static final String EXAM_SESSION_ALREADY_EXISTS = "An exam session already exists for this exam. You cannot start a duplicate session.";
	public static final String EXAM_SESSION_NOT_FOUND = "Exam session not found. Please verify the session ID and try again.";
	public static final String EXAM_ALREADY_SUBMITTED = "This exam has already been submitted. You cannot submit it again.";
	public static final String EXAM_SUBMITTED = "Exam submitted successfully.";
	public static final String TIME_EXCEEDED = "The exam time has expired. Your session has been automatically ended.";

	// ==================== Result Messages ====================
	public static final String RESULT_CALCULATED = "Results calculated successfully.";
	public static final String RESULT_NOT_FOUND = "Result not found. Please verify the result ID and try again.";
	public static final String RESULT_PUBLISHED = "Results published successfully.";

	// ==================== Student Messages ====================
	public static final String STUDENT_NOT_FOUND = "Student not found. Please verify the student ID and try again.";
	public static final String STUDENT_ALREADY_EXISTS = "A student profile already exists for this user.";
	public static final String STUDENT_CODE_ALREADY_EXISTS = "This student code is already in use. Please use a different code.";
	public static final String STUDENT_ALREADY_DEACTIVATED = "This student account is already deactivated.";
	public static final String STUDENT_ALREADY_ACTIVE = "This student account is already active.";
	public static final String STUDENT_CREATED = "Student registered successfully.";
	public static final String STUDENT_UPDATED = "Student profile updated successfully.";
	public static final String STUDENT_DELETED = "Student deleted successfully.";
	public static final String STUDENT_DEACTIVATED = "Student account deactivated successfully.";
	public static final String STUDENT_ACTIVATED = "Student account activated successfully.";
	public static final String STUDENT_CODE_MISMATCH = "The student code does not match the authenticated user. Please verify your student code.";
	public static final String STUDENT_ACCOUNT_DEACTIVATED = "This student account has been deactivated. Please contact support.";

	// ==================== Exam Access Messages ====================
	public static final String EXAM_NOT_AVAILABLE = "This exam is not currently available. Please check the exam schedule.";
	public static final String EXAM_NOT_STARTED_YET = "This exam has not started yet. Please wait until the scheduled start time.";
	public static final String EXAM_ALREADY_ENDED = "This exam has already ended. You can no longer access it.";
	public static final String EXAM_VALIDATED = "Exam access validated successfully.";
	public static final String STUDENT_CODE_VERIFIED = "Student identity verified successfully.";
	public static final String EXAM_SESSION_STARTED_SUCCESS = "Exam session started successfully.";
	public static final String DUPLICATE_SESSION = "You have already started a session for this exam. Duplicate sessions are not allowed.";

	// ==================== Upload / Python Service Messages ====================
	public static final String QUESTIONS_UPLOADED_SUCCESSFULLY = "Questions uploaded and parsed successfully.";
	public static final String PYTHON_SERVICE_UNAVAILABLE = "Unable to connect to the question parsing service. Please ensure the service is running and try again.";
	public static final String PYTHON_SERVICE_INVALID_RESPONSE = "Received an invalid response from the question parsing service. Please try again or contact support.";
}