package be.pxl.services.services;

import be.pxl.services.domain.Notification;
import be.pxl.services.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification add(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public Notification update(Long id, Notification notification) {
        Notification existing = notificationRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setFrom(notification.getFrom());
        existing.setTo(notification.getTo());
        existing.setSubject(notification.getSubject());
        existing.setMessage(notification.getMessage());
        return notificationRepository.save(existing);
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}
