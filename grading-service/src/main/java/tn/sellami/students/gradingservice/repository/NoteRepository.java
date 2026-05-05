package tn.sellami.students.gradingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.sellami.students.gradingservice.entity.Note;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByStudentId(Long studentId);
}
