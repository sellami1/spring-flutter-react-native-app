package tn.sellami.students.rest_spring_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;

    @NotBlank(message = "cin is required")
    private String cin;

    @NotBlank(message = "nom is required")
    private String nom;

    @NotNull(message = "dateNaissance is required")
    @Past(message = "dateNaissance must be in the past")
    private LocalDate dateNaissance;

    @NotNull(message = "anneePremiereInscription is required")
    @Min(value = 1900, message = "anneePremiereInscription must be >= 1900")
    private Integer anneePremiereInscription;

    private Integer age;
}
