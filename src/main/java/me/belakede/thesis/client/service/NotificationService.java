package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.belakede.thesis.server.game.response.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final ListProperty<Notification> notifications = new SimpleListProperty<>();

    public NotificationService() {
        setNotifications(FXCollections.observableArrayList());
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

}
