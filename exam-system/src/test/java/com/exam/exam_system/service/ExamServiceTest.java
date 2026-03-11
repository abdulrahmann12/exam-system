package com.exam.exam_system.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

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

@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;
    @Mock
    private QrCodeService qrCodeService;
    @Mock
    private ImageService imageService;
    @Mock
    private QrProperties qrProperties;

    @InjectMocks
    private ExamService examService;

    private Exam exam;

    @BeforeEach
    void setUp() {
        exam = new Exam();
        exam.setExamId(1L);
        exam.setStartTime(LocalDateTime.now().plusHours(1));
    }

    @Test
    void deleteExam_InPast_ThrowsException() {
        exam.setStartTime(LocalDateTime.now().minusHours(1));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        assertThrows(ExamDeletionNotAllowedException.class, () -> examService.deleteExam(1L));
    }

    @Test
    void generateQr_ExistingValidQr_ReturnsExisting() {
        exam.setQrCodeUrl("existing-url");
        exam.setQrExpiresAt(LocalDateTime.now().plusMinutes(5));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        ExamQrResponseDTO result = examService.generateQrForExam(1L);

        assertEquals("existing-url", result.getQrCodeUrl());
        verify(qrCodeService, never()).generateQrCode(anyString());
    }
}
