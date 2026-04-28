package tn.sellami.students.rest_spring_api.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tn.sellami.students.rest_spring_api.dto.StudentDto;
import tn.sellami.students.rest_spring_api.entity.Student;
import tn.sellami.students.rest_spring_api.exception.ResourceNotFoundException;
import tn.sellami.students.rest_spring_api.mapper.StudentMapper;
import tn.sellami.students.rest_spring_api.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Cacheable(value = "etudiants")
    public List<StudentDto> findAll() {
        return studentRepository.findAll().stream().map(StudentMapper::toDto).toList();
    }

    public StudentDto findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
        return StudentMapper.toDto(student);
    }

    public List<StudentDto> findByFirstInscriptionYear(int year) {
        return studentRepository.findByAnneePremiereInscription(year).stream().map(StudentMapper::toDto).toList();
    }

    @CacheEvict(value = "etudiants", allEntries = true)
    public StudentDto create(StudentDto studentDto) {
        Student student = StudentMapper.toEntity(studentDto);
        student.setId(null);
        Student saved = studentRepository.save(student);
        return StudentMapper.toDto(saved);
    }

    @CacheEvict(value = "etudiants", allEntries = true)
    public StudentDto update(Long id, StudentDto studentDto) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
        StudentMapper.updateEntity(existing, studentDto);
        Student saved = studentRepository.save(existing);
        return StudentMapper.toDto(saved);
    }

    @CacheEvict(value = "etudiants", allEntries = true)
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        studentRepository.deleteById(id);
    }
}
