package com.exam.exam_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.entities.*;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.mapper.ExamMapper;
import com.exam.exam_system.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamUploadService {

    private static final String PYTHON_PARSE_URL = "https://abdulraahmann-pdf-exam-parser.hf.space/parse-questions";

    private final RestTemplate restTemplate;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final CollegeRepository collegeRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;

    private final ExamMapper examMapper;
    private final UserService userService;

    /**
     * Single endpoint: create exam + parse questions from uploaded file.
     */
    @Transactional
    public ExamResponseDTO createExamWithFile(CreateExamWithFileRequestDTO dto, MultipartFile file) {

        // 1. Validate relations
        College college = collegeRepository.findById(dto.getCollegeId())
                .orElseThrow(CollegeNotFoundException::new);

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(DepartmentNotFoundException::new);

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(SubjectNotFoundException::new);

        if (!department.getCollege().getCollegeId().equals(college.getCollegeId())) {
            throw new DepartmentCollegeMismatchException();
        }
        if (!subject.getDepartment().getDepartmentId().equals(department.getDepartmentId())) {
            throw new SubjectDepartmentMismatchException();
        }

        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new InvalidExamTimeException();
        }

        User user = userService.getCurrentUser();

        if (!user.getCollege().getCollegeId().equals(college.getCollegeId())) {
            throw new UserCollegeMismatchException();
        }
        if (!user.getDepartment().getDepartmentId().equals(department.getDepartmentId())) {
            throw new UserDepartmentMismatchException();
        }

        // 2. Send file to Python service
        ParsedExamDTO parsedExam = sendFileToPythonService(file);

        if (parsedExam == null || parsedExam.getQuestions() == null || parsedExam.getQuestions().isEmpty()) {
            throw new PythonServiceException(Messages.PYTHON_SERVICE_INVALID_RESPONSE);
        }

        // 3. Build exam entity
        Exam exam = examMapper.toEntity(dto);
        exam.setCollege(college);
        exam.setDepartment(department);
        exam.setSubject(subject);
        exam.setCreatedBy(user);
        exam.setQrCodeUrl(null);

        // 4. Map parsed questions to entities
        List<Question> questions = new ArrayList<>();

        for (ParsedQuestionDTO parsedQuestion : parsedExam.getQuestions()) {
            Question question = Question.builder()
                    .questionText(parsedQuestion.getQuestionText())
                    .questionType(QuestionType.valueOf(parsedQuestion.getQuestionType()))
                    .marks(parsedQuestion.getMarks())
                    .questionOrder(parsedQuestion.getQuestionOrder())
                    .exam(exam)
                    .build();

            if (parsedQuestion.getChoices() != null && !parsedQuestion.getChoices().isEmpty()) {
                List<Choice> choices = new ArrayList<>();
                for (ParsedChoiceDTO parsedChoice : parsedQuestion.getChoices()) {
                    Choice choice = Choice.builder()
                            .choiceText(parsedChoice.getChoiceText())
                            .isCorrect(parsedChoice.getIsCorrect())
                            .choiceOrder(parsedChoice.getChoiceOrder())
                            .question(question)
                            .build();
                    choices.add(choice);
                }
                question.setChoices(choices);
            }

            questions.add(question);
        }

        exam.setQuestions(questions);
        exam.setTotalQuestions(questions.size());

        // 5. Save exam (cascade saves questions + choices)
        Exam savedExam = examRepository.save(exam);

        return examMapper.toDto(savedExam);
    }

    /**
     * Existing: upload questions to an already-created exam.
     */
    @Transactional
    public UploadQuestionsResponseDTO uploadAndParseQuestions(Long examId, MultipartFile file) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(ExamNotFoundException::new);

        ParsedExamDTO parsedExam = sendFileToPythonService(file);

        if (parsedExam == null || parsedExam.getQuestions() == null || parsedExam.getQuestions().isEmpty()) {
            throw new PythonServiceException(Messages.PYTHON_SERVICE_INVALID_RESPONSE);
        }

        List<Question> questions = new ArrayList<>();

        for (ParsedQuestionDTO parsedQuestion : parsedExam.getQuestions()) {
            Question question = Question.builder()
                    .questionText(parsedQuestion.getQuestionText())
                    .questionType(QuestionType.valueOf(parsedQuestion.getQuestionType()))
                    .marks(parsedQuestion.getMarks())
                    .questionOrder(parsedQuestion.getQuestionOrder())
                    .exam(exam)
                    .build();

            if (parsedQuestion.getChoices() != null && !parsedQuestion.getChoices().isEmpty()) {
                List<Choice> choices = new ArrayList<>();
                for (ParsedChoiceDTO parsedChoice : parsedQuestion.getChoices()) {
                    Choice choice = Choice.builder()
                            .choiceText(parsedChoice.getChoiceText())
                            .isCorrect(parsedChoice.getIsCorrect())
                            .choiceOrder(parsedChoice.getChoiceOrder())
                            .question(question)
                            .build();
                    choices.add(choice);
                }
                question.setChoices(choices);
            }

            questions.add(question);
        }

        questionRepository.saveAll(questions);

        exam.setTotalQuestions(
            (exam.getTotalQuestions() != null ? exam.getTotalQuestions() : 0) + questions.size()
        );
        examRepository.save(exam);

        return new UploadQuestionsResponseDTO(
                Messages.QUESTIONS_UPLOADED_SUCCESSFULLY,
                questions.size()
        );
    }

    private ParsedExamDTO sendFileToPythonService(MultipartFile file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new HttpEntity<>(fileResource, createFileHeaders(file)));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ParsedExamDTO> response = restTemplate.exchange(
                    PYTHON_PARSE_URL,
                    HttpMethod.POST,
                    requestEntity,
                    ParsedExamDTO.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new PythonServiceException(Messages.PYTHON_SERVICE_INVALID_RESPONSE);
            }

            return response.getBody();

        } catch (RestClientException ex) {
            log.error("Failed to connect to Python parsing service: {}", ex.getMessage());
            throw new PythonServiceException(Messages.PYTHON_SERVICE_UNAVAILABLE, ex);
        } catch (PythonServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error sending file to Python service: {}", ex.getMessage());
            throw new PythonServiceException(Messages.PYTHON_SERVICE_UNAVAILABLE, ex);
        }
    }

    private HttpHeaders createFileHeaders(MultipartFile file) {
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.parseMediaType(
                file.getContentType() != null ? file.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE
        ));
        fileHeaders.setContentDispositionFormData("file", file.getOriginalFilename());
        return fileHeaders;
    }
}
