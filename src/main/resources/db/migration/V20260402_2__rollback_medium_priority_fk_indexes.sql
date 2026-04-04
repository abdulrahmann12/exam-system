-- ============================================================================
-- ROLLBACK: Reverse all MEDIUM priority FK index changes from V20260402_2
-- Date:      2026-04-02
-- Usage:     Run this ONLY if the migration causes issues in production
-- Strategy:  Drop all explicitly created indexes.
--            InnoDB's auto-generated FK indexes (if any) will remain intact.
-- ============================================================================

-- EXAMS
DROP INDEX idx_exams_college ON exams;
DROP INDEX idx_exams_department ON exams;
DROP INDEX idx_exams_subject ON exams;
DROP INDEX idx_exams_created_by ON exams;
DROP INDEX idx_exams_active ON exams;

-- USERS
DROP INDEX idx_users_role ON users;
DROP INDEX idx_users_college ON users;
DROP INDEX idx_users_department ON users;

-- STUDENTS
DROP INDEX idx_students_active ON students;
DROP INDEX idx_students_academic_year ON students;

-- DEPARTMENTS
DROP INDEX idx_departments_college ON departments;

-- SUBJECTS
DROP INDEX idx_subjects_department ON subjects;
DROP INDEX idx_subjects_college ON subjects;

-- ROLES
DROP INDEX idx_roles_name ON roles;
