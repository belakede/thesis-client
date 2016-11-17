package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import me.belakede.thesis.server.game.response.Notification;
import me.belakede.thesis.server.game.response.PlayerJoinedNotification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final ListProperty<Notification> notifications = new SimpleListProperty<>();
    private final ListProperty<PlayerJoinedNotification> playerJoinedNotifications = new SimpleListProperty<>();

    public NotificationService() {
        setNotifications(FXCollections.observableArrayList());
        setPlayerJoinedNotifications(FXCollections.observableArrayList());
        setHookupChangeListeners();
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

    public ObservableList<PlayerJoinedNotification> getPlayerJoinedNotifications() {
        return playerJoinedNotifications.get();
    }

    public void setPlayerJoinedNotifications(ObservableList<PlayerJoinedNotification> playerJoinedNotifications) {
        this.playerJoinedNotifications.set(playerJoinedNotifications);
    }

    public ListProperty<PlayerJoinedNotification> playerJoinedNotificationsProperty() {
        return playerJoinedNotifications;
    }

    public void add(Notification notification) {
        notifications.add(notification);
    }

    private void setHookupChangeListeners() {
        notifications.addListener((ListChangeListener.Change<? extends Notification> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(notification -> {
                        if (notification instanceof PlayerJoinedNotification) {
                            playerJoinedNotifications.add((PlayerJoinedNotification) notification);
                        }
                    });
                }
            }
        });
    }

}
