package tn.sellami.students.rest_spring_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.sellami.students.rest_spring_api.dto.StudentDto;
import tn.sellami.students.rest_spring_api.exception.GlobalExceptionHandler;
import tn.sellami.students.rest_spring_api.exception.ResourceNotFoundException;
import tn.sellami.students.rest_spring_api.service.StudentService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import({GlobalExceptionHandler.class, StudentControllerWebMvcTest.TestBeans.class})
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentService studentService;

    @TestConfiguration
    static class TestBeans {
        @Bean
        StudentService studentService() {
            return org.mockito.Mockito.mock(StudentService.class);
        }
    }

    @Test
    void getAllStudentsReturnsOkAndPayload() throws Exception {
        StudentDto student = new StudentDto(
                1L,
                "07123456",
                "Ahmed",
                LocalDate.of(2005, 3, 15),
                2023,
                21
        );
        when(studentService.findAll()).thenReturn(List.of(student));

        mockMvc.perform(get("/api/etudiants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cin").value("07123456"))
                .andExpect(jsonPath("$[0].age").value(21));
    }

    @Test
    void getStudentByIdWhenMissingReturnsNotFound() throws Exception {
        when(studentService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Student with id 999 not found"));

        mockMvc.perform(get("/api/etudiants/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/etudiants/999"));
    }

    @Test
    void createStudentWithInvalidBodyReturnsBadRequest() throws Exception {
        StudentDto invalid = new StudentDto(
                null,
                "",
                "",
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/etudiants"));
    }

    @Test
    void createStudentWhenDuplicateCinReturnsConflict() throws Exception {
        StudentDto request = new StudentDto(
                null,
                "07123456",
                "Ahmed",
                LocalDate.of(2005, 3, 15),
                2023,
                null
        );

        when(studentService.create(any(StudentDto.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate key value violates unique constraint"));

        mockMvc.perform(post("/api/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.path").value("/api/etudiants"));
    }

    @Test
    void searchByFirstInscriptionYearReturnsOk() throws Exception {
        StudentDto student = new StudentDto(
                2L,
                "09345678",
                "Mohamed",
                LocalDate.of(2005, 11, 8),
                2023,
                20
        );

        when(studentService.findByFirstInscriptionYear(eq(2023))).thenReturn(List.of(student));

        mockMvc.perform(get("/api/etudiants/search").param("firstInscriptionYear", "2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].anneePremiereInscription").value(2023));
    }
}
