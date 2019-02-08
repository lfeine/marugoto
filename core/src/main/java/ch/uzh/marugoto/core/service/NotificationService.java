package ch.uzh.marugoto.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import ch.uzh.marugoto.core.data.entity.Dialog;
import ch.uzh.marugoto.core.data.entity.Mail;
import ch.uzh.marugoto.core.data.entity.Notification;
import ch.uzh.marugoto.core.data.entity.Page;
import ch.uzh.marugoto.core.data.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    protected NotificationRepository notificationRepository;

    public List<Notification> getIncomingNotifications(Page page) {
        return notificationRepository.findByPageId(page.getId());
    }

    public List<Mail> getIncomingMails() {
        return notificationRepository.findMailNotifications();
    }

    public List<Mail> getIncomingMails(Page page) {
        return getIncomingNotifications(page).stream()
                .filter(notification -> notification instanceof Mail)
                .map(notification -> (Mail) notification)
                .collect(Collectors.toList());
    }

    public List<Dialog> getIncomingDialogs(Page page) {
        return getIncomingNotifications(page).stream()
                .filter(notification -> notification instanceof Dialog)
                .map(notification -> (Dialog) notification)
                .collect(Collectors.toList());
    }

    public Notification getNotification(String notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow();
    }
}
