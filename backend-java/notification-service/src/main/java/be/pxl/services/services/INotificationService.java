package be.pxl.services.services;

import be.pxl.services.domain.Notification;

import java.util.List;

public interface INotificationService {
    List<Notification> getAllNotifications();
    Notification add(Notification notification);
    Notification findById(Long id);
    Notification update(Long id, Notification notification);
    void delete(Long id);
}
