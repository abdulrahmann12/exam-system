package com.exam.exam_system.config;

public class SwaggerMessages {

	// ==================== Tag Names ====================
	public static final String TAG_AUTH = "Authentication";
	public static final String TAG_ROLE = "Roles";
	public static final String TAG_PERMISSION = "Permissions";
	public static final String TAG_COLLEGE = "Colleges";
	public static final String TAG_DEPARTMENT = "Departments";
	public static final String TAG_SUBJECT = "Subjects";
	public static final String TAG_EXAM = "Exams";
	public static final String TAG_USER = "Users";
	public static final String TAG_STUDENT = "Students";
	public static final String TAG_SESSION = "Exam Sessions";

	// ==================== Tag Descriptions ====================
	public static final String TAG_AUTH_DESC = "Endpoints for user authentication — login, logout, password management, and token refresh.";
	public static final String TAG_ROLE_DESC = "Endpoints for managing system roles and their associated permissions.";
	public static final String TAG_PERMISSION_DESC = "Endpoints for managing fine-grained permissions (modules and actions).";
	public static final String TAG_COLLEGE_DESC = "Endpoints for creating, updating, searching, and deleting colleges.";
	public static final String TAG_DEPARTMENT_DESC = "Endpoints for creating, updating, searching, and deleting departments within colleges.";
	public static final String TAG_SUBJECT_DESC = "Endpoints for creating, updating, searching, and deleting subjects within departments.";
	public static final String TAG_EXAM_DESC = "Endpoints for the full exam lifecycle — creation, updates, file uploads, QR codes, and deactivation.";
	public static final String TAG_USER_DESC = "Endpoints for managing user accounts, profiles, and admin operations.";
	public static final String TAG_STUDENT_DESC = "Endpoints for student registration, profile management, and admin operations.";
	public static final String TAG_SESSION_DESC = "Endpoints for the student exam-taking flow — QR access, identity verification, session management, and answer submission.";

	// ==================== Auth Summaries ====================
	public static final String USER_LOGIN = "Log in with username/email and password";
	public static final String REFRESH_ACCESS_TOKEN = "Refresh an expired access token";
	public static final String LOGOUT_USER = "Log out the current user";
	public static final String CREATE_NEW_USER = "Create a new user account (admin only)";
	public static final String CHANGE_USER_PASSWORD = "Change the current user's password";
	public static final String REGENERATE_VERIFICATION_CODE = "Resend the email verification code";
	public static final String FORGET_PASSWORD = "Request a password reset code via email";
	public static final String RESET_PASSWORD = "Reset password using the emailed verification code";

	// ==================== Auth Descriptions ====================
	public static final String USER_LOGIN_DESC = "Authenticates a user with their username (or email) and password. Returns an access token and a refresh token on success.";
	public static final String REFRESH_ACCESS_TOKEN_DESC = "Generates a new access token using the refresh token provided in the request header. Use this when the current access token has expired.";
	public static final String LOGOUT_USER_DESC = "Invalidates the current user's refresh token, effectively logging them out. The access token will remain valid until it expires.";
	public static final String CREATE_NEW_USER_DESC = "Creates a new user account with the specified role, college, and department. Requires USER_CREATE permission.";
	public static final String CHANGE_USER_PASSWORD_DESC = "Allows the authenticated user to change their password by providing the current password and a new password.";
	public static final String REGENERATE_VERIFICATION_CODE_DESC = "Sends a new verification code to the authenticated user's email address. Use this if the previous code expired or was not received.";
	public static final String FORGET_PASSWORD_DESC = "Sends a password reset code to the user's registered email address. The code is required to reset the password.";
	public static final String RESET_PASSWORD_DESC = "Resets the user's password using the verification code sent via email. Provide the email, code, and new password.";

	// ==================== Role Summaries ====================
	public static final String CREATE_ROLE = "Create a new role";
	public static final String UPDATE_ROLE = "Update an existing role by ID";
	public static final String GET_ROLE_BY_ID = "Retrieve a role by its ID";
	public static final String GET_ROLE_BY_NAME = "Retrieve a role by its name";
	public static final String GET_ALL_ROLES = "List all roles (paginated)";
	public static final String DELETE_ROLE = "Delete a role by its ID";

	// ==================== Role Descriptions ====================
	public static final String CREATE_ROLE_DESC = "Creates a new role with an optional list of permission IDs. Requires ROLE_CREATE permission.";
	public static final String UPDATE_ROLE_DESC = "Updates the name and/or permissions of an existing role. Requires ROLE_UPDATE permission.";
	public static final String GET_ROLE_BY_ID_DESC = "Returns the full details of a role including its assigned permissions.";
	public static final String GET_ROLE_BY_NAME_DESC = "Looks up a role by its exact name and returns its details.";
	public static final String GET_ALL_ROLES_DESC = "Returns a paginated list of all roles in the system. Use the 'page' and 'size' query parameters to control pagination.";
	public static final String DELETE_ROLE_DESC = "Permanently deletes a role. The role must not be assigned to any users.";

	// ==================== Permission Summaries ====================
	public static final String CREATE_PERMISSION = "Create a new permission";
	public static final String UPDATE_PERMISSION = "Update an existing permission by ID";
	public static final String GET_PERMISSION_BY_ID = "Retrieve a permission by its ID";
	public static final String GET_ALL_PERMISSIONS = "List all permissions (paginated)";
	public static final String DELETE_PERMISSION = "Delete a permission by its ID";
	public static final String GET_ALL_PERMISSION_MODULES = "List all available permission modules";
	public static final String GET_ALL_PERMISSION_ACTIONS = "List all available permission actions";

	// ==================== Permission Descriptions ====================
	public static final String CREATE_PERMISSION_DESC = "Creates a new permission by combining a module and an action (e.g., EXAM_CREATE). Requires PERMISSION_CREATE permission.";
	public static final String UPDATE_PERMISSION_DESC = "Updates an existing permission's module, action, or description. Requires PERMISSION_UPDATE permission.";
	public static final String GET_PERMISSION_BY_ID_DESC = "Returns the full details of a permission by its ID.";
	public static final String GET_ALL_PERMISSIONS_DESC = "Returns a paginated list of all permissions. Use 'page' and 'size' query parameters.";
	public static final String DELETE_PERMISSION_DESC = "Permanently deletes a permission. It must not be assigned to any role.";
	public static final String GET_ALL_PERMISSION_MODULES_DESC = "Returns a list of all available module names that can be used when creating permissions (e.g., EXAM, USER, COLLEGE).";
	public static final String GET_ALL_PERMISSION_ACTIONS_DESC = "Returns a list of all available action names that can be used when creating permissions (e.g., CREATE, READ, UPDATE, DELETE).";

	// ==================== College Summaries ====================
	public static final String CREATE_COLLEGE = "Create a new college";
	public static final String UPDATE_COLLEGE = "Update an existing college by ID";
	public static final String GET_COLLEGE_BY_ID = "Retrieve a college by its ID";
	public static final String GET_COLLEGE_BY_NAME = "Retrieve a college by its name";
	public static final String SEARCH_COLLEGES = "Search colleges by name keyword";
	public static final String GET_ALL_COLLEGES = "List all colleges (paginated)";
	public static final String DELETE_COLLEGE = "Delete a college by its ID";

	// ==================== College Descriptions ====================
	public static final String CREATE_COLLEGE_DESC = "Creates a new college with the given name. The name must be unique. Requires COLLEGE_CREATE permission.";
	public static final String UPDATE_COLLEGE_DESC = "Updates the name of an existing college. Requires COLLEGE_UPDATE permission.";
	public static final String GET_COLLEGE_BY_ID_DESC = "Returns the details of a college by its ID.";
	public static final String GET_COLLEGE_BY_NAME_DESC = "Looks up a college by its exact name and returns its details.";
	public static final String SEARCH_COLLEGES_DESC = "Searches for colleges whose name contains the provided keyword. Returns paginated results.";
	public static final String GET_ALL_COLLEGES_DESC = "Returns a paginated list of all colleges. Use 'page' and 'size' query parameters.";
	public static final String DELETE_COLLEGE_DESC = "Permanently deletes a college. The college must not have any assigned departments or subjects.";

	// ==================== Department Summaries ====================
	public static final String CREATE_DEPARTMENT = "Create a new department";
	public static final String UPDATE_DEPARTMENT = "Update an existing department by ID";
	public static final String GET_DEPARTMENT_BY_ID = "Retrieve a department by its ID";
	public static final String GET_DEPARTMENT_BY_NAME = "Retrieve a department by its name";
	public static final String SEARCH_DEPARTMENTS = "Search departments by name keyword";
	public static final String GET_ALL_DEPARTMENTS = "List all departments (paginated)";
	public static final String GET_DEPARTMENTS_BY_COLLEGE = "List all departments in a specific college (paginated)";
	public static final String DELETE_DEPARTMENT = "Delete a department by its ID";

	// ==================== Department Descriptions ====================
	public static final String CREATE_DEPARTMENT_DESC = "Creates a new department under the specified college. The name must be unique within the system. Requires DEPARTMENT_CREATE permission.";
	public static final String UPDATE_DEPARTMENT_DESC = "Updates the name or college assignment of an existing department. Requires DEPARTMENT_UPDATE permission.";
	public static final String GET_DEPARTMENT_BY_ID_DESC = "Returns the details of a department by its ID, including the parent college.";
	public static final String GET_DEPARTMENT_BY_NAME_DESC = "Looks up a department by its exact name and returns its details.";
	public static final String SEARCH_DEPARTMENTS_DESC = "Searches for departments whose name contains the provided keyword. Returns paginated results.";
	public static final String GET_ALL_DEPARTMENTS_DESC = "Returns a paginated list of all departments. Use 'page' and 'size' query parameters.";
	public static final String GET_DEPARTMENTS_BY_COLLEGE_DESC = "Returns a paginated list of all departments that belong to the specified college.";
	public static final String DELETE_DEPARTMENT_DESC = "Permanently deletes a department. The department must not have any assigned users or subjects.";

	// ==================== Subject Summaries ====================
	public static final String CREATE_SUBJECT = "Create a new subject";
	public static final String UPDATE_SUBJECT = "Update an existing subject by ID";
	public static final String GET_SUBJECT_BY_ID = "Retrieve a subject by its ID";
	public static final String GET_ALL_SUBJECTS = "List all subjects (paginated)";
	public static final String GET_SUBJECTS_BY_DEPARTMENT = "List subjects in a specific department (paginated)";
	public static final String GET_SUBJECTS_BY_COLLEGE = "List subjects in a specific college (paginated)";
	public static final String SEARCH_SUBJECTS = "Search subjects by name or code keyword";
	public static final String DELETE_SUBJECT = "Delete a subject by its ID";

	// ==================== Subject Descriptions ====================
	public static final String CREATE_SUBJECT_DESC = "Creates a new subject under the specified department and college. Both the name and code must be unique. Requires SUBJECT_CREATE permission.";
	public static final String UPDATE_SUBJECT_DESC = "Updates the name, code, or department/college assignment of an existing subject. Requires SUBJECT_UPDATE permission.";
	public static final String GET_SUBJECT_BY_ID_DESC = "Returns the details of a subject by its ID, including department and college info.";
	public static final String GET_ALL_SUBJECTS_DESC = "Returns a paginated list of all subjects. Use 'page' and 'size' query parameters.";
	public static final String GET_SUBJECTS_BY_DEPARTMENT_DESC = "Returns a paginated list of all subjects that belong to the specified department.";
	public static final String GET_SUBJECTS_BY_COLLEGE_DESC = "Returns a paginated list of all subjects that belong to the specified college.";
	public static final String SEARCH_SUBJECTS_DESC = "Searches for subjects whose name or code contains the provided keyword. Returns paginated results.";
	public static final String DELETE_SUBJECT_DESC = "Permanently deletes a subject. The subject must not have any associated exams.";

	// ==================== Exam Summaries ====================
	public static final String CREATE_EXAM = "Create a new exam with questions and choices";
	public static final String CREATE_EXAM_WITH_FILE = "Create a new exam with questions parsed from an uploaded file (PDF or CSV)";
	public static final String UPDATE_EXAM = "Update an existing exam by ID";
	public static final String GET_EXAM_BY_ID = "Retrieve full exam details by ID (admin view)";
	public static final String GET_EXAMS_BY_COLLEGE = "List exams in a specific college (paginated)";
	public static final String GET_EXAMS_BY_DEPARTMENT = "List exams in a specific department (paginated)";
	public static final String GET_EXAMS_BY_USER = "List exams created by a specific user (paginated)";
	public static final String GET_MY_EXAMS = "List exams created by the current user (paginated)";
	public static final String GET_EXAMS_BY_SUBJECT = "List exams for a specific subject (paginated)";
	public static final String GET_ALL_EXAMS = "List all exams (paginated)";
	public static final String GET_ALL_ACTIVE_EXAMS = "List all active exams (paginated)";
	public static final String SEARCH_EXAMS = "Search exams by keyword (paginated)";
	public static final String DEACTIVATE_EXAM = "Deactivate an exam (soft delete)";
	public static final String DELETE_EXAM = "Permanently delete an exam";
	public static final String GENERATE_QR = "Generate a QR code for an exam";
	public static final String UPLOAD_EXAM_QUESTIONS = "Upload a file (PDF/CSV) to auto-generate questions for an existing exam (deprecated)";

	// ==================== Exam Descriptions ====================
	public static final String CREATE_EXAM_DESC = "Creates a new exam with its full question set and choices. Requires EXAM_CREATE permission.";
	public static final String CREATE_EXAM_WITH_FILE_DESC = "Creates a new exam by uploading a PDF or CSV file. Questions are automatically parsed from the file. Send the exam metadata as a JSON part and the file as a multipart part.";
	public static final String UPDATE_EXAM_DESC = "Updates an existing exam's details and questions. The exam must not have started yet. Requires EXAM_UPDATE permission.";
	public static final String GET_EXAM_BY_ID_DESC = "Returns the complete exam details including all questions and choices. This is the admin view.";
	public static final String GET_EXAMS_BY_COLLEGE_DESC = "Returns a paginated list of exams that belong to the specified college.";
	public static final String GET_EXAMS_BY_DEPARTMENT_DESC = "Returns a paginated list of exams that belong to the specified department.";
	public static final String GET_EXAMS_BY_USER_DESC = "Returns a paginated list of exams created by the specified user.";
	public static final String GET_MY_EXAMS_DESC = "Returns a paginated list of exams created by the currently authenticated user.";
	public static final String GET_EXAMS_BY_SUBJECT_DESC = "Returns a paginated list of exams for the specified subject.";
	public static final String GET_ALL_EXAMS_DESC = "Returns a paginated list of all exams in the system. Use 'page' and 'size' query parameters.";
	public static final String GET_ALL_ACTIVE_EXAMS_DESC = "Returns a paginated list of all currently active exams.";
	public static final String SEARCH_EXAMS_DESC = "Searches exams by title or description keyword. Returns paginated results.";
	public static final String DEACTIVATE_EXAM_DESC = "Deactivates an exam without permanently deleting it (soft delete). Requires EXAM_DELETE permission.";
	public static final String DELETE_EXAM_DESC = "Permanently deletes an exam. The exam must not have started yet. Requires EXAM_DELETE permission.";
	public static final String GENERATE_QR_DESC = "Generates a QR code for the specified exam. Students scan this QR code to access the exam. Requires EXAM_CREATE permission.";
	public static final String UPLOAD_EXAM_QUESTIONS_DESC = "Uploads a PDF or CSV file and parses it to auto-generate questions for an existing exam. This endpoint is deprecated — use the create-with-file endpoint instead.";

	// ==================== User Summaries ====================
	public static final String GET_CURRENT_USER_PROFILE = "Retrieve the current user's profile";
	public static final String UPDATE_CURRENT_USER_PROFILE = "Update the current user's profile (name, phone)";
	public static final String CHANGE_USERNAME = "Change the current user's username";
	public static final String REQUEST_EMAIL_CHANGE = "Request an email address change (sends verification code)";
	public static final String CONFIRM_EMAIL_CHANGE = "Confirm email change using the verification code";
	public static final String GET_USER_BY_ID = "Retrieve a user by their ID (admin only)";
	public static final String GET_ALL_USERS = "List all users (paginated, admin only)";
	public static final String GET_USERS_BY_COLLEGE = "List users in a specific college (paginated)";
	public static final String GET_USERS_BY_DEPARTMENT = "List users in a specific department (paginated)";
	public static final String GET_USERS_BY_ROLE = "List users with a specific role (paginated)";
	public static final String UPDATE_USER_BY_ADMIN = "Update a user's role, college, or department (admin only)";
	public static final String DEACTIVATE_USER = "Deactivate a user account (admin only)";
	public static final String ACTIVATE_USER = "Reactivate a deactivated user account (admin only)";
	public static final String SEARCH_USERS = "Search users by keyword, role, college, department, or status (paginated)";

	// ==================== User Descriptions ====================
	public static final String GET_CURRENT_USER_PROFILE_DESC = "Returns the profile of the currently authenticated user, including name, email, role, college, and department.";
	public static final String UPDATE_CURRENT_USER_PROFILE_DESC = "Updates the authenticated user's own profile (first name, last name, phone number).";
	public static final String CHANGE_USERNAME_DESC = "Changes the authenticated user's username. After a successful change, the user must log in again.";
	public static final String REQUEST_EMAIL_CHANGE_DESC = "Initiates an email change by sending a verification code to the new email address. The change is not applied until confirmed.";
	public static final String CONFIRM_EMAIL_CHANGE_DESC = "Confirms the email change using the verification code sent to the new email address. After confirmation, the user must log in again.";
	public static final String GET_USER_BY_ID_DESC = "Returns the full details of a user by their ID. Requires USER_READ permission.";
	public static final String GET_ALL_USERS_DESC = "Returns a paginated list of all users in the system. Use 'page' and 'size' query parameters. Requires USER_READ permission.";
	public static final String GET_USERS_BY_COLLEGE_DESC = "Returns a paginated list of all users who belong to the specified college.";
	public static final String GET_USERS_BY_DEPARTMENT_DESC = "Returns a paginated list of all users who belong to the specified department.";
	public static final String GET_USERS_BY_ROLE_DESC = "Returns a paginated list of all users who have the specified role name.";
	public static final String UPDATE_USER_BY_ADMIN_DESC = "Allows an admin to update a user's role, college, department, or active status. Requires USER_UPDATE permission.";
	public static final String DEACTIVATE_USER_DESC = "Deactivates a user account, preventing them from logging in. Requires USER_UPDATE permission.";
	public static final String ACTIVATE_USER_DESC = "Reactivates a previously deactivated user account. Requires USER_UPDATE permission.";
	public static final String SEARCH_USERS_DESC = "Searches users by keyword (name/email), role, college, department, and/or active status. All filters are optional. Returns paginated results.";

	// ==================== Student Summaries ====================
	public static final String REGISTER_STUDENT = "Register a new student account and return the student profile";
	public static final String UPDATE_MY_STUDENT_PROFILE = "Update the current student's own profile";
	public static final String ADMIN_UPDATE_STUDENT = "Update a student's details by ID (admin only)";
	public static final String GET_STUDENT_BY_ID = "Retrieve a student by their ID";
	public static final String GET_ALL_STUDENTS = "List all students (paginated)";
	public static final String GET_ALL_ACTIVE_STUDENTS = "List all active students (paginated)";
	public static final String GET_MY_STUDENT_PROFILE = "Retrieve the current student's own profile";
	public static final String DELETE_STUDENT = "Permanently delete a student by ID";
	public static final String DEACTIVATE_STUDENT = "Deactivate a student by ID (soft delete)";
	public static final String ACTIVATE_STUDENT = "Reactivate a deactivated student by ID";
	public static final String SEARCH_STUDENTS = "Search students by code, academic year, or active status (paginated)";
	public static final String GET_STUDENTS_BY_DEPARTMENT = "List students in a specific department (paginated)";
	public static final String GET_STUDENTS_BY_COLLEGE = "List students in a specific college (paginated)";
	public static final String COUNT_STUDENTS_BY_YEAR = "Get the total number of students in a specific academic year";
	public static final String COUNT_ACTIVE_STUDENTS = "Get the total number of currently active students";

	// ==================== Student Descriptions ====================
	public static final String REGISTER_STUDENT_DESC = "Registers a new student by creating both a user account and a student profile. Returns the complete student profile on success.";
	public static final String UPDATE_MY_STUDENT_PROFILE_DESC = "Allows the currently authenticated student to update their own profile (student code, academic year).";
	public static final String ADMIN_UPDATE_STUDENT_DESC = "Allows an admin to update a student's code and academic year. Requires STUDENTS_UPDATE permission.";
	public static final String GET_STUDENT_BY_ID_DESC = "Returns the full details of a student by their ID. Requires STUDENTS_READ permission.";
	public static final String GET_ALL_STUDENTS_DESC = "Returns a paginated list of all students. Use 'page' and 'size' query parameters.";
	public static final String GET_ALL_ACTIVE_STUDENTS_DESC = "Returns a paginated list of all currently active students.";
	public static final String GET_MY_STUDENT_PROFILE_DESC = "Returns the profile of the currently authenticated student.";
	public static final String DELETE_STUDENT_DESC = "Permanently deletes a student record. This action cannot be undone. Requires STUDENTS_DELETE permission.";
	public static final String DEACTIVATE_STUDENT_DESC = "Deactivates a student account without deleting it (soft delete). Requires STUDENTS_DELETE permission.";
	public static final String ACTIVATE_STUDENT_DESC = "Reactivates a previously deactivated student account. Requires STUDENTS_UPDATE permission.";
	public static final String SEARCH_STUDENTS_DESC = "Searches students by code, academic year, and/or active status. All filters are optional. Returns paginated results.";
	public static final String GET_STUDENTS_BY_DEPARTMENT_DESC = "Returns a paginated list of all students in the specified department.";
	public static final String GET_STUDENTS_BY_COLLEGE_DESC = "Returns a paginated list of all students in the specified college.";
	public static final String COUNT_STUDENTS_BY_YEAR_DESC = "Returns the total count of students enrolled in the specified academic year.";
	public static final String COUNT_ACTIVE_STUDENTS_DESC = "Returns the total count of students whose accounts are currently active.";

	// ==================== Student Exam Session Summaries ====================
	public static final String VALIDATE_QR_TOKEN = "Validate a QR token and retrieve exam details";
	public static final String ACCESS_EXAM_VIA_QR = "Access an exam by scanning its QR code (validates token, availability, and time window)";
	public static final String VERIFY_STUDENT_CODE = "Verify the student's identity code before starting the exam";
	public static final String START_EXAM_SESSION = "Start a new exam session after all validations pass";
	public static final String SUBMIT_ANSWER = "Submit an answer for a specific question in an active session";
	public static final String END_EXAM_SESSION = "End the exam session, submit all answers, and calculate results";
	public static final String GET_SESSION_QUESTIONS = "Retrieve all questions for an active exam session";
	public static final String GET_MY_EXAM_SESSIONS = "List all exam sessions for the currently authenticated student";
	public static final String GET_SESSION_BY_ID = "Retrieve a specific exam session by ID (e.g., for resuming)";

	// ==================== Student Exam Session Descriptions ====================
	public static final String ACCESS_EXAM_VIA_QR_DESC = "Validates the QR token and checks that the exam is currently available (active, within the time window). Returns exam details if valid.";
	public static final String VERIFY_STUDENT_CODE_DESC = "Verifies the student's identity code against the authenticated user before allowing them to start the exam.";
	public static final String START_EXAM_SESSION_DESC = "Starts a new timed exam session after verifying the QR token, exam availability, and student identity. Returns the session details.";
	public static final String SUBMIT_ANSWER_DESC = "Submits or updates an answer for a specific question within an active exam session. For MCQ/TRUE_FALSE, provide choiceId. For ESSAY, provide answerText.";
	public static final String END_EXAM_SESSION_DESC = "Ends the exam session, finalizes all submitted answers, and triggers automatic grading. The session cannot be resumed after this.";
	public static final String GET_SESSION_QUESTIONS_DESC = "Returns all questions (with choices for MCQ/TRUE_FALSE) for the specified active exam session.";
	public static final String GET_MY_EXAM_SESSIONS_DESC = "Returns a list of all exam sessions (past and current) for the currently authenticated student.";
	public static final String GET_SESSION_BY_ID_DESC = "Returns the details of a specific exam session. Useful for resuming an in-progress session.";
	public static final String START_EXAM_SESSION_LEGACY_DESC = "Legacy endpoint for starting an exam session. Use POST /api/sessions/start instead.";
}
