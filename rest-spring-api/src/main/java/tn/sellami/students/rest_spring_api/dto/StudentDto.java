package tn.sellami.students.rest_spring_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String cin;
    private String nom;
    private LocalDate dateNaissance;
    private Integer anneePremiereInscription;
    private Integer age;
}
