package tn.sellami.students.rest_spring_api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import tn.sellami.students.rest_spring_api.entity.Student;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryDataJpaTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void findByAnneePremiereInscriptionReturnsExpectedRows() {
        List<Student> students = studentRepository.findByAnneePremiereInscription(2023);

        assertTrue(students.size() >= 2);
        assertTrue(students.stream().allMatch(s -> s.getAnneePremiereInscription() == 2023));
    }

    @Test
    void duplicateCinRaisesDataIntegrityViolation() {
        String cin = "55550000";

        Student first = new Student();
        first.setCin(cin);
        first.setNom("First");
        first.setDateNaissance(LocalDate.of(2000, 1, 1));
        first.setAnneePremiereInscription(2020);

        Student duplicate = new Student();
        duplicate.setCin(cin);
        duplicate.setNom("Duplicate");
        duplicate.setDateNaissance(LocalDate.of(2001, 1, 1));
        duplicate.setAnneePremiereInscription(2021);

        studentRepository.saveAndFlush(first);

        assertThrows(DataIntegrityViolationException.class, () -> studentRepository.saveAndFlush(duplicate));
    }
}
