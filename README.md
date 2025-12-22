# Online Exam System (Spring Boot)

A complete backend system for managing online exams inside universities.  
The project supports QR-based student access, question shuffling, per-question timing, session tracking, and full doctor/admin dashboards.

---

## Project Architecture

**Tech Stack**
- Java 17 / 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Security (JWT)
- MySQL Database
- Lombok
- Validation API
- ZXing (QR Code Generator)

---

## Features

### For Admin
- Manage Colleges
- Manage Departments
- Manage Doctors
- Assign Subjects to Doctors
- View all exams and system statistics

### For Doctor
- Create & manage exams
- Add questions (MCQ, True/False, Written)
- Set exam rules  
  - Duration  
  - Per-question time  
  - Allow back navigation  
  - Auto-submit  
  - Randomize questions
- View results for each student
- Generate **QR Code** for exam entry
- View active student sessions

### For Students
- Enter exam without an account  
  → via QR Code  
- Solve shuffled questions  
- System auto-tracks:
  - Leaving the tab (auto-submit)
  - Time remaining
  - Answers
  - Session status

---

## Database Schema (Main Entities)

### User & Roles
- `users` (Doctor/Admin)
- `roles`
- `doctor_subjects`

### University Structure
- `colleges`
- `departments`
- `subjects`

### Exam System
- `exams`
- `questions`
- `choices`
- `student_exam_sessions`
- `student_answers`
- `results`

---

## Authentication & Security

Implemented using:
- Spring Security 6
- JWT Access/Refresh tokens
- Role-based access:
  - `ADMIN`
  - `DOCTOR`

No student accounts → They enter via QR token.

---

## How the QR System Works

1. Doctor creates exam  
2. System generates **unique session code**  
3. Student scans QR → Session starts  
4. System monitors:
- Time per question
- Full exam time
- Tab switching
- Auto-submit on timeout

