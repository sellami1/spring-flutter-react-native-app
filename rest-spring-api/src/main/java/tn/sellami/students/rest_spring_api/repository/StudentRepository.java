package tn.sellami.students.rest_spring_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.sellami.students.rest_spring_api.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	List<Student> findByAnneePremiereInscription(int annee);
}
