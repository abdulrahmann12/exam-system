-- ============================================================================
-- Migration: Add CRITICAL + HIGH priority indexes
-- Date:      2026-04-02
-- Author:    Performance Engineering
-- Scope:     CRITICAL #1, #2, #3  |  HIGH #4
-- Strategy:  Safe ordering — CREATE new indexes first, then DROP redundant ones
-- ============================================================================

-- ============================================================================
-- STEP 1: PRE-CHECK (run manually before applying)
-- ============================================================================
-- Run these on production BEFORE applying to confirm current state:
--
--   SHOW INDEX FROM exams;
--     Expected: NO index on qr_token
--
--   SHOW INDEX FROM student_answers;
--     Expected: FK auto-indexes on student_session_id and question_id (single-column)
--     Expected: NO composite index on (student_session_id, question_id)
--
--   SHOW INDEX FROM tokens;
--     Expected: idx_tokens_user on user_id (single-column)
--     Expected: NO composite index on (user_id, expired, revoked)
--
--   SHOW INDEX FROM student_exam_sessions;
--     Expected: uk_student_exam_session on (exam_id, student_id)
--     Expected: NO index with student_id as leading column
-- ============================================================================


-- ============================================================================
-- STEP 2: CREATE NEW INDEXES (safe — additive only, no locks on reads)
-- ============================================================================

-- ---------------------------------------------------------------------------
-- CRITICAL #1: exams.qr_token
-- Query:    findByQrToken(String qrToken)
-- Problem:  Full table scan on every student QR code scan
-- Impact:   O(n) full scan → O(1) unique seek
-- ---------------------------------------------------------------------------
CREATE UNIQUE INDEX idx_exams_qr_token
    ON exams(qr_token);

-- ---------------------------------------------------------------------------
-- CRITICAL #2: student_answers(student_session_id, question_id)
-- Query:    findByStudentSession_SessionIdAndQuestion_QuestionId(sessionId, questionId)
--           findByStudentSession_SessionId(sessionId)
-- Problem:  Hottest path — every answer submission does a lookup with 2 columns;
--           MySQL cannot efficiently merge two single-column FK indexes
-- Impact:   Two partial index scans + row filter → single composite seek
--           Also enforces business rule: one answer per question per session
-- ---------------------------------------------------------------------------
CREATE UNIQUE INDEX idx_sa_session_question
    ON student_answers(student_session_id, question_id);

-- ---------------------------------------------------------------------------
-- CRITICAL #3: tokens(user_id, expired, revoked)
-- Query:    findAllValidTokensByUser   → WHERE user_id=? AND expired=0 AND revoked=0
--           revokeAllRefreshTokensByUser → UPDATE ... WHERE user_id=? AND expired=0 AND revoked=0
-- Problem:  idx_tokens_user only covers user_id; must row-scan all tokens for
--           that user to filter expired/revoked. Grows worse as tokens accumulate.
-- Impact:   Index seek on user_id + row filter → precise composite range scan
-- ---------------------------------------------------------------------------
CREATE INDEX idx_tokens_user_valid
    ON tokens(user_id, expired, revoked);

-- ---------------------------------------------------------------------------
-- HIGH #4: student_exam_sessions(student_id, started_at)
-- Query:    findAllByStudentWithExam → WHERE student_id=? ORDER BY startedAt DESC
-- Problem:  Existing unique index is (exam_id, student_id) — exam_id leads,
--           so student_id-only lookups cannot use it. Full scan + filesort.
-- Impact:   Full table scan + filesort → index range scan in sorted order
-- ---------------------------------------------------------------------------
CREATE INDEX idx_ses_student_started
    ON student_exam_sessions(student_id, started_at DESC);


-- ============================================================================
-- STEP 3: DROP REDUNDANT INDEXES
-- Only after confirming the new composites are active (SHOW INDEX FROM ...)
-- ============================================================================

-- The old single-column idx_tokens_user is now a left-prefix subset of
-- idx_tokens_user_valid. Keeping both wastes write I/O on every INSERT/UPDATE.
DROP INDEX idx_tokens_user ON tokens;

-- The InnoDB auto-generated FK index on student_answers.student_session_id
-- is now a left-prefix subset of idx_sa_session_question.
-- NOTE: MySQL auto-names FK indexes. Check the actual name with SHOW INDEX first.
-- Common auto-names: FKxxxxxxxx or student_session_id or fk_student_answers_session
-- Replace <auto_fk_index_name> with the actual name from SHOW INDEX output.
--
-- ⚠️  IMPORTANT: Do NOT drop this if it's the only index enforcing the FK constraint.
--     MySQL requires at least one index on FK columns. Since idx_sa_session_question
--     has student_session_id as its leading column, it satisfies the FK requirement.
--     Therefore the standalone FK index IS safe to drop.
--
-- Uncomment after confirming the auto-generated FK index name:
-- DROP INDEX <auto_fk_index_name> ON student_answers;
