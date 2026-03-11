package com.exam.exam_system.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exam.exam_system.entities.*;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.repository.*;
import com.exam.exam_system.mapper.*;

@ExtendWith(MockitoExtension.class)
public class StudentExamSessionServiceTest {

    @Mock
    private StudentExamSessionRepository sessionRepository;
    @Mock
    private ExamRepository examRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UserService userService;
    @Mock
    private StudentExamSessionMapper sessionMapper;

    @InjectMocks
    private StudentExamSessionService sessionService;

    private Exam exam;
    private User user;
    private Student student;

    @BeforeEach
    void setUp() {
        exam = new Exam();
        exam.setExamId(1L);
        exam.setQrToken("valid-token");
        exam.setIsActive(true);
        exam.setStartTime(LocalDateTime.now().minusHours(1));
        exam.setEndTime(LocalDateTime.now().plusHours(1));
        exam.setQrExpiresAt(LocalDateTime.now().plusHours(1));

        user = new User();
        user.setUserId(1L);

        student = new Student();
        student.setUser(user);
    }

    @Test
    void validateToken_ValidToken_ReturnsDto() {
        when(examRepository.findByQrToken("valid-token")).thenReturn(Optional.of(exam));

        ExamResponseDTO result = sessionService.validateTokenAndGetExam("valid-token");

        assertNotNull(result);
        assertEquals(exam.getExamId(), result.getExamId());
    }

    @Test
    void validateToken_ExpiredToken_ThrowsException() {
        exam.setQrExpiresAt(LocalDateTime.now().minusMinutes(1));
        when(examRepository.findByQrToken("valid-token")).thenReturn(Optional.of(exam));

        assertThrows(InvalidTokenException.class, () -> sessionService.validateTokenAndGetExam("valid-token"));
    }

    @Test
    void startSession_Success() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(studentRepository.findByUser_UserId(user.getUserId())).thenReturn(Optional.of(student));

        CreateStudentExamSessionRequestDTO dto = new CreateStudentExamSessionRequestDTO();
        dto.setExamId(1L);
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        StudentExamSession session = new StudentExamSession();
        session.setStudent(student);
        session.setSessionCode("uuid-code");
        when(sessionRepository.save(any(StudentExamSession.class))).thenReturn(session);
        when(sessionMapper.toResponseDTO(any())).thenReturn(new StudentExamSessionResponseDTO());

        StudentExamSessionResponseDTO result = sessionService.startSession(dto);

        assertNotNull(result);
        verify(sessionRepository).save(any(StudentExamSession.class));
    }
}
