package tn.sellami.students.gradingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "etudiant-service")
public interface StudentClient {

    @GetMapping("/api/etudiants/{id}")
    Object getStudentById(@PathVariable("id") Long id);
}
