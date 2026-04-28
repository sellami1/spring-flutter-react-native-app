package tn.sellami.students.rest_spring_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartementDto {
    private Long id;

    @NotBlank(message = "nom is required")
    private String nom;
}
