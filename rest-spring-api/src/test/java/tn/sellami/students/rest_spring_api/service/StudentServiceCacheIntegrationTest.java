package tn.sellami.students.rest_spring_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import tn.sellami.students.rest_spring_api.dto.StudentDto;
import tn.sellami.students.rest_spring_api.repository.StudentRepository;

import java.time.LocalDate;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class StudentServiceCacheIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CacheManager cacheManager;

    @SpyBean
    private StudentRepository studentRepository;

    @BeforeEach
    void clearCacheAndSpies() {
        Cache cache = cacheManager.getCache("etudiants");
        if (cache != null) {
            cache.clear();
        }
        clearInvocations(studentRepository);
    }

    @Test
    void findAllUsesCacheAfterFirstCall() {
        studentService.findAll();
        studentService.findAll();

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void createEvictsCacheSoNextFindAllHitsRepositoryAgain() {
        studentService.findAll();
        verify(studentRepository, times(1)).findAll();

        StudentDto dto = new StudentDto(
                null,
                "CIN" + System.nanoTime(),
                "Cache Test",
                LocalDate.of(2003, 2, 10),
                2022,
                null
        );

        studentService.create(dto);
        studentService.findAll();

        verify(studentRepository, times(2)).findAll();
    }
}
