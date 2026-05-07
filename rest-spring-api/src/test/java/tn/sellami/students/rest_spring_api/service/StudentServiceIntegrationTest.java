package tn.sellami.students.rest_spring_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import tn.sellami.students.rest_spring_api.dto.StudentDto;
import tn.sellami.students.rest_spring_api.entity.Student;
import tn.sellami.students.rest_spring_api.exception.ResourceNotFoundException;
import tn.sellami.students.rest_spring_api.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5433/students_db",
        "spring.datasource.username=postgres",
        "spring.datasource.password=postgres",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class StudentServiceIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    // ========== Persistence Tests ==========
    @Test
    void shouldPersistAndRetrieveStudent() {
        // given
        StudentDto dto = new StudentDto(
                null,
                "12345678",
                "Ali Ahmed",
                LocalDate.of(2000, 5, 15),
                2022,
                0
        );

        // when
        StudentDto created = studentService.create(dto);

        // then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getNom()).isEqualTo("Ali Ahmed");

        // verify persistence
        StudentDto retrieved = studentService.findById(created.getId());
        assertThat(retrieved.getNom()).isEqualTo("Ali Ahmed");
        assertThat(retrieved.getCin()).isEqualTo("12345678");
    }

    @Test
    void shouldReturnAllPersistedStudents() {
        // given
        StudentDto dto1 = new StudentDto(null, "11111111", "Student One", LocalDate.of(2000, 1, 1), 2020, 0);
        StudentDto dto2 = new StudentDto(null, "22222222", "Student Two", LocalDate.of(2001, 2, 2), 2021, 0);

        studentService.create(dto1);
        studentService.create(dto2);

        // when
        List<StudentDto> result = studentService.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(StudentDto::getNom)
                .containsExactly("Student One", "Student Two");
    }

    @Test
    void shouldUpdatePersistedStudent() {
        // given
        StudentDto dto = new StudentDto(null, "12345678", "Ali Ahmed", LocalDate.of(2000, 5, 15), 2022, 0);
        StudentDto created = studentService.create(dto);

        StudentDto updateDto = new StudentDto(
                created.getId(),
                "12345678",
                "Ali Ahmed Updated",
                LocalDate.of(2000, 5, 15),
                2023,
                0
        );

        // when
        StudentDto updated = studentService.update(created.getId(), updateDto);

        // then
        assertThat(updated.getNom()).isEqualTo("Ali Ahmed Updated");
        assertThat(updated.getAnneePremiereInscription()).isEqualTo(2023);

        // verify persistence
        StudentDto retrieved = studentService.findById(created.getId());
        assertThat(retrieved.getNom()).isEqualTo("Ali Ahmed Updated");
    }

    @Test
    void shouldDeletePersistedStudent() {
        // given
        StudentDto dto = new StudentDto(null, "12345678", "Ali Ahmed", LocalDate.of(2000, 5, 15), 2022, 0);
        StudentDto created = studentService.create(dto);

        // when
        studentService.delete(created.getId());

        // then
        assertThatThrownBy(() -> studentService.findById(created.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ========== Query Tests ==========
    @Test
    void shouldFindStudentsByInscriptionYear() {
        // given
        StudentDto dto1 = new StudentDto(null, "11111111", "Student 2022-1", LocalDate.of(2000, 1, 1), 2022, 0);
        StudentDto dto2 = new StudentDto(null, "22222222", "Student 2022-2", LocalDate.of(2001, 2, 2), 2022, 0);
        StudentDto dto3 = new StudentDto(null, "33333333", "Student 2023-1", LocalDate.of(2002, 3, 3), 2023, 0);

        studentService.create(dto1);
        studentService.create(dto2);
        studentService.create(dto3);

        // when
        List<StudentDto> result2022 = studentService.findByFirstInscriptionYear(2022);
        List<StudentDto> result2023 = studentService.findByFirstInscriptionYear(2023);

        // then
        assertThat(result2022).hasSize(2);
        assertThat(result2022).extracting(StudentDto::getAnneePremiereInscription)
                .containsOnly(2022);

        assertThat(result2023).hasSize(1);
        assertThat(result2023.get(0).getAnneePremiereInscription()).isEqualTo(2023);
    }

    // ========== Validation Tests ==========
    @Test
    void shouldThrowExceptionForNonExistentStudentDuringUpdate() {
        // given
        StudentDto updateDto = new StudentDto(null, "12345678", "Ali Ahmed", LocalDate.of(2000, 5, 15), 2022, 0);

        // when & then
        assertThatThrownBy(() -> studentService.update(999L, updateDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldThrowExceptionForNonExistentStudentDuringDelete() {
        // when & then
        assertThatThrownBy(() -> studentService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("not found");
    }

    // ========== Edge Cases ==========
    @Test
    void shouldHandleEmptyDatabase() {
        // when
        List<StudentDto> result = studentService.findAll();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldHandleMultipleOperationsSequentially() {
        // Create 3 students
        StudentDto dto1 = new StudentDto(null, "11111111", "Student 1", LocalDate.of(2000, 1, 1), 2020, 0);
        StudentDto dto2 = new StudentDto(null, "22222222", "Student 2", LocalDate.of(2001, 2, 2), 2021, 0);
        StudentDto dto3 = new StudentDto(null, "33333333", "Student 3", LocalDate.of(2002, 3, 3), 2022, 0);

        StudentDto created1 = studentService.create(dto1);
        StudentDto created2 = studentService.create(dto2);
        StudentDto created3 = studentService.create(dto3);

        assertThat(studentService.findAll()).hasSize(3);

        // Update one
        StudentDto updateDto = new StudentDto(
                created1.getId(),
                "11111111",
                "Student 1 Updated",
                LocalDate.of(2000, 1, 1),
                2020,
                0
        );
        studentService.update(created1.getId(), updateDto);
        assertThat(studentService.findAll()).hasSize(3);

        // Delete one
        studentService.delete(created2.getId());
        assertThat(studentService.findAll()).hasSize(2);

        // Verify data persisted correctly
        assertThat(studentService.findById(created1.getId()).getNom()).isEqualTo("Student 1 Updated");
        assertThat(studentService.findById(created3.getId()).getNom()).isEqualTo("Student 3");
    }

    // ========== Data Integrity Tests ==========
    @Test
    void shouldPreserveStudentDataThroughPersistence() {
        // given
        String testCin = "98765432";
        String testNom = "Test Student";
        LocalDate testBirthDate = LocalDate.of(1995, 7, 20);
        int testYear = 2020;

        StudentDto dto = new StudentDto(null, testCin, testNom, testBirthDate, testYear, 0);

        // when
        StudentDto created = studentService.create(dto);
        StudentDto retrieved = studentService.findById(created.getId());

        // then
        assertThat(retrieved.getCin()).isEqualTo(testCin);
        assertThat(retrieved.getNom()).isEqualTo(testNom);
        assertThat(retrieved.getDateNaissance()).isEqualTo(testBirthDate);
        assertThat(retrieved.getAnneePremiereInscription()).isEqualTo(testYear);
    }
}
