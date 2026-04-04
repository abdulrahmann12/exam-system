-- ============================================================================
-- Migration: Add MEDIUM priority indexes (FK columns used in WHERE/JOIN filters)
-- Date:      2026-04-02
-- Author:    Performance Engineering
-- Scope:     FK indexes on exams, users, students, departments, subjects, roles
-- Strategy:  Safe ordering — CREATE only, no drops needed
-- Note:      InnoDB auto-creates indexes for FK constraints, but only simple
--            single-column ones. These explicit indexes ensure optimal coverage
--            and make intent clear. If InnoDB already created one with the same
--            column, MySQL will skip or maintain both (no conflict).
-- ============================================================================

-- ============================================================================
-- EXAMS: FK columns used in filtered listings
-- Queries:  findByCollege_CollegeId, findByCollegeId (paginated)
--           findByDepartment_DepartmentId, findByDepartmentId (paginated)
--           findBySubject_SubjectId, findBySubjectId (paginated)
--           findByCreatedBy_UserId, findByUserId (paginated)
--           findByIsActiveTrue, findAllActive, findExamFull (constant filter)
-- ============================================================================

CREATE INDEX idx_exams_college ON exams(college_id);

CREATE INDEX idx_exams_department ON exams(department_id);

CREATE INDEX idx_exams_subject ON exams(subject_id);

CREATE INDEX idx_exams_created_by ON exams(createdBy);

CREATE INDEX idx_exams_active ON exams(is_active);


-- ============================================================================
-- USERS: FK columns used in search/filter queries
-- Queries:  findByRole_RoleName → joins on role_id
--           findByCollege_CollegeId (paginated)
--           findByDepartment_DepartmentId (paginated)
--           searchUsers with role/college/department filters
-- ============================================================================

CREATE INDEX idx_users_role ON users(role_id);

CREATE INDEX idx_users_college ON users(college_id);

CREATE INDEX idx_users_department ON users(department_id);


-- ============================================================================
-- STUDENTS: Filter columns used in paginated queries
-- Queries:  findByIsActiveTrue (paginated)
--           countByIsActiveTrue
--           searchStudents with isActive filter
--           countByAcademicYear
--           searchStudents with academicYear filter
-- ============================================================================

CREATE INDEX idx_students_active ON students(is_active);

CREATE INDEX idx_students_academic_year ON students(academic_year);


-- ============================================================================
-- DEPARTMENTS: FK column used in listings
-- Queries:  findByCollege_CollegeId (paginated)
--           existsByCollege_CollegeId
--           existsByDepartmentNameAndCollege_CollegeId
-- ============================================================================

CREATE INDEX idx_departments_college ON departments(college_id);


-- ============================================================================
-- SUBJECTS: FK columns used in listings/existence checks
-- Queries:  findAllByDepartment_DepartmentId
--           existsByDepartment_DepartmentId
--           findAllByCollege_CollegeId
--           existsByCollege_CollegeId
-- Note:     UniqueConstraint on (subject_name, department_id) has subject_name
--           as leading column, so department_id-only lookups cannot use it.
-- ============================================================================

CREATE INDEX idx_subjects_department ON subjects(department_id);

CREATE INDEX idx_subjects_college ON subjects(college_id);


-- ============================================================================
-- ROLES: Name lookup used in auth flow
-- Queries:  findByRoleName
--           existsByRoleName
-- Note:     Small table, but queried on every auth request.
-- ============================================================================

CREATE INDEX idx_roles_name ON roles(role_name);
