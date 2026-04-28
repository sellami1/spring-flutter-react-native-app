package tn.sellami.students.rest_spring_api.mapper;

import tn.sellami.students.rest_spring_api.dto.DepartementDto;
import tn.sellami.students.rest_spring_api.entity.Departement;

public final class DepartementMapper {

    private DepartementMapper() {
    }

    public static DepartementDto toDto(Departement departement) {
        return new DepartementDto(departement.getId(), departement.getNom());
    }

    public static Departement toEntity(DepartementDto departementDto) {
        Departement departement = new Departement();
        departement.setId(departementDto.getId());
        departement.setNom(departementDto.getNom());
        return departement;
    }

    public static void updateEntity(Departement departement, DepartementDto departementDto) {
        departement.setNom(departementDto.getNom());
    }
}
