package tn.sellami.students.gradingservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tn.sellami.students.gradingservice.dto.NoteDto;
import tn.sellami.students.gradingservice.kafka.event.NoteCreatedEvent;

import java.time.Instant;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, NoteCreatedEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, NoteCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishNoteCreated(NoteDto note) {
        NoteCreatedEvent event = new NoteCreatedEvent(
                note.getId(),
                note.getStudentId(),
                note.getMatiere(),
                note.getValeur(),
                Instant.now()
        );
        kafkaTemplate.send("note-created", event);
    }
}
