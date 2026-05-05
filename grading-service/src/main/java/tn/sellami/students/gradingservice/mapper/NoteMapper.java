package tn.sellami.students.gradingservice.mapper;

import tn.sellami.students.gradingservice.dto.NoteDto;
import tn.sellami.students.gradingservice.entity.Note;

public final class NoteMapper {

    private NoteMapper() {
    }

    public static NoteDto toDto(Note note) {
        return new NoteDto(note.getId(), note.getStudentId(), note.getMatiere(), note.getValeur());
    }

    public static Note toEntity(NoteDto noteDto) {
        return new Note(noteDto.getId(), noteDto.getStudentId(), noteDto.getMatiere(), noteDto.getValeur());
    }

    public static void updateEntity(Note note, NoteDto noteDto) {
        note.setStudentId(noteDto.getStudentId());
        note.setMatiere(noteDto.getMatiere());
        note.setValeur(noteDto.getValeur());
    }
}
