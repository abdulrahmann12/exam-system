---
title: Exam System
emoji: 📝
colorFrom: blue
colorTo: purple
sdk: docker
pinned: false
---

# 📝 Exam System — Online Examination Platform (Backend)

A comprehensive Spring Boot backend for an **Online Exam Management System** that supports user/student management, exam creation with multiple question types (manual or AI-parsed from PDF/CSV files), QR-code-based exam access, timed exam sessions, auto-grading, and result processing. Built with a clean layered architecture, JWT-based security, asynchronous email notifications via RabbitMQ, and full Swagger/OpenAPI documentation.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Architecture](#2-architecture)
3. [Main Features](#3-main-features)
4. [Authentication & Security](#4-authentication--security)
5. [API Structure](#5-api-structure)
6. [Database Model](#6-database-model)
7. [Exam Flow](#7-exam-flow)
8. [Technologies Used](#8-technologies-used)
9. [Folder Structure](#9-folder-structure)
10. [API Usage Examples](#10-api-usage-examples)
11. [Configuration & Environment Variables](#11-configuration--environment-variables)
12. [Running the Project](#12-running-the-project)

---

## 1. Project Overview

The **Exam System** is a backend REST API that powers an online examination platform designed for academic institutions. It enables administrators and instructors to create and manage exams while students can access exams via QR codes, answer questions within timed sessions, and receive auto-graded results.

### Key Capabilities

- **Multi-role access control** — Admins, instructors, and students each have distinct permission sets.
- **Full exam lifecycle** — From exam creation with questions and choices (manual or AI-parsed from PDF/CSV), to QR code generation, student session management, answer submission, and automated result calculation.
- **AI-powered exam upload** — Upload a PDF or CSV file, and questions are automatically parsed via an external Python microservice and added to the exam.
- **Academic hierarchy** — College → Department → Subject → Exam, with strict relationship validation.
- **Asynchronous email notifications** — Welcome emails, password reset codes, and email change verification sent via RabbitMQ consumers and Brevo SMTP.
- **Secure by default** — Stateless JWT authentication, refresh token rotation, permission-based authorization, CORS, and security headers.

---

## 2. Architecture

The project follows a **layered architecture** pattern common in enterprise Spring Boot applications:

```
┌─────────────────────────────────────────────────┐
│                   Client / Frontend              │
└──────────────────────┬──────────────────────────┘
                       │  HTTP / REST
┌──────────────────────▼──────────────────────────┐
│              Controller Layer                    │
│  (REST endpoints, request validation, routing)   │
└──────────────────────┬──────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────┐
│               Service Layer                      │
│  (Business logic, validation, orchestration)     │
└──────────┬───────────┬──────────────────────────┘
           │           │
┌──────────▼───┐ ┌─────▼─────────────────────────┐
│  Repository  │ │  External Services              │
│  Layer (JPA) │ │  (Email, Cloudinary, RabbitMQ)  │
└──────────┬───┘ └────────────────────────────────┘
           │
┌──────────▼──────────────────────────────────────┐
│              Database (MySQL)                    │
└─────────────────────────────────────────────────┘
```

### Layer Descriptions

| Layer | Package | Responsibility |
|-------|---------|----------------|
| **Controller** | `controller/` | Receives HTTP requests, performs input validation via `@Valid`, delegates to services, and returns standardized `BasicResponse` JSON. |
| **Service** | `service/` | Contains all business logic — authentication, exam creation/validation, session management, QR code generation, AI-powered file upload (PDF/CSV parsing via external Python microservice), email sending, image upload, etc. |
| **Repository** | `repository/` | Spring Data JPA interfaces for database access. Custom query methods for optimized fetching (e.g., `findByIdWithExam`, `findQuestionsWithChoices`). |
| **DTO** | `dto/` | Data Transfer Objects for request/response payloads. Separates internal entity structure from the API contract. Includes validation annotations. |
| **Entity** | `entities/` | JPA entities mapped to database tables. Includes auditing fields (`createdAt`, `updatedAt`) via `@PrePersist` / `@PreUpdate`. |
| **Mapper** | `mapper/` | MapStruct-based mappers for converting between entities and DTOs. Eliminates boilerplate mapping code. |
| **Config** | `config/` | Spring Security configuration, JWT filter, Swagger/OpenAPI setup, CORS, caching (Caffeine), Cloudinary, and password encoding. |
| **Exception** | `exception/` | Custom exception classes and a `GlobalExceptionHandler` (`@ControllerAdvice`) that maps exceptions to appropriate HTTP status codes. |
| **Events / Consumer / RabbitConfig** | `events/`, `consumer/`, `rabbitconfig/` | Asynchronous event-driven architecture using RabbitMQ for email notifications (user registration, password reset, email change, code regeneration). |

### Cross-Cutting Concerns

- **Security** — `JwtAuthenticationFilter` intercepts requests, validates JWT tokens, and sets Spring Security context with permission-based authorities.
- **Validation** — Jakarta Bean Validation (`@NotBlank`, `@Email`, `@Size`, etc.) on DTOs; `@Validated` on services for method-level validation. `ExamValidationHelper` centralizes reusable exam/session validation logic (QR token checks, session ownership, expiry enforcement).
- **Error Handling** — Centralized `GlobalExceptionHandler` catches 70+ custom exceptions and returns consistent error responses.
- **Caching** — Caffeine cache configuration for frequently accessed data.
- **External Service Integration** — `ExamUploadService` communicates with an external Python microservice via `RestTemplate` for AI-powered PDF/CSV question parsing.

---

## 3. Main Features

### 🔐 User Authentication & Account Management
- Login with username or email + password
- JWT access token + refresh token issuance
- Refresh token rotation (old tokens revoked on login)
- Password change (authenticated) and password reset (via email code)
- Email change with verification code
- Username change
- Account activation / deactivation by admin
- Welcome email on registration (async via RabbitMQ)

### 👩‍🎓 Student Management
- Self-registration with student code and academic year
- Profile management (view/update own profile)
- Admin CRUD operations on students
- Search/filter by student code, academic year, department, college, active status
- Activate / deactivate / delete students

### 🏫 Academic Structure
- **Colleges** — CRUD with search, enforced uniqueness
- **Departments** — CRUD with college association, search
- **Subjects** — CRUD with unique subject codes, department + college association

### 📝 Exam Management
- Create exams with full question and choice definitions in a single request
- **AI-powered file upload** — Create exams by uploading a PDF or CSV file; questions are auto-parsed via an external Python microservice
- Upload questions to an existing exam from a file (legacy, deprecated)
- Supports three question types: **MCQ**, **TRUE_FALSE**, **ESSAY**
- Configurable settings: duration, per-question time, back navigation, question randomization
- Time window enforcement (start time / end time)
- Exam update (locked after start time)
- Exam deactivation and deletion
- Search exams by keyword; filter by college, department, subject, or creator
- **QR code generation** — generates a unique QR token per exam, uploaded to Cloudinary

### 📱 QR-Based Exam Access
- Admin/instructor generates a QR code for an exam
- Student scans QR code to access the exam
- QR tokens expire after a configurable duration (default: 2 minutes)
- Multi-step verification before starting a session

### ✍️ Exam Sessions & Answer Submission
- Students start a timed session after QR + identity verification
- Fetch exam questions (with randomization if enabled; correct answers hidden)
- Submit/update answers per question during active session
- MCQ/TRUE_FALSE answers auto-graded on submission
- Essay answers stored for manual grading
- End session (idempotent) triggers result calculation

### 📊 Result Processing
- Automatic score calculation when session ends
- Total marks and max marks recorded per session
- Result linked 1:1 with `StudentExamSession`
- Student can view their exam history and results

### 🔑 Role-Based Access Control (RBAC)
- Dynamic roles with assignable permissions
- Permissions follow `MODULE_ACTION` convention (e.g., `EXAM_CREATE`, `USER_READ`)
- Permissions embedded in JWT claims for stateless authorization
- `@PreAuthorize("hasAuthority('...')")` on every endpoint

### 📧 Asynchronous Email Notifications
- RabbitMQ message broker with dedicated exchange and queues
- Events: `UserRegisteredEvent`, `PasswordResetRequestedEvent`, `EmailChangeEvent`, `CodeRegeneratedEvent`
- Consumers process messages and send HTML emails via Brevo SMTP using Thymeleaf templates

### 🖼️ Image Upload
- Cloudinary integration for image hosting (QR codes, profile images)

---

## 4. Authentication & Security

### JWT Authentication Flow

```
1. Client sends POST /api/auth/login with credentials
2. Server validates credentials via AuthenticationManager
3. Server generates:
   - Access Token (JWT, short-lived) — contains user email + permissions[]
   - Refresh Token (opaque, hashed in DB, configurable expiry)
4. Client stores both tokens
5. Client sends Access Token in Authorization: Bearer <token> header
6. JwtAuthenticationFilter validates token, extracts permissions, sets SecurityContext
7. When access token expires, client sends refresh token to POST /api/auth/refresh-token
8. Server issues new access token + new refresh token (rotation)
```

### Token Structure

**Access Token (JWT) Claims:**
```json
{
  "sub": "user@example.com",
  "permissions": ["EXAM_CREATE", "EXAM_READ", "USER_READ"],
  "iat": 1710000000,
  "exp": 1710003600
}
```

**Refresh Token:**
- Random 64-byte Base64 string
- Stored in `tokens` table as SHA-256 hash
- Has `expired`, `revoked`, and `expiresAt` fields
- All previous refresh tokens revoked on login and password change

### Spring Security Configuration

- **Stateless** session management (`SessionCreationPolicy.STATELESS`)
- **CORS** configured with allowed origins from environment variable
- **CSRF** disabled (stateless JWT auth)
- **Security headers**: XSS protection, frame options (deny), content type options
- **Public endpoints**: login, register (student), forget/reset password, refresh token, logout, Swagger, actuator health
- **All other endpoints** require authentication
- **Method-level security** via `@EnableMethodSecurity` and `@PreAuthorize`

### Permission System

Permissions use a `MODULE_ACTION` naming convention:

| Module | Actions |
|--------|---------|
| `USER` | `CREATE`, `READ`, `UPDATE`, `DELETE` |
| `STUDENTS` | `CREATE`, `READ`, `UPDATE`, `DELETE` |
| `EXAM` | `CREATE`, `READ`, `UPDATE`, `DELETE` |
| `ROLE` | `CREATE`, `READ`, `UPDATE`, `DELETE` |
| `PERMISSION` | `CREATE`, `READ`, `UPDATE`, `DELETE` |
| `COLLEGE` | `CREATE`, `READ`, `UPDATE`, `DELETE` |
| `DEPARTMENT` | `CREATE`, `READ`, `UPDATE`, `DELETE` |
| `SUBJECT` | `CREATE`, `READ`, `UPDATE`, `DELETE` |

Roles are dynamic — permissions are assigned to roles via a many-to-many `role_permissions` join table. Users are assigned one role. Permissions are included in the JWT at login time.

---

## 5. API Structure

**Base URL:** `http://localhost:7860/api`

All responses follow a standard envelope:

```json
{
  "message": "Success message",
  "data": { ... },
  "timestamp": "2026-03-15T12:00:00.000Z"
}
```

### Endpoint Groups

#### 🔐 Auth — `/api/auth`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/login` | ❌ | Login with username/email + password |
| `POST` | `/refresh-token` | ❌ | Refresh access token using refresh token |
| `POST` | `/logout` | ❌ | Invalidate refresh token |
| `POST` | `/create-user` | `USER_CREATE` | Admin creates a new user |
| `POST` | `/change-password` | ✅ Authenticated | Change own password |
| `POST` | `/regenerate-code` | ✅ Authenticated | Resend verification code |
| `POST` | `/forget-password` | ❌ | Request password reset code |
| `POST` | `/reset-password` | ❌ | Reset password with code |

#### 👤 Users — `/api/users`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/profile` | ✅ Authenticated | Get own profile |
| `PUT` | `/profile` | ✅ Authenticated | Update own profile |
| `PUT` | `/profile/username` | ✅ Authenticated | Change username |
| `POST` | `/profile/email/request` | ✅ Authenticated | Request email change |
| `POST` | `/profile/email/confirm` | ✅ Authenticated | Confirm email change with code |
| `GET` | `/{userId}` | `USER_READ` | Get user by ID |
| `GET` | `/` | `USER_READ` | Get all users (paginated) |
| `GET` | `/by-college/{collegeId}` | `USER_READ` | Get users by college |
| `GET` | `/by-department/{departmentId}` | `USER_READ` | Get users by department |
| `GET` | `/by-role?roleName=` | `USER_READ` | Get users by role |
| `GET` | `/search` | `USER_READ` | Search users (keyword, role, college, department, isActive) |
| `PUT` | `/{userId}` | `USER_UPDATE` | Admin update user |
| `DELETE` | `/{userId}` | `USER_UPDATE` | Deactivate user |
| `PUT` | `/{userId}/activate` | `USER_UPDATE` | Activate user |

#### 👩‍🎓 Students — `/api/students`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/register` | ❌ | Student self-registration |
| `GET` | `/me` | `STUDENTS_READ` | Get own student profile |
| `PUT` | `/me` | ✅ Authenticated | Update own student profile |
| `GET` | `/{studentId}` | `STUDENTS_READ` | Get student by ID |
| `GET` | `/` | `STUDENTS_READ` | Get all students (paginated) |
| `GET` | `/active` | `STUDENTS_READ` | Get all active students |
| `GET` | `/search` | `STUDENTS_READ` | Search students by code, year, active status |
| `GET` | `/department/{departmentId}` | `STUDENTS_READ` | Get students by department |
| `GET` | `/college/{collegeId}` | `STUDENTS_READ` | Get students by college |
| `GET` | `/count/year?year=` | `STUDENTS_READ` | Count students by academic year |
| `GET` | `/count/active` | `STUDENTS_READ` | Count active students |
| `PUT` | `/{studentId}` | `STUDENTS_UPDATE` | Admin update student |
| `POST` | `/{studentId}/activate` | `STUDENTS_UPDATE` | Activate student |
| `DELETE` | `/{studentId}/deactivate` | `STUDENTS_DELETE` | Deactivate student |
| `DELETE` | `/{studentId}/delete` | `STUDENTS_DELETE` | Delete student |

#### 📝 Exams — `/api/exams`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/` | `EXAM_CREATE` | Create exam with questions and choices |
| `POST` | `/upload` | `EXAM_CREATE` | Create exam with questions parsed from uploaded file (PDF/CSV) — multipart |
| `PUT` | `/{examId}` | `EXAM_UPDATE` | Update exam (locked after start time) |
| `GET` | `/{examId}` | `EXAM_READ` | Get full exam details (admin view) |
| `GET` | `/` | `EXAM_READ` | Get all exams (paginated) |
| `GET` | `/active` | `EXAM_READ` | Get all active exams |
| `GET` | `/my` | `EXAM_READ` | Get exams created by current user |
| `GET` | `/college/{collegeId}` | `EXAM_READ` | Get exams by college |
| `GET` | `/department/{departmentId}` | `EXAM_READ` | Get exams by department |
| `GET` | `/subject/{subjectId}` | `EXAM_READ` | Get exams by subject |
| `GET` | `/user/{userId}` | `EXAM_READ` | Get exams by creator user |
| `GET` | `/search?keyword=` | `EXAM_READ` | Search exams by keyword |
| `POST` | `/{examId}/qr` | `EXAM_CREATE` | Generate QR code for exam |
| `POST` | `/{examId}/upload-questions` | `EXAM_CREATE` | ⚠️ *Deprecated* — Upload questions file to existing exam |
| `DELETE` | `/deactivate/{examId}` | `EXAM_DELETE` | Deactivate exam (soft delete) |
| `DELETE` | `/{examId}` | `EXAM_DELETE` | Delete exam permanently |

#### 🎯 Exam Sessions — `/api/sessions`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/access?token=` | ❌ | Step 1: Access exam via QR token |
| `POST` | `/verify-student` | ✅ Authenticated | Step 2: Verify student code |
| `POST` | `/start` | ✅ Authenticated | Step 3: Start exam session |
| `GET` | `/{sessionId}/questions` | ✅ Authenticated | Step 4: Get exam questions |
| `POST` | `/submit-answer` | ✅ Authenticated | Step 5: Submit/update an answer |
| `POST` | `/end/{sessionId}` | ✅ Authenticated | Step 6: End session & calculate result |
| `GET` | `/my-exams` | ✅ Authenticated | Get all my exam sessions (with status & results) |
| `GET` | `/{sessionId}` | ✅ Authenticated | Get session details by ID (for resuming) |
| `POST` | `/start/{examId}?token=` | ✅ Authenticated | ⚠️ *Deprecated* — Legacy start session (no student code) |

#### 🏫 Colleges — `/api/colleges`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/` | `COLLEGE_CREATE` | Create college |
| `PUT` | `/{collegeId}` | `COLLEGE_UPDATE` | Update college |
| `GET` | `/{collegeId}` | `COLLEGE_READ` | Get college by ID |
| `GET` | `/by-name?collegeName=` | `COLLEGE_READ` | Get college by name |
| `GET` | `/` | `COLLEGE_READ` | Get all colleges (paginated) |
| `GET` | `/search?keyword=` | `COLLEGE_READ` | Search colleges |
| `DELETE` | `/{collegeId}` | `COLLEGE_DELETE` | Delete college |

#### 🏢 Departments — `/api/departments`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/` | `DEPARTMENT_CREATE` | Create department |
| `PUT` | `/{departmentId}` | `DEPARTMENT_UPDATE` | Update department |
| `GET` | `/{departmentId}` | `DEPARTMENT_READ` | Get department by ID |
| `GET` | `/by-name?departmentName=` | `DEPARTMENT_READ` | Get department by name |
| `GET` | `/` | `DEPARTMENT_READ` | Get all departments (paginated) |
| `GET` | `/by-college/{collegeId}` | `DEPARTMENT_READ` | Get departments by college |
| `GET` | `/search?keyword=` | `DEPARTMENT_READ` | Search departments |
| `DELETE` | `/{departmentId}` | `DEPARTMENT_DELETE` | Delete department |

#### 📚 Subjects — `/api/subjects`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/` | `SUBJECT_CREATE` | Create subject |
| `PUT` | `/{subjectId}` | `SUBJECT_UPDATE` | Update subject |
| `GET` | `/{subjectId}` | `SUBJECT_READ` | Get subject by ID |
| `GET` | `/` | `SUBJECT_READ` | Get all subjects (paginated) |
| `GET` | `/by-department/{departmentId}` | `SUBJECT_READ` | Get subjects by department |
| `GET` | `/by-college/{collegeId}` | `SUBJECT_READ` | Get subjects by college |
| `GET` | `/search?keyword=` | `SUBJECT_READ` | Search subjects |
| `DELETE` | `/{subjectId}` | `SUBJECT_DELETE` | Delete subject |

#### 🔑 Roles — `/api/roles`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/` | `ROLE_CREATE` | Create role |
| `PUT` | `/{roleId}` | `ROLE_UPDATE` | Update role |
| `GET` | `/{roleId}` | `ROLE_READ` | Get role by ID |
| `GET` | `/by-name?roleName=` | `ROLE_READ` | Get role by name |
| `GET` | `/` | `ROLE_READ` | Get all roles (paginated) |
| `DELETE` | `/{roleId}` | `ROLE_DELETE` | Delete role |

#### 🛡️ Permissions — `/api/permissions`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/` | `PERMISSION_CREATE` | Create permission |
| `PUT` | `/{permissionId}` | `PERMISSION_UPDATE` | Update permission |
| `GET` | `/{permissionId}` | `PERMISSION_READ` | Get permission by ID |
| `GET` | `/` | `PERMISSION_READ` | Get all permissions (paginated) |
| `GET` | `/modules` | `PERMISSION_READ` | Get all permission module names |
| `GET` | `/actions` | `PERMISSION_READ` | Get all permission action names |
| `DELETE` | `/{permissionId}` | `PERMISSION_DELETE` | Delete permission |

---

## 6. Database Model

### Entity Relationship Diagram (Textual)

```
┌──────────┐       ┌──────────┐       ┌──────────────┐
│  College  │──1:N──│Department│──1:N──│   Subject    │
└──────────┘       └──────────┘       └──────────────┘
     │                   │                    │
     │                   │                    │
     ├───────────────────┤                    │
     │                   │                    │
     ▼                   ▼                    ▼
┌──────────┐       ┌──────────┐         ┌─────────┐
│   User   │──N:1──│   Role   │         │  Exam   │
└──────────┘       └──────────┘         └─────────┘
     │                   │                    │
     │                   │                    │
     │              N:M  │               1:N  │
     │                   ▼                    ▼
     │            ┌────────────┐       ┌──────────┐
     │            │ Permission │       │ Question │
     │            └────────────┘       └──────────┘
     │                                       │
     ▼                                  1:N  │
┌──────────┐                                 ▼
│ Student  │◄──1:1──(User)            ┌──────────┐
└──────────┘                          │  Choice  │
     │                                └──────────┘
     │
     ▼
┌────────────────────┐       ┌───────────────┐
│StudentExamSession  │──1:N──│ StudentAnswer │
└────────────────────┘       └───────────────┘
     │
     ▼
┌──────────┐
│  Result  │ (1:1 with Session)
└──────────┘

┌──────────┐
│  Token   │──N:1──(User)   [Refresh tokens]
└──────────┘
```

### Entity Details

#### `User` (table: `users`)
Implements Spring Security `UserDetails`. Represents admins, instructors, and is also the base for students.

| Field | Type | Description |
|-------|------|-------------|
| `userId` | Long (PK, auto) | Primary key |
| `username` | String (unique) | Login username |
| `email` | String (unique) | Login email |
| `password` | String | BCrypt hashed password |
| `firstName`, `lastName` | String | Personal info |
| `phone` | String (unique) | Phone number |
| `isActive` | Boolean | Account active flag (default: true) |
| `role` | Role (ManyToOne) | User's assigned role |
| `college` | College (ManyToOne) | Associated college |
| `department` | Department (ManyToOne) | Associated department |
| `requestCode` | String | Verification/reset code |
| `requestCodeExpiry` | LocalDateTime | Code expiration time |
| `pendingEmail` | String | New email awaiting verification |
| `createdAt`, `updatedAt` | LocalDateTime | Audit timestamps |

#### `Student` (table: `students`)
Extends `User` via a `@OneToOne @MapsId` relationship (shared primary key).

| Field | Type | Description |
|-------|------|-------------|
| `studentId` | Long (PK = userId) | Same as user ID |
| `user` | User (OneToOne) | Linked user account |
| `studentCode` | String (unique) | University student code |
| `academicYear` | Integer | Current academic year |
| `isActive` | Boolean | Student active flag |
| `deactivatedAt` | LocalDateTime | When deactivated |
| `enrolledAt` | LocalDateTime | Enrollment timestamp |

#### `Exam` (table: `exams`)

| Field | Type | Description |
|-------|------|-------------|
| `examId` | Long (PK, auto) | Primary key |
| `title` | String | Exam title |
| `description` | String | Exam description |
| `college`, `department`, `subject` | ManyToOne | Academic hierarchy |
| `createdBy` | User (ManyToOne) | Instructor who created the exam |
| `durationMinutes` | Integer | Total exam duration |
| `perQuestionTimeSeconds` | Integer | Time per question |
| `allowBackNavigation` | Boolean | Can student go back? |
| `randomizeQuestions` | Boolean | Shuffle question order? |
| `totalQuestions` | Integer | Cached question count |
| `isActive` | Boolean | Exam active flag |
| `startTime`, `endTime` | LocalDateTime | Exam availability window |
| `qrCodeUrl` | String | Cloudinary URL of QR image |
| `qrToken` | String | Unique QR token |
| `qrExpiresAt` | LocalDateTime | QR token expiry |
| `questions` | List\<Question\> | Ordered list of questions |

#### `Question` (table: `questions`)

| Field | Type | Description |
|-------|------|-------------|
| `questionId` | Long (PK, auto) | Primary key |
| `questionText` | String (2000) | Question content |
| `questionType` | Enum: `MCQ`, `TRUE_FALSE`, `ESSAY` | Question type |
| `marks` | Integer | Points for this question |
| `questionOrder` | Integer | Display order in exam |
| `exam` | Exam (ManyToOne) | Parent exam |
| `choices` | List\<Choice\> | Ordered list of choices |

#### `Choice` (table: `choices`)

| Field | Type | Description |
|-------|------|-------------|
| `choiceId` | Long (PK, auto) | Primary key |
| `choiceText` | String (1000) | Choice content |
| `isCorrect` | Boolean | Is this the correct answer? |
| `choiceOrder` | Integer | Display order |
| `question` | Question (ManyToOne) | Parent question |

#### `StudentExamSession` (table: `student_exam_sessions`)
Unique constraint on `(exam_id, student_id)` — one session per student per exam.

| Field | Type | Description |
|-------|------|-------------|
| `sessionId` | Long (PK, auto) | Primary key |
| `exam` | Exam (ManyToOne) | Associated exam |
| `student` | Student (ManyToOne) | Associated student |
| `sessionCode` | String (unique) | UUID session identifier |
| `isActive` | Boolean | Session in progress? |
| `startedAt` | LocalDateTime | Session start time |
| `endedAt` | LocalDateTime | Session end time |
| `answers` | List\<StudentAnswer\> | Submitted answers |

#### `StudentAnswer` (table: `student_answers`)

| Field | Type | Description |
|-------|------|-------------|
| `answerId` | Long (PK, auto) | Primary key |
| `question` | Question (ManyToOne) | Answered question |
| `studentSession` | StudentExamSession (ManyToOne) | Parent session |
| `choice` | Choice (ManyToOne, nullable) | Selected choice (MCQ/TRUE_FALSE) |
| `answerText` | String (nullable) | Text answer (ESSAY) |
| `isCorrect` | Boolean (nullable) | Auto-graded result (null for essays) |
| `answeredAt` | LocalDateTime | Submission timestamp |

#### `Result` (table: `results`)

| Field | Type | Description |
|-------|------|-------------|
| `resultId` | Long (PK, auto) | Primary key |
| `studentSession` | StudentExamSession (OneToOne) | Linked session |
| `totalMark` | Integer | Achieved score |
| `maxMark` | Integer | Maximum possible score |
| `submittedAt` | LocalDateTime | Result calculation timestamp |

#### `Role` (table: `roles`)

| Field | Type | Description |
|-------|------|-------------|
| `roleId` | Long (PK, auto) | Primary key |
| `roleName` | String | Role name (e.g., "ADMIN", "INSTRUCTOR", "STUDENT") |
| `permissions` | Set\<Permission\> (ManyToMany) | Assigned permissions |

#### `Permission` (table: `permissions`)

| Field | Type | Description |
|-------|------|-------------|
| `permissionId` | Long (PK, auto) | Primary key |
| `code` | String (unique) | Permission code (e.g., "EXAM_CREATE") |
| `description` | String | Human-readable description |
| `active` | Boolean | Is permission active? |

#### `Token` (table: `tokens`)

| Field | Type | Description |
|-------|------|-------------|
| `id` | Long (PK, auto) | Primary key |
| `token` | String (unique) | SHA-256 hash of refresh token |
| `expired` | boolean | Token expired flag |
| `revoked` | boolean | Token revoked flag |
| `user` | User (ManyToOne) | Token owner |
| `expiresAt` | LocalDateTime | Expiration timestamp |

---

## 7. Exam Flow

The exam flow is a multi-step process with security checks at each stage:

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  1. Generate │     │  2. Student  │     │  3. Verify   │
│   QR Code    │────▶│  Scans QR    │────▶│  Student ID  │
│  (Instructor)│     │  (Public)    │     │ (Auth'd)     │
└──────────────┘     └──────────────┘     └──────────────┘
                                                │
                                                ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  6. End      │     │  5. Submit   │     │  4. Start    │
│  Session &   │◀────│  Answers     │◀────│  Session     │
│  Auto-Grade  │     │  (per Q)     │     │ (Auth'd)     │
└──────────────┘     └──────────────┘     └──────────────┘
```

### Step-by-Step

1. **QR Code Generation** (Instructor)
   - `POST /api/exams/{examId}/qr`
   - Generates a UUID `qrToken`, sets `qrExpiresAt` (default 2 min), creates QR code image via ZXing, uploads to Cloudinary, saves URL.
   - Returns QR code URL and token.

2. **Student Scans QR Code** (Public)
   - `GET /api/sessions/access?token={qrToken}`
   - Validates: exam exists, is active, QR token matches, QR not expired, exam within time window.
   - Returns exam summary (title, description, duration, total questions).

3. **Student Verifies Identity** (Authenticated)
   - `POST /api/sessions/verify-student`
   - Body: `{ examId, studentCode, token }`
   - Re-validates QR token and exam availability.
   - Verifies the `studentCode` matches the authenticated user's student record.
   - Checks no existing session for this student + exam.

4. **Start Exam Session** (Authenticated)
   - `POST /api/sessions/start`
   - Body: `{ examId, token, studentCode }`
   - Final validation of all conditions.
   - Creates `StudentExamSession` with unique `sessionCode` (UUID), records `startedAt`.
   - Returns session details (sessionId, sessionCode, startedAt).

5. **Answer Questions** (Authenticated)
   - `GET /api/sessions/{sessionId}/questions` — Fetches all questions (shuffled if enabled). Correct answers are **never** exposed to the student.
   - `POST /api/sessions/submit-answer` — Submit or update one answer:
     ```json
     { "sessionId": 1, "questionId": 5, "choiceId": 12 }
     ```
     or for essay:
     ```json
     { "sessionId": 1, "questionId": 6, "answerText": "My essay answer..." }
     ```
   - Validates: session ownership, session active, duration not expired, question belongs to exam, choice belongs to question.
   - MCQ/TRUE_FALSE: auto-graded (`isCorrect` set immediately).
   - ESSAY: stored as text (`isCorrect` = null, requires manual grading).

6. **End Session & Auto-Grade** (Authenticated)
   - `POST /api/sessions/end/{sessionId}`
   - Marks session as inactive, records `endedAt`.
   - Calculates total score by summing marks of correctly answered questions.
   - Creates `Result` record with `totalMark` and `maxMark`.
   - Idempotent — calling again on an already-ended session is a no-op.

---

## 8. Technologies Used

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 21 | Programming language |
| **Spring Boot** | 3.2.5 | Application framework |
| **Spring Security** | 6.x | Authentication & authorization |
| **Spring Data JPA** | 3.x | Database access (ORM) |
| **Spring AMQP (RabbitMQ)** | 3.x | Async message broker for emails |
| **Spring WebSocket** | 3.x | WebSocket support |
| **Spring Mail** | 3.x | Email sending (SMTP) |
| **Spring Validation** | 3.x | Bean validation |
| **MySQL** | 8.x | Production database |
| **H2** | 2.x | Test database |
| **JJWT (io.jsonwebtoken)** | 0.11.5 | JWT creation & validation |
| **MapStruct** | 1.5.5 | Entity ↔ DTO mapping |
| **Lombok** | 1.18.30 | Boilerplate reduction |
| **Caffeine** | 3.x | In-memory caching |
| **Google ZXing** | 3.5.3 | QR code generation |
| **Cloudinary** | 1.29.0 | Image/QR code cloud hosting |
| **Thymeleaf** | 3.x | HTML email templates |
| **OpenHTMLToPDF** | 1.0.10 | PDF generation |
| **SpringDoc OpenAPI** | 2.1.0 | Swagger UI / API documentation |
| **Maven** | 3.9+ | Build tool |
| **Docker** | — | Containerization |
| **Python PDF Parser** | External | AI-powered microservice for parsing exam files (PDF/CSV) into structured questions |

---

## 9. Folder Structure

```
src/main/java/com/exam/exam_system/
├── ExamSystemApplication.java          # Main entry point
│
├── config/                             # Configuration classes
│   ├── AppConfig.java                  # BCrypt password encoder bean
│   ├── CacheConfig.java               # Caffeine cache configuration
│   ├── CloudinaryConfig.java          # Cloudinary SDK setup
│   ├── JwtAuthenticationFilter.java   # JWT token extraction & validation filter
│   ├── Messages.java                  # Centralized message constants
│   ├── SecurityConfig.java            # Spring Security filter chain, CORS, auth
│   ├── SwaggerConfig.java             # OpenAPI/Swagger configuration
│   └── SwaggerMessages.java           # Swagger operation summary constants
│
├── controller/                         # REST controllers (API endpoints)
│   ├── AuthController.java            # Login, register, password reset, refresh
│   ├── CollegeController.java         # College CRUD
│   ├── DepartmentController.java      # Department CRUD
│   ├── ExamController.java            # Exam CRUD, QR generation
│   ├── PermissionController.java      # Permission CRUD
│   ├── RoleController.java            # Role CRUD
│   ├── StudentController.java         # Student registration, CRUD, search
│   ├── StudentExamSessionController.java # Exam access, session, answers
│   ├── SubjectController.java         # Subject CRUD
│   └── UserController.java            # User profile, admin user management
│
├── dto/                                # Data Transfer Objects (70+ classes)
│   ├── BasicResponse.java             # Standard API response envelope
│   ├── AuthResponse.java              # Access + refresh tokens
│   ├── LoginRequestDTO.java           # Login request
│   ├── CreateExamRequestDTO.java      # Exam creation with nested questions
│   ├── CreateStudentAnswerRequestDTO.java # Answer submission
│   ├── StartExamRequestDTO.java       # Session start request
│   ├── VerifyStudentCodeRequestDTO.java # Student verification
│   ├── StudentRegisterRequestDTO.java # Student self-registration
│   ├── ExamAccessResponseDTO.java     # QR access response
│   ├── ExamQuestionsResponseDTO.java  # Questions for student view
│   ├── QrProperties.java             # QR expiration config properties
│   ├── RefreshTokenProperties.java    # Refresh token config properties
│   ├── ParsedExamDTO.java            # Parsed exam structure from Python service
│   ├── ParsedQuestionDTO.java        # Parsed question from file upload
│   ├── ParsedChoiceDTO.java          # Parsed choice from file upload
│   ├── CreateExamWithFileRequestDTO.java # Exam creation via file upload request
│   ├── UploadQuestionsResponseDTO.java # File upload response (question count)
│   ├── ExamStatisticsDTO.java        # Exam statistics (avg score, pass rate, etc.)
│   ├── ExamResultDTO.java            # Student exam result with grade
│   ├── QuestionResultDTO.java        # Per-question result breakdown
│   ├── StudentSessionOverviewDTO.java # Student session overview (my exams list)
│   ├── StudentExamRowDTO.java        # Per-student exam row (for admin views)
│   └── ...                            # (Many more request/response DTOs)
│
├── entities/                           # JPA entities (database models)
│   ├── User.java                      # User (implements UserDetails)
│   ├── Student.java                   # Student (extends User via @MapsId)
│   ├── Role.java                      # Role with permissions
│   ├── Permission.java                # Permission (code-based)
│   ├── PermissionActions.java         # Enum: CREATE, READ, UPDATE, DELETE
│   ├── PermissionModules.java         # Enum: USERS, EXAMS, ROLES, etc.
│   ├── College.java                   # College
│   ├── Department.java                # Department (belongs to College)
│   ├── Subject.java                   # Subject (belongs to Dept + College)
│   ├── Exam.java                      # Exam with settings and QR fields
│   ├── Question.java                  # Question with type and order
│   ├── QuestionType.java              # Enum: MCQ, TRUE_FALSE, ESSAY
│   ├── Choice.java                    # Answer choice for a question
│   ├── StudentExamSession.java        # Active exam session
│   ├── StudentAnswer.java             # Submitted answer
│   ├── Result.java                    # Calculated exam result
│   └── Token.java                     # Refresh token (hashed)
│
├── events/                             # RabbitMQ event payloads
│   ├── UserRegisteredEvent.java       # New user registration event
│   ├── PasswordResetRequestedEvent.java # Password reset code event
│   ├── EmailChangeEvent.java          # Email change verification event
│   └── CodeRegeneratedEvent.java      # Verification code regeneration event
│
├── consumer/                           # RabbitMQ message consumers
│   ├── UserRegisteredConsumer.java    # Sends welcome email
│   ├── PasswordResetConsumer.java     # Sends reset code email
│   ├── EmailChangeConsumer.java       # Sends email change code
│   └── CodeRegeneratedConsumer.java   # Sends regenerated code email
│
├── rabbitconfig/                       # RabbitMQ configuration
│   ├── RabbitConstants.java           # Exchange, queue, and routing key names
│   ├── RabbitCommonConfig.java        # JSON message converter
│   └── AuthRabbitConfig.java          # Exchange, queues, bindings setup
│
├── exception/                          # Custom exceptions (70+ classes)
│   ├── GlobalExceptionHandler.java    # @ControllerAdvice centralized handler
│   ├── UserNotFoundException.java
│   ├── ExamNotFoundException.java
│   ├── SessionExpiredException.java
│   ├── InvalidTokenException.java
│   └── ...                            # (Many domain-specific exceptions)
│
├── mapper/                             # MapStruct mappers
│   ├── UserMapper.java
│   ├── StudentMapper.java
│   ├── ExamMapper.java
│   ├── QuestionMapper.java
│   ├── ChoiceMapper.java
│   ├── StudentExamSessionMapper.java
│   ├── StudentAnswerMapper.java
│   ├── ResultMapper.java
│   ├── CollegeMapper.java
│   ├── DepartmentMapper.java
│   ├── SubjectMapper.java
│   ├── RoleMapper.java
│   └── PermissionMapper.java
│
├── repository/                         # Spring Data JPA repositories
│   ├── UserRepository.java
│   ├── StudentRepository.java
│   ├── ExamRepository.java
│   ├── QuestionRepository.java
│   ├── ChoiceRepository.java
│   ├── StudentExamSessionRepository.java
│   ├── StudentAnswerRepository.java
│   ├── ResultRepository.java
│   ├── TokenRepository.java
│   ├── CollegeRepository.java
│   ├── DepartmentRepository.java
│   ├── SubjectRepository.java
│   ├── RoleRepository.java
│   └── PermissionRepository.java
│
└── service/                            # Business logic services
    ├── AuthService.java               # Authentication, registration, password ops
    ├── JwtService.java                # JWT generation, validation, claims extraction
    ├── UserService.java               # User CRUD, profile, email change
    ├── StudentService.java            # Student CRUD, registration
    ├── ExamService.java               # Exam CRUD, QR generation
    ├── ExamUploadService.java         # AI-powered exam file upload (PDF/CSV parsing via Python service)
    ├── ExamAccessService.java         # QR access + student verification (Steps 1-2)
    ├── ExamSessionService.java        # Session lifecycle (Steps 3-4, 6)
    ├── ExamAnswerService.java         # Answer submission (Step 5)
    ├── ExamValidationHelper.java      # Shared validation logic (QR tokens, session ownership, expiry)
    ├── StudentExamSessionService.java # ⚠️ Deprecated — delegates to Access/Session/Answer services
    ├── QrCodeService.java             # QR code image generation (ZXing)
    ├── ImageService.java              # Cloudinary image upload
    ├── EmailService.java              # HTML email sending (Thymeleaf + SMTP)
    ├── CollegeService.java            # College CRUD
    ├── DepartmentService.java         # Department CRUD
    ├── SubjectService.java            # Subject CRUD
    ├── RoleService.java               # Role CRUD
    ├── PermissionService.java         # Permission CRUD
    └── BaseService.java               # Shared service utilities

src/main/resources/
├── application.properties              # App configuration (see Section 11)
└── templates/emails/
    ├── welcome.html                    # Welcome email template
    └── send-code.html                  # Verification code email template
```

---

## 10. API Usage Examples

### 10.1 Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "admin@example.com",
  "password": "SecurePass123"
}
```

**Response (200):**
```json
{
  "message": "Login successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "dGhpcyBpcyBhIHJhbmRvbSByZWZyZXNoIHRva2Vu..."
  },
  "timestamp": "2026-03-15T12:00:00.000Z"
}
```

### 10.2 Create an Exam (with questions and choices)

```http
POST /api/exams
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "title": "Midterm - Data Structures",
  "description": "Covers arrays, linked lists, stacks, and queues.",
  "collegeId": 1,
  "departmentId": 2,
  "subjectId": 5,
  "durationMinutes": 60,
  "perQuestionTimeSeconds": 120,
  "allowBackNavigation": true,
  "randomizeQuestions": false,
  "startTime": "2026-03-20T09:00:00",
  "endTime": "2026-03-20T11:00:00",
  "isActive": true,
  "questions": [
    {
      "questionText": "Which data structure uses FIFO?",
      "questionType": "MCQ",
      "marks": 5,
      "questionOrder": 1,
      "choices": [
        { "choiceText": "Stack", "isCorrect": false, "choiceOrder": 1 },
        { "choiceText": "Queue", "isCorrect": true, "choiceOrder": 2 },
        { "choiceText": "Tree", "isCorrect": false, "choiceOrder": 3 },
        { "choiceText": "Graph", "isCorrect": false, "choiceOrder": 4 }
      ]
    },
    {
      "questionText": "A stack follows LIFO principle.",
      "questionType": "TRUE_FALSE",
      "marks": 3,
      "questionOrder": 2,
      "choices": [
        { "choiceText": "True", "isCorrect": true, "choiceOrder": 1 },
        { "choiceText": "False", "isCorrect": false, "choiceOrder": 2 }
      ]
    },
    {
      "questionText": "Explain the difference between a stack and a queue.",
      "questionType": "ESSAY",
      "marks": 10,
      "questionOrder": 3,
      "choices": []
    }
  ]
}
```

### 10.3 Create an Exam with File Upload (AI-Parsed)

```http
POST /api/exams/upload
Authorization: Bearer <access_token>
Content-Type: multipart/form-data

--boundary
Content-Disposition: form-data; name="exam"
Content-Type: application/json

{
  "title": "Final - Operating Systems",
  "description": "Covers processes, threads, memory management.",
  "collegeId": 1,
  "departmentId": 2,
  "subjectId": 7,
  "durationMinutes": 90,
  "perQuestionTimeSeconds": 180,
  "allowBackNavigation": true,
  "randomizeQuestions": true,
  "startTime": "2026-04-10T09:00:00",
  "endTime": "2026-04-10T12:00:00"
}
--boundary
Content-Disposition: form-data; name="file"; filename="os-exam.pdf"
Content-Type: application/pdf

<binary PDF content>
--boundary--
```

**How it works:**
1. The backend sends the uploaded file to an external Python microservice (`https://abdulraahmann-pdf-exam-parser.hf.space/parse-questions`).
2. The Python service parses the PDF/CSV and extracts questions, choices, types, and marks.
3. The backend maps the parsed data to exam entities and saves everything in one transaction.

**Response (200):** Same structure as manual exam creation (Section 10.2).

### 10.4 Generate QR Code for Exam

```http
POST /api/exams/3/qr
Authorization: Bearer <access_token>
```

**Response (200):**
```json
{
  "message": "Fetched successfully",
  "data": {
    "examId": 3,
    "qrCodeUrl": "https://res.cloudinary.com/xxx/image/upload/qr_exam_3.png",
    "qrToken": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "expiresAt": "2026-03-20T09:02:00"
  },
  "timestamp": "2026-03-15T12:05:00.000Z"
}
```

### 10.5 Student Registration

```http
POST /api/students/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@university.edu",
  "password": "StudentPass123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "01012345678",
  "collegeId": 1,
  "departmentId": 2,
  "studentCode": "STU-2026-001",
  "academicYear": 3
}
```

### 10.6 Exam Flow — Complete Student Journey

**Step 1: Access exam via QR token (public)**
```http
GET /api/sessions/access?token=a1b2c3d4-e5f6-7890-abcd-ef1234567890
```

**Step 2: Verify student identity (authenticated)**
```http
POST /api/sessions/verify-student
Authorization: Bearer <student_access_token>
Content-Type: application/json

{
  "examId": 3,
  "studentCode": "STU-2026-001",
  "token": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

**Step 3: Start exam session (authenticated)**
```http
POST /api/sessions/start
Authorization: Bearer <student_access_token>
Content-Type: application/json

{
  "examId": 3,
  "token": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "studentCode": "STU-2026-001"
}
```

**Response:**
```json
{
  "message": "Exam session started successfully",
  "data": {
    "sessionId": 42,
    "sessionCode": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "startedAt": "2026-03-20T09:01:00"
  },
  "timestamp": "2026-03-20T09:01:00.000Z"
}
```

**Step 4: Get exam questions (authenticated)**
```http
GET /api/sessions/42/questions
Authorization: Bearer <student_access_token>
```

**Step 5: Submit answers (authenticated)**
```http
POST /api/sessions/submit-answer
Authorization: Bearer <student_access_token>
Content-Type: application/json

{
  "sessionId": 42,
  "questionId": 10,
  "choiceId": 25
}
```

**Step 6: End session (authenticated)**
```http
POST /api/sessions/end/42
Authorization: Bearer <student_access_token>
```

**Response:**
```json
{
  "message": "Exam submitted successfully",
  "data": null,
  "timestamp": "2026-03-20T09:55:00.000Z"
}
```

### 10.7 Refresh Access Token

```http
POST /api/auth/refresh-token
Authorization: Bearer <refresh_token>
```

### 10.8 Password Reset Flow

```http
# Step 1: Request reset code
POST /api/auth/forget-password
Content-Type: application/json

{ "email": "john@university.edu" }

# Step 2: Reset with code
POST /api/auth/reset-password
Content-Type: application/json

{
  "email": "john@university.edu",
  "code": "48291",
  "newPassword": "NewSecurePass456"
}
```

---

## 11. Configuration & Environment Variables

The application is configured via `application.properties` with environment variable overrides:

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:mysql://localhost:3306/test` | MySQL JDBC URL |
| `DB_USERNAME` | `test` | Database username |
| `DB_PASSWORD` | `test` | Database password |
| `JWT_SECRET` | — (required) | HMAC-SHA256 secret key for JWT signing |
| `JWT_EXPIRATION` | — (required) | Access token expiry in milliseconds |
| `JWT_REFRESH_EXPIRATION_MINUTES` | — (required) | Refresh token expiry in minutes |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:3000` | Comma-separated allowed origins |
| `MAIL_FROM` | — | Sender email address |
| `BREVO_USERNAME` | `test@example.com` | Brevo SMTP username |
| `BREVO_API_KEY` | `testpassword` | Brevo SMTP API key |
| `CLOUDINARY_NAME` | — | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | — | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | — | Cloudinary API secret |
| `CLOUDINARY_URL` | — | Full Cloudinary URL |
| `RABBITMQ_HOST` | `localhost` | RabbitMQ server host |
| `RABBITMQ_PORT` | `5672` | RabbitMQ server port |
| `RABBITMQ_USERNAME` | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | `guest` | RabbitMQ password |
| `SWAGGER_ENABLED` | `true` | Enable/disable Swagger UI |

**Server port:** `7860`

---

## 12. Running the Project

### Prerequisites

- Java 21+
- Maven 3.9+
- MySQL 8+
- RabbitMQ 3.x
- (Optional) Docker
- (Optional) Python PDF Parser microservice — required for the exam file upload feature (`POST /api/exams/upload`). Hosted at `https://abdulraahmann-pdf-exam-parser.hf.space/parse-questions`

### Local Development

```bash
# Clone the repository
git clone <repo-url>
cd exam-system

# Set environment variables (or use a .env file with your IDE)
export JWT_SECRET=your-256-bit-secret-key-here-must-be-long-enough
export JWT_EXPIRATION=3600000
export JWT_REFRESH_EXPIRATION_MINUTES=10080

# Build & run
./mvnw spring-boot:run
```

### Docker

```bash
# Build the image
docker build -t exam-system .

# Run the container
docker run -p 7860:7860 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/exam_db \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=secret \
  -e JWT_SECRET=your-secret-key \
  -e JWT_EXPIRATION=3600000 \
  -e JWT_REFRESH_EXPIRATION_MINUTES=10080 \
  exam-system
```

### Swagger UI

Once running, visit: [http://localhost:7860/swagger-ui.html](http://localhost:7860/swagger-ui.html)

### Health Check

```http
GET http://localhost:7860/actuator/health
```

---

## License

This project is for educational purposes.

---

*Last updated: March 2026 — this README provides a complete map of the backend system for building frontends, integrations, or extending the API.*