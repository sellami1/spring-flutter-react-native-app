package tn.sellami.students.rest_spring_api.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tn.sellami.students.rest_spring_api.dto.StudentDto;
import tn.sellami.students.rest_spring_api.kafka.event.StudentCreatedEvent;

import java.time.Instant;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, StudentCreatedEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, StudentCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishStudentCreated(StudentDto student) {
        StudentCreatedEvent event = new StudentCreatedEvent(
                student.getId(),
                student.getCin(),
                student.getNom(),
                student.getDateNaissance(),
                student.getAnneePremiereInscription(),
                Instant.now()
        );
        kafkaTemplate.send("etudiant-created", event);
    }
}
