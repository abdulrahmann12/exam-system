package com.exam.exam_system.config;

public class SwaggerMessages {

	// ==================== Auth ====================
	public static final String USER_LOGIN = "User login";
	public static final String REFRESH_ACCESS_TOKEN = "Refresh access token";
	public static final String LOGOUT_USER = "Logout user";
	public static final String CREATE_NEW_USER = "Create new user";
	public static final String CHANGE_USER_PASSWORD = "Change user password";
	public static final String REGENERATE_VERIFICATION_CODE = "Regenerate verification code";
	public static final String FORGET_PASSWORD = "Forget password";
	public static final String RESET_PASSWORD = "Reset password";

	// ==================== Role ====================
	public static final String CREATE_ROLE = "Create new role";
	public static final String UPDATE_ROLE = "Update existing role";
	public static final String GET_ROLE_BY_ID = "Get role by ID";
	public static final String GET_ROLE_BY_NAME = "Get role by name";
	public static final String GET_ALL_ROLES = "Get all roles";
	public static final String DELETE_ROLE = "Delete role by ID";

	// ==================== Permission ====================
	public static final String CREATE_PERMISSION = "Create new permission";
	public static final String UPDATE_PERMISSION = "Update existing permission";
	public static final String GET_PERMISSION_BY_ID = "Get permission by ID";
	public static final String GET_ALL_PERMISSIONS = "Get all permissions";
	public static final String DELETE_PERMISSION = "Delete permission by ID";
	public static final String GET_ALL_PERMISSION_MODULES = "Get all permissions modules";
	public static final String GET_ALL_PERMISSION_ACTIONS = "Get all permissions actions";

	// ==================== College ====================
	public static final String CREATE_COLLEGE = "Create new college";
	public static final String UPDATE_COLLEGE = "Update existing college";
	public static final String GET_COLLEGE_BY_ID = "Get college by ID";
	public static final String GET_COLLEGE_BY_NAME = "Get college by name";
	public static final String SEARCH_COLLEGES = "Search colleges by name";
	public static final String GET_ALL_COLLEGES = "Get all colleges";
	public static final String DELETE_COLLEGE = "Delete college by ID";

	// ==================== Department ====================
	public static final String CREATE_DEPARTMENT = "Create new department";
	public static final String UPDATE_DEPARTMENT = "Update existing department";
	public static final String GET_DEPARTMENT_BY_ID = "Get department by ID";
	public static final String GET_DEPARTMENT_BY_NAME = "Get department by name";
	public static final String SEARCH_DEPARTMENTS = "Search departments by name";
	public static final String GET_ALL_DEPARTMENTS = "Get all departments";
	public static final String GET_DEPARTMENTS_BY_COLLEGE = "Get all departments by college ID";
	public static final String DELETE_DEPARTMENT = "Delete department by ID";

	// ==================== Subject ====================
	public static final String CREATE_SUBJECT = "Create new subject";
	public static final String UPDATE_SUBJECT = "Update existing subject";
	public static final String GET_SUBJECT_BY_ID = "Get subject by ID";
	public static final String GET_ALL_SUBJECTS = "Get all subjects";
	public static final String GET_SUBJECTS_BY_DEPARTMENT = "Get subjects by department ID";
	public static final String GET_SUBJECTS_BY_COLLEGE = "Get subjects by college ID";
	public static final String SEARCH_SUBJECTS = "Search subjects by name or code";
	public static final String DELETE_SUBJECT = "Delete subject by ID";

	// ==================== Exam ====================
	public static final String CREATE_EXAM = "Create new exam";
	public static final String CREATE_EXAM_WITH_FILE = "Create new exam with questions parsed from an uploaded file (PDF/CSV)";
	public static final String UPDATE_EXAM = "Update existing exam";
	public static final String GET_EXAM_BY_ID = "Get exam by ID";
	public static final String GET_EXAMS_BY_COLLEGE = "Get exams by college (paginated)";
	public static final String GET_EXAMS_BY_DEPARTMENT = "Get exams by department (paginated)";
	public static final String GET_EXAMS_BY_USER = "Get exams by user (paginated)";
	public static final String GET_MY_EXAMS = "Get my exams (paginated)";
	public static final String GET_EXAMS_BY_SUBJECT = "Get exams by subject (paginated)";
	public static final String GET_ALL_EXAMS = "Get all exams (paginated)";
	public static final String GET_ALL_ACTIVE_EXAMS = "Get all Active exams (paginated)";
	public static final String SEARCH_EXAMS = "Search exams by keyword (paginated)";
	public static final String DEACTIVATE_EXAM = "DeActivate exam";
	public static final String DELETE_EXAM = "Delete exam";
	public static final String UPLOAD_EXAM_QUESTIONS = "Upload and parse an exam file (PDF/CSV) to automatically create questions";

	// ==================== User ====================
	public static final String GET_CURRENT_USER_PROFILE = "Get current user profile";
	public static final String UPDATE_CURRENT_USER_PROFILE = "Update current user profile";
	public static final String CHANGE_USERNAME = "Change username";
	public static final String REQUEST_EMAIL_CHANGE = "Request email change";
	public static final String CONFIRM_EMAIL_CHANGE = "Confirm email change";
	public static final String GET_USER_BY_ID = "Get user by ID";
	public static final String GET_ALL_USERS = "Get all users";
	public static final String GET_USERS_BY_COLLEGE = "Get users by college";
	public static final String GET_USERS_BY_DEPARTMENT = "Get users by department";
	public static final String GET_USERS_BY_ROLE = "Get users by role";
	public static final String UPDATE_USER_BY_ADMIN = "Update user by admin";
	public static final String DEACTIVATE_USER = "Deactivate user";
	public static final String ACTIVATE_USER = "Activate user";
	public static final String SEARCH_USERS = "Search users";

	// ==================== Student ====================
	public static final String REGISTER_STUDENT = "Register new student and return profile";
	public static final String UPDATE_MY_STUDENT_PROFILE = "Update my own student profile (logged-in student)";
	public static final String ADMIN_UPDATE_STUDENT = "Admin update student by ID";
	public static final String GET_STUDENT_BY_ID = "Get student by ID";
	public static final String GET_ALL_STUDENTS = "Get all students (paginated)";
	public static final String GET_ALL_ACTIVE_STUDENTS = "Get all active students (paginated)";
	public static final String GET_MY_STUDENT_PROFILE = "Get my own student profile";
	public static final String DELETE_STUDENT = "Hard delete student by ID";
	public static final String DEACTIVATE_STUDENT = "Deactivate student by ID (soft delete)";
	public static final String ACTIVATE_STUDENT = "Activate student by ID";
	public static final String SEARCH_STUDENTS = "Search students by studentCode, academicYear, isActive (paginated)";
	public static final String GET_STUDENTS_BY_DEPARTMENT = "Get students by Department (paginated)";
	public static final String GET_STUDENTS_BY_COLLEGE = "Get students by College (paginated)";
	public static final String COUNT_STUDENTS_BY_YEAR = "Count students by academic year";
	public static final String COUNT_ACTIVE_STUDENTS = "Count active students";

	// ==================== Student Exam Session ====================
	public static final String VALIDATE_QR_TOKEN = "Validate QR token and get exam details";
	public static final String ACCESS_EXAM_VIA_QR = "Access exam by scanning QR code (validates token, exam availability, and time window)";
	public static final String VERIFY_STUDENT_CODE = "Verify student code before starting the exam";
	public static final String START_EXAM_SESSION = "Start a new exam session (with all validations)";
	public static final String SUBMIT_ANSWER = "Submit an answer for a question";
	public static final String END_EXAM_SESSION = "End the exam session and calculate results";
	public static final String GET_SESSION_QUESTIONS = "Get exam questions for an active session";
	public static final String GET_MY_EXAM_SESSIONS = "Get all exam sessions for the currently authenticated student";
	public static final String GET_SESSION_BY_ID = "Get a specific exam session by ID (for resuming)";
}
