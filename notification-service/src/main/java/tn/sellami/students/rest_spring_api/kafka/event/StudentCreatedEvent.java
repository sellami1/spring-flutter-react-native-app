package tn.sellami.students.rest_spring_api.kafka.event;

import java.time.Instant;
import java.time.LocalDate;

public class StudentCreatedEvent {

    private Long studentId;
    private String cin;
    private String nom;
    private LocalDate dateNaissance;
    private Integer anneePremiereInscription;
    private Instant timestamp;

    public StudentCreatedEvent() {
    }

    public StudentCreatedEvent(Long studentId, String cin, String nom, LocalDate dateNaissance,
                               Integer anneePremiereInscription, Instant timestamp) {
        this.studentId = studentId;
        this.cin = cin;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.anneePremiereInscription = anneePremiereInscription;
        this.timestamp = timestamp;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Integer getAnneePremiereInscription() {
        return anneePremiereInscription;
    }

    public void setAnneePremiereInscription(Integer anneePremiereInscription) {
        this.anneePremiereInscription = anneePremiereInscription;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
