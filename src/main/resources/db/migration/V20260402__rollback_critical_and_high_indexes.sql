-- ============================================================================
-- ROLLBACK: Reverse all CRITICAL + HIGH index changes from V20260402
-- Date:      2026-04-02
-- Usage:     Run this ONLY if the migration causes issues in production
-- Strategy:  Drop new indexes first, then recreate the old ones
-- ============================================================================


-- ============================================================================
-- STEP 1: DROP the new indexes
-- ============================================================================

-- Reverse CRITICAL #1
DROP INDEX idx_exams_qr_token ON exams;

-- Reverse CRITICAL #2
DROP INDEX idx_sa_session_question ON student_answers;

-- Reverse CRITICAL #3
DROP INDEX idx_tokens_user_valid ON tokens;

-- Reverse HIGH #4
DROP INDEX idx_ses_student_started ON student_exam_sessions;


-- ============================================================================
-- STEP 2: RECREATE the old indexes that were dropped
-- ============================================================================

-- Restore original single-column token index
CREATE INDEX idx_tokens_user ON tokens(user_id);

-- If you dropped the auto FK index on student_answers.student_session_id,
-- InnoDB will need it back for the FK constraint. Uncomment if needed:
-- CREATE INDEX fk_sa_student_session ON student_answers(student_session_id);
