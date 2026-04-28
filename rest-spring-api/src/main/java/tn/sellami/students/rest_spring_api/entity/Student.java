package tn.sellami.students.rest_spring_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "etudiants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cin;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Column(nullable = false)
    private Integer anneePremiereInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Departement departement;

    public int age() {
        return ageAt(LocalDate.now());
    }

    public int ageAt(LocalDate referenceDate) {
        return Period.between(this.dateNaissance, referenceDate).getYears();
    }
}
