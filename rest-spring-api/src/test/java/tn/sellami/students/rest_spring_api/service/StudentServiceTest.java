package tn.sellami.students.rest_spring_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.sellami.students.rest_spring_api.dto.StudentDto;
import tn.sellami.students.rest_spring_api.entity.Student;
import tn.sellami.students.rest_spring_api.exception.ResourceNotFoundException;
import tn.sellami.students.rest_spring_api.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private StudentDto testStudentDto;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setCin("12345678");
        testStudent.setNom("Ali Ahmed");
        testStudent.setDateNaissance(LocalDate.of(2000, 5, 15));
        testStudent.setAnneePremiereInscription(2022);

        testStudentDto = new StudentDto(
                1L,
                "12345678",
                "Ali Ahmed",
                LocalDate.of(2000, 5, 15),
                2022,
                23
        );
    }

    // ========== findAll Tests ==========
    @Test
    void shouldReturnAllStudents() {
        // given
        when(studentRepository.findAll()).thenReturn(List.of(testStudent));

        // when
        List<StudentDto> result = studentService.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getNom()).isEqualTo("Ali Ahmed");
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoStudents() {
        // given
        when(studentRepository.findAll()).thenReturn(List.of());

        // when
        List<StudentDto> result = studentService.findAll();

        // then
        assertThat(result).isEmpty();
        verify(studentRepository, times(1)).findAll();
    }

    // ========== findById Tests ==========
    @Test
    void shouldReturnStudentById() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // when
        StudentDto result = studentService.findById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Ali Ahmed");
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFoundById() {
        // given
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> studentService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student with id 999 not found");

        verify(studentRepository, times(1)).findById(999L);
    }

    // ========== findByFirstInscriptionYear Tests ==========
    @Test
    void shouldReturnStudentsByInscriptionYear() {
        // given
        when(studentRepository.findByAnneePremiereInscription(2022)).thenReturn(List.of(testStudent));

        // when
        List<StudentDto> result = studentService.findByFirstInscriptionYear(2022);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAnneePremiereInscription()).isEqualTo(2022);
        verify(studentRepository, times(1)).findByAnneePremiereInscription(2022);
    }

    @Test
    void shouldReturnEmptyListWhenNoStudentsInYear() {
        // given
        when(studentRepository.findByAnneePremiereInscription(2000)).thenReturn(List.of());

        // when
        List<StudentDto> result = studentService.findByFirstInscriptionYear(2000);

        // then
        assertThat(result).isEmpty();
        verify(studentRepository, times(1)).findByAnneePremiereInscription(2000);
    }

    // ========== findByDepartementId Tests ==========
    @Test
    void shouldReturnStudentsByDepartementId() {
        // given
        when(studentRepository.findByDepartementId(1L)).thenReturn(List.of(testStudent));

        // when
        List<StudentDto> result = studentService.findByDepartementId(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(studentRepository, times(1)).findByDepartementId(1L);
    }

    @Test
    void shouldReturnEmptyListWhenNoDepartementStudents() {
        // given
        when(studentRepository.findByDepartementId(999L)).thenReturn(List.of());

        // when
        List<StudentDto> result = studentService.findByDepartementId(999L);

        // then
        assertThat(result).isEmpty();
        verify(studentRepository, times(1)).findByDepartementId(999L);
    }

    // ========== create Tests ==========
    @Test
    void shouldCreateNewStudent() {
        // given
        StudentDto dto = new StudentDto(
                null,
                "87654321",
                "Fatima Ben",
                LocalDate.of(2001, 3, 10),
                2023,
                0 // age ignored in create
        );
        Student newStudent = new Student();
        newStudent.setId(2L);
        newStudent.setCin("87654321");
        newStudent.setNom("Fatima Ben");
        newStudent.setDateNaissance(LocalDate.of(2001, 3, 10));
        newStudent.setAnneePremiereInscription(2023);

        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        // when
        StudentDto result = studentService.create(dto);

        // then
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getNom()).isEqualTo("Fatima Ben");
        assertThat(result.getCin()).isEqualTo("87654321");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldSetIdToNullBeforeCreating() {
        // given
        StudentDto dto = new StudentDto(
                999L, // id should be ignored
                "11111111",
                "Test Student",
                LocalDate.of(2000, 1, 1),
                2020,
                0
        );
        Student savedStudent = new Student();
        savedStudent.setId(3L);
        savedStudent.setCin("11111111");
        savedStudent.setNom("Test Student");
        savedStudent.setDateNaissance(LocalDate.of(2000, 1, 1));
        savedStudent.setAnneePremiereInscription(2020);

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // when
        StudentDto result = studentService.create(dto);

        // then
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getId()).isNotEqualTo(999L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    // ========== update Tests ==========
    @Test
    void shouldUpdateExistingStudent() {
        // given
        StudentDto updateDto = new StudentDto(
                1L,
                "12345678",
                "Ali Ahmed Updated",
                LocalDate.of(2000, 5, 15),
                2022,
                0
        );
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setCin("12345678");
        updatedStudent.setNom("Ali Ahmed Updated");
        updatedStudent.setDateNaissance(LocalDate.of(2000, 5, 15));
        updatedStudent.setAnneePremiereInscription(2022);

        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        // when
        StudentDto result = studentService.update(1L, updateDto);

        // then
        assertThat(result.getNom()).isEqualTo("Ali Ahmed Updated");
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentStudent() {
        // given
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> studentService.update(999L, testStudentDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student with id 999 not found");

        verify(studentRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    // ========== delete Tests ==========
    @Test
    void shouldDeleteExistingStudent() {
        // given
        when(studentRepository.existsById(1L)).thenReturn(true);

        // when
        studentService.delete(1L);

        // then
        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentStudent() {
        // given
        when(studentRepository.existsById(999L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> studentService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student with id 999 not found");

        verify(studentRepository, times(1)).existsById(999L);
        verify(studentRepository, never()).deleteById(999L);
    }

    // ========== Multiple Students Tests ==========
    @Test
    void shouldHandleMultipleStudents() {
        // given
        Student student2 = new Student();
        student2.setId(2L);
        student2.setCin("87654321");
        student2.setNom("Fatima Ben");
        student2.setDateNaissance(LocalDate.of(2001, 3, 10));
        student2.setAnneePremiereInscription(2023);

        when(studentRepository.findAll()).thenReturn(List.of(testStudent, student2));

        // when
        List<StudentDto> result = studentService.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(StudentDto::getNom)
                .containsExactly("Ali Ahmed", "Fatima Ben");
    }
}
