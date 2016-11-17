package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import me.belakede.thesis.client.boundary.javafx.task.MessageReceiverTask;
import me.belakede.thesis.client.boundary.javafx.task.NoteRegistrationTask;
import me.belakede.thesis.server.game.response.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameFlowService {

    private final ListProperty<Notification> notifications = new SimpleListProperty<>();

    private final MessageReceiverTask messageReceiverTask;
    private final NoteRegistrationTask noteRegistrationTask;

    @Autowired
    public GameFlowService(MessageReceiverTask messageReceiverTask, NoteRegistrationTask noteRegistrationTask) {
        this.messageReceiverTask = messageReceiverTask;
        this.noteRegistrationTask = noteRegistrationTask;
    }

    public ObservableList<Notification> getNotifications() {
        return notifications.get();
    }

    public void setNotifications(ObservableList<Notification> notifications) {
        this.notifications.set(notifications);
    }

    public ListProperty<Notification> notificationsProperty() {
        return notifications;
    }

    public void openChannels() {
        startDaemonThread(messageReceiverTask);
        startDaemonThread(noteRegistrationTask);
    }

    private void startDaemonThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

}
