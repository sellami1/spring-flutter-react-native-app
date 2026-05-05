package tn.sellami.students.gradingservice.service;

import feign.FeignException;
import org.springframework.stereotype.Service;
import tn.sellami.students.gradingservice.client.StudentClient;
import tn.sellami.students.gradingservice.dto.NoteDto;
import tn.sellami.students.gradingservice.entity.Note;
import tn.sellami.students.gradingservice.exception.ResourceNotFoundException;
import tn.sellami.students.gradingservice.mapper.NoteMapper;
import tn.sellami.students.gradingservice.repository.NoteRepository;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final StudentClient studentClient;

    public NoteService(NoteRepository noteRepository, StudentClient studentClient) {
        this.noteRepository = noteRepository;
        this.studentClient = studentClient;
    }

    public List<NoteDto> findAll() {
        return noteRepository.findAll().stream()
                .map(NoteMapper::toDto)
                .toList();
    }

    public List<NoteDto> findByStudentId(Long studentId) {
        return noteRepository.findByStudentId(studentId).stream()
                .map(NoteMapper::toDto)
                .toList();
    }

    public NoteDto findById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + id));
        return NoteMapper.toDto(note);
    }

    public NoteDto create(NoteDto noteDto) {
        ensureStudentExists(noteDto.getStudentId());
        Note note = NoteMapper.toEntity(noteDto);
        note.setId(null);
        Note saved = noteRepository.save(note);
        return NoteMapper.toDto(saved);
    }

    public NoteDto update(Long id, NoteDto noteDto) {
        ensureStudentExists(noteDto.getStudentId());
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + id));
        NoteMapper.updateEntity(existing, noteDto);
        Note saved = noteRepository.save(existing);
        return NoteMapper.toDto(saved);
    }

    public void delete(Long id) {
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + id));
        noteRepository.delete(existing);
    }

    private void ensureStudentExists(Long studentId) {
        try {
            studentClient.getStudentById(studentId);
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Student not found with id " + studentId);
        }
    }
}
