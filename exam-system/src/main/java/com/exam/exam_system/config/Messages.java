package com.exam.exam_system.config;

public class Messages {

	// ==================== Email Messages ====================
	public static final String FETCH_SUCCESS = "Data fetched successfully";
	
	// ==================== Email Messages ====================
    public static final String RESET_PASSWORD = "Reset your password";
    public static final String RESEND_CODE = "A new code was sent";
    public static final String FAILED_EMAIL = "Failed to send email. Please try again later";
	
	// ==================== Auth Messages ==================== 
    public static final String LOGIN_SUCCESS = "Login successfully";
    public static final String GOOGLE_LOGIN_SUCCESS = "Google login successful!";
    public static final String CREATE_NEW_USER = "New user created successfully";
    public static final String LOGOUT_SUCCESS = "Logged out successfully";
    public static final String ALREADY_LOGGED_OUT = "You are already logged out";
    public static final String INVALID_CONFIRM_EMAIL = "Invalid confirmation code";
    public static final String CONFIRM_EMAIL_SUCCESS = "Email confirmed successfully";
    public static final String INVALID_PASSWORD = "Current password is incorrect";
    public static final String CHANGE_PASSWORD = "Password changed successfully";
    public static final String CODE_SENT = "Code sent successfully";
    public static final String INVALID_RESET_CODE = "Invalid reset code";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String BAD_CREDENTIALS = "Invalid username or password";
    public static final String AUTH_FAILED = "Authentication failed";
    public static final String REQUEST_NOT_SUPPORTED = "Request method not supported";
    public static final String NOT_SALES_EMPLOYEE = "User does not have SALES_EMPLOYEE role.";
    public static final String NOT_PURCHASES_EMPLOYEE = "User does not have PURCHASING_OFFICER role.";
    
	// ==================== Token Messages ====================
    public static final String INVALID_REFRESH_TOKEN = "Invalid or missing refresh token";
    public static final String NEW_TOKEN_GENERATED = "New token generated successfully";
    public static final String COULD_NOT_EXTRACT_USER = "Unable to extract username from token";
    public static final String TOKEN_NOT_FOUND = "Token not found or invalid";
    public static final String TOKEN_EXPIRED_OR_REVOKED = "Token expired or revoked";
    public static final String SESSION_EXPIRED = "Your session has expired. Please login again";
    public static final String ACCESS_DENIED = "You do not have permission to access this resource";
    public static final String MISSING_TOKEN = "JWT token is missing";
    
	// ==================== User Messages ==================== 
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USERNAME_NOT_FOUND = "User not found with username or email: ";
    public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
    public static final String CHANGE_ROLES_ERROR = "Invalid role. Please provide one of: ADMIN, DOCTOR, etc.";
    public static final String USER_UPDATE_PROFILE = "User role updated successfully";
    public static final String USER_UPDATE_IMAGE = "User image updated successfully";
    public static final String DELETE_USER = "User deleted successfully";
    public static final String UPDATE_USER = "User updated successfully";
    
	// ==================== Department Messages ====================
    public static final String DEPARTMENT_NOT_FOUND = "Department not found";
    public static final String DEPARTMENT_ALREADY_EXISTS = "Department with this name already exist";
    public static final String DEPARTMENT_UPDATE= "Department updated successfully";
    public static final String DELETE_DEPARTMENT = "Department deleted successfully";
    public static final String ADD_DEPARTMENT = "Department added successfully";
    public static final String CANNOT_DELETE_DEPARTMENT = "Cannot delete department with assigned users.";
	
	// ==================== College Messages ====================
    public static final String COLLEGE_NOT_FOUND = "College not found";
    public static final String COLLEGE_ALREADY_EXISTS = "College with this name already exist";
    public static final String COLLEGE_UPDATE= "College updated successfully";
    public static final String DELETE_COLLEGE = "College deleted successfully";
    public static final String ADD_COLLEGE = "College added successfully";
    public static final String CANNOT_DELETE_COLLEGE = "Cannot delete College with assigned subjects.";
    
	// ==================== Role Messages ====================
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String ROLE_ALREADY_EXISTS = "Role with this name already exist";
    public static final String ROLE_UPDATE= "Role updated successfully";
    public static final String DELETE_ROLE = "Role deleted successfully";
    public static final String ADD_ROLE = "Role added successfully";
    public static final String CANNOT_DELETE_ROLE = "Cannot delete Role with assigned users.";
    
	// ==================== Error Messages ====================
    public static final String FORMAT_ERROR = "Malformed JSON request";
    public static final String INVALID_DATA = "Invalid data. Please check your request body";
    
    // ==================== Exam Messages ====================
    public static final String EXAM_CREATED = "Exam created successfully";
    public static final String EXAM_UPDATED = "Exam updated successfully";
    public static final String EXAM_DELETED = "Exam deleted successfully";
    public static final String EXAM_NOT_FOUND = "Exam not found";
    public static final String EXAM_NOT_ACTIVE = "Exam is not active";
    public static final String EXAM_ALREADY_STARTED = "Exam already started";
    public static final String EXAM_ENDED = "Exam has ended";

    // ==================== QR Code Messages ====================
    public static final String QR_GENERATED = "QR code generated successfully";
    public static final String INVALID_QR = "Invalid or expired QR code";
    public static final String QR_EXPIRED = "QR code expired";

    // ==================== Question Messages ====================
    public static final String QUESTION_ADDED = "Question added successfully";
    public static final String QUESTION_UPDATED = "Question updated successfully";
    public static final String QUESTION_DELETED = "Question deleted successfully";
    public static final String QUESTION_NOT_FOUND = "Question not found";

    // ==================== Answer Messages ====================
    public static final String ANSWER_SAVED = "Answer saved successfully";
    public static final String ANSWER_UPDATED = "Answer updated successfully";
    public static final String ANSWER_NOT_FOUND = "Answer not found";

    // ==================== Exam Attempt / Session Messages ====================
    public static final String EXAM_SESSION_STARTED = "Exam session started";
    public static final String EXAM_SESSION_ALREADY_EXISTS = "Exam session already exists";
    public static final String EXAM_SESSION_NOT_FOUND = "Exam session not found";
    public static final String EXAM_ALREADY_SUBMITTED = "Exam already submitted";
    public static final String EXAM_SUBMITTED = "Exam submitted successfully";
    public static final String TIME_EXCEEDED = "Exam time exceeded";

    // ==================== Result Messages ====================
    public static final String RESULT_CALCULATED = "Result calculated successfully";
    public static final String RESULT_NOT_FOUND = "Result not found";
    public static final String RESULT_PUBLISHED = "Result published successfully";
}
