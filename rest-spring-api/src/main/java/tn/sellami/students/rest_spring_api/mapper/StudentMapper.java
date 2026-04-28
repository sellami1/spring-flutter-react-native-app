package tn.sellami.students.rest_spring_api.mapper;

import tn.sellami.students.rest_spring_api.dto.StudentDto;
import tn.sellami.students.rest_spring_api.entity.Student;

public final class StudentMapper {

    private StudentMapper() {
    }

    public static StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getCin(),
                student.getNom(),
                student.getDateNaissance(),
                student.getAnneePremiereInscription(),
                student.age()
        );
    }

    public static Student toEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setId(studentDto.getId());
        student.setCin(studentDto.getCin());
        student.setNom(studentDto.getNom());
        student.setDateNaissance(studentDto.getDateNaissance());
        student.setAnneePremiereInscription(studentDto.getAnneePremiereInscription());
        return student;
    }

    public static void updateEntity(Student student, StudentDto studentDto) {
        student.setCin(studentDto.getCin());
        student.setNom(studentDto.getNom());
        student.setDateNaissance(studentDto.getDateNaissance());
        student.setAnneePremiereInscription(studentDto.getAnneePremiereInscription());
    }
}
