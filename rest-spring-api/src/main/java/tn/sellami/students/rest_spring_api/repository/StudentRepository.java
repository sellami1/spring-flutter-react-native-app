package tn.sellami.students.rest_spring_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.sellami.students.rest_spring_api.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
