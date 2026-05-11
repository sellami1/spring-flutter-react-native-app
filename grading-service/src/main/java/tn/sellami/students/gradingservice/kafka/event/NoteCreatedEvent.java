package tn.sellami.students.gradingservice.kafka.event;

import java.time.Instant;

public class NoteCreatedEvent {

    private Long noteId;
    private Long studentId;
    private String matiere;
    private Double valeur;
    private Instant timestamp;

    public NoteCreatedEvent() {
    }

    public NoteCreatedEvent(Long noteId, Long studentId, String matiere, Double valeur, Instant timestamp) {
        this.noteId = noteId;
        this.studentId = studentId;
        this.matiere = matiere;
        this.valeur = valeur;
        this.timestamp = timestamp;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
