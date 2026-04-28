package tn.sellami.students.rest_spring_api.service;

import org.springframework.stereotype.Service;
import tn.sellami.students.rest_spring_api.dto.DepartementDto;
import tn.sellami.students.rest_spring_api.entity.Departement;
import tn.sellami.students.rest_spring_api.exception.ResourceNotFoundException;
import tn.sellami.students.rest_spring_api.mapper.DepartementMapper;
import tn.sellami.students.rest_spring_api.repository.DepartementRepository;

import java.util.List;

@Service
public class DepartementService {

    private final DepartementRepository departementRepository;

    public DepartementService(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    public List<DepartementDto> findAll() {
        return departementRepository.findAll().stream().map(DepartementMapper::toDto).toList();
    }

    public DepartementDto findById(Long id) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departement with id " + id + " not found"));
        return DepartementMapper.toDto(departement);
    }

    public DepartementDto create(DepartementDto departementDto) {
        Departement departement = DepartementMapper.toEntity(departementDto);
        departement.setId(null);
        Departement saved = departementRepository.save(departement);
        return DepartementMapper.toDto(saved);
    }

    public DepartementDto update(Long id, DepartementDto departementDto) {
        Departement existing = departementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departement with id " + id + " not found"));
        DepartementMapper.updateEntity(existing, departementDto);
        Departement saved = departementRepository.save(existing);
        return DepartementMapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!departementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Departement with id " + id + " not found");
        }
        departementRepository.deleteById(id);
    }
}
