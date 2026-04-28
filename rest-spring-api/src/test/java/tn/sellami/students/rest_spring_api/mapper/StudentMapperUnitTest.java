package tn.sellami.students.rest_spring_api.mapper;

import org.junit.jupiter.api.Test;
import tn.sellami.students.rest_spring_api.dto.StudentDto;
import tn.sellami.students.rest_spring_api.entity.Student;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StudentMapperUnitTest {

    @Test
    void toDtoComputesAge() {
        Student student = new Student();
        student.setId(1L);
        student.setCin("07123456");
        student.setNom("Ahmed");
        student.setDateNaissance(LocalDate.now().minusYears(20));
        student.setAnneePremiereInscription(2023);

        StudentDto dto = StudentMapper.toDto(student);

        assertEquals(1L, dto.getId());
        assertEquals(20, dto.getAge());
    }

    @Test
    void toEntityDoesNotUseAgeField() {
        StudentDto dto = new StudentDto(
                null,
                "09345678",
                "Mohamed",
                LocalDate.of(2005, 11, 8),
                2023,
                99
        );

        Student entity = StudentMapper.toEntity(dto);

        assertEquals("09345678", entity.getCin());
        assertNull(entity.getId());
    }

    @Test
    void updateEntityUpdatesWritableFields() {
        Student entity = new Student();
        entity.setId(10L);
        entity.setCin("old");
        entity.setNom("old");
        entity.setDateNaissance(LocalDate.of(2000, 1, 1));
        entity.setAnneePremiereInscription(2019);

        StudentDto dto = new StudentDto(
                999L,
                "new",
                "new name",
                LocalDate.of(2001, 2, 2),
                2020,
                0
        );

        StudentMapper.updateEntity(entity, dto);

        assertEquals(10L, entity.getId());
        assertEquals("new", entity.getCin());
        assertEquals("new name", entity.getNom());
        assertEquals(LocalDate.of(2001, 2, 2), entity.getDateNaissance());
        assertEquals(2020, entity.getAnneePremiereInscription());
    }
}
