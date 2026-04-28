package tn.sellami.students.rest_spring_api.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentAgeUnitTest {

    @Test
    void ageAtBirthdayIsExact() {
        Student student = new Student();
        student.setDateNaissance(LocalDate.of(2002, 4, 7));

        int age = student.ageAt(LocalDate.of(2025, 4, 7));

        assertEquals(23, age);
    }

    @Test
    void ageBeforeBirthdayIsOneYearLess() {
        Student student = new Student();
        student.setDateNaissance(LocalDate.of(2002, 10, 10));

        int age = student.ageAt(LocalDate.of(2025, 4, 7));

        assertEquals(22, age);
    }

    @Test
    void leapDayBirthdayIsHandled() {
        Student student = new Student();
        student.setDateNaissance(LocalDate.of(2004, 2, 29));

        int age = student.ageAt(LocalDate.of(2025, 3, 1));

        assertEquals(21, age);
    }
}
