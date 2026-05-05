package tn.sellami.students.gradingservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.sellami.students.gradingservice.dto.NoteDto;
import tn.sellami.students.gradingservice.service.NoteService;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getNotes(@RequestParam(value = "studentId", required = false) Long studentId) {
        List<NoteDto> notes = studentId == null
                ? noteService.findAll()
                : noteService.findByStudentId(studentId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody NoteDto noteDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.create(noteDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @Valid @RequestBody NoteDto noteDto) {
        return ResponseEntity.ok(noteService.update(id, noteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
