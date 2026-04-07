package tn.sellami.students.rest_spring_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.sellami.students.rest_spring_api.entity.Departement;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
}
