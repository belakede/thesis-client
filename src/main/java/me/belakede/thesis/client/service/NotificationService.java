package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import me.belakede.thesis.server.game.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private final ListProperty<Notification> notifications = new SimpleListProperty<>();
    private final ListProperty<PlayerJoinedNotification> playerJoinedNotifications = new SimpleListProperty<>();
    private final ObjectProperty<PlayerStatusNotification> playerStatusNotification = new SimpleObjectProperty<>();
    private final ObjectProperty<GameStatusNotification> gameStatusNotification = new SimpleObjectProperty<>();
    private final ObjectProperty<GamePausedNotification> gamePausedNotification = new SimpleObjectProperty<>();
    private final ObjectProperty<CurrentPlayerNotification> currentPlayerNotification = new SimpleObjectProperty<>();
    private final ObjectProperty<ShowYourCardNotification> showYourCardNotification = new SimpleObjectProperty<>();

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

    public PlayerStatusNotification getPlayerStatusNotification() {
        return playerStatusNotification.get();
    }

    public void setPlayerStatusNotification(PlayerStatusNotification playerStatusNotification) {
        this.playerStatusNotification.set(playerStatusNotification);
    }

    public ObjectProperty<PlayerStatusNotification> playerStatusNotificationProperty() {
        return playerStatusNotification;
    }

    public GameStatusNotification getGameStatusNotification() {
        return gameStatusNotification.get();
    }

    public void setGameStatusNotification(GameStatusNotification gameStatusNotification) {
        this.gameStatusNotification.set(gameStatusNotification);
    }

    public ObjectProperty<GameStatusNotification> gameStatusNotificationProperty() {
        return gameStatusNotification;
    }

    public GamePausedNotification getGamePausedNotification() {
        return gamePausedNotification.get();
    }

    public void setGamePausedNotification(GamePausedNotification gamePausedNotification) {
        this.gamePausedNotification.set(gamePausedNotification);
    }

    public ObjectProperty<GamePausedNotification> gamePausedNotificationProperty() {
        return gamePausedNotification;
    }

    public CurrentPlayerNotification getCurrentPlayerNotification() {
        return currentPlayerNotification.get();
    }

    public void setCurrentPlayerNotification(CurrentPlayerNotification currentPlayerNotification) {
        this.currentPlayerNotification.set(currentPlayerNotification);
    }

    public ObjectProperty<CurrentPlayerNotification> currentPlayerNotificationProperty() {
        return currentPlayerNotification;
    }

    public ShowYourCardNotification getShowYourCardNotification() {
        return showYourCardNotification.get();
    }

    public void setShowYourCardNotification(ShowYourCardNotification showYourCardNotification) {
        this.showYourCardNotification.set(showYourCardNotification);
    }

    public ObjectProperty<ShowYourCardNotification> showYourCardNotificationProperty() {
        return showYourCardNotification;
    }

    public void add(Notification notification) {
        notifications.add(notification);
    }

    private void setHookupChangeListeners() {
        notifications.addListener((ListChangeListener.Change<? extends Notification> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::set);
                }
            }
        });
    }

    private void set(PlayerJoinedNotification notification) {
        playerJoinedNotifications.add(notification);
    }

    private void set(PlayerStatusNotification notification) {
        setPlayerStatusNotification(notification);
    }

    private void set(GameStatusNotification notification) {
        setGameStatusNotification(notification);
    }

    private void set(GamePausedNotification notification) {
        setGamePausedNotification(notification);
    }

    private void set(CurrentPlayerNotification notification) {
        setCurrentPlayerNotification(notification);
    }

    private void set(ShowYourCardNotification notification) {
        setShowYourCardNotification(notification);
    }

    private void set(Notification notification) {
        try {
            Method setMethod = getClass().getDeclaredMethod("set", notification.getClass());
            setMethod.invoke(this, notification);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.warn("Specified method not found. Please check you have set method with {} parameter", notification.getClass());
        }
    }
}
