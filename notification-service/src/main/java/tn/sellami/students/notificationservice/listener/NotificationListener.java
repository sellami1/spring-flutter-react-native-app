package tn.sellami.students.notificationservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tn.sellami.students.gradingservice.kafka.event.NoteCreatedEvent;
import tn.sellami.students.rest_spring_api.kafka.event.StudentCreatedEvent;

@Component
public class NotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    @KafkaListener(topics = "etudiant-created", groupId = "notification-group")
    public void onStudentCreated(StudentCreatedEvent event) {
        logger.info("[NOTIFICATION] Student created: {} (id: {}, cin: {})",
                event.getNom(), event.getStudentId(), event.getCin());
    }

    @KafkaListener(topics = "note-created", groupId = "notification-group")
    public void onNoteCreated(NoteCreatedEvent event) {
        logger.info("[NOTIFICATION] Note created: studentId={}, matiere={}, valeur={}",
                event.getStudentId(), event.getMatiere(), event.getValeur());
    }
}
