package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.server.game.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationConverterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConverterService.class);
    private final ListProperty<String> notifications = new SimpleListProperty<>();
    private final ObjectProperty<ResourceBundle> resourceBundle = new SimpleObjectProperty<>();
    private final ObjectProperty<Locale> locale = new SimpleObjectProperty<>();
    private final NotificationService notificationService;

    @Autowired
    public NotificationConverterService(NotificationService notificationService) {
        this.notificationService = notificationService;
        setNotifications(FXCollections.observableArrayList());
        setupListeners();
        setupBindings();
    }

    private void setupBindings() {
        notificationService.getNotifications().addListener((ListChangeListener.Change<? extends Notification> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().map(this::readText).forEach(notifications::add);
                }
            }
        });
        localeProperty().bind(SpringFxmlLoader.DEFAULT_LOCALE);
    }

    private void setupListeners() {
        localeProperty().addListener((observable, oldValue, newValue) -> {
            setResourceBundle(ResourceBundle.getBundle("bundles/bundles", newValue));
        });
    }

    public ObservableList<String> getNotifications() {
        return notifications.get();
    }

    public void setNotifications(ObservableList<String> notifications) {
        this.notifications.set(notifications);
    }

    public ListProperty<String> notificationsProperty() {
        return notifications;
    }

    public String readText(Notification notification) {
        String result = "";
        try {
            Method method = getClass().getDeclaredMethod("readText", notification.getClass());
            method.setAccessible(true);
            result = (String) method.invoke(this, notification);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.warn("Specified method not found. Please check you have readText method with {} parameter", notification.getClass());
        }
        return result;
    }

    public String readText(AccusationNotification notification) {
        String suspect = cardAsString(notification.getSuspect());
        String room = cardAsString(notification.getRoom());
        String weapon = cardAsString(notification.getWeapon());
        return String.format(templateFor(notification), suspect, room, weapon);
    }

    public String readText(CardNotification notification) {
        String result = templateFor("NoCardNotification");
        if (notification.isShown()) {
            if (notification.getCard() == null) {
                result = templateFor("HaveCardNotification");
            } else {
                result = String.format(templateFor("CardNotification"), cardAsString(notification.getCardValue().get()));
            }
        }
        return result;
    }

    public String readText(CurrentPlayerNotification notification) {
        return String.format(templateFor(notification), notification.getCurrent(), notification.getNext());
    }

    public String readText(FigurineNotification notification) {
        Coordinate position = notification.getPosition();
        String figurine = cardAsString((Card) notification.getFigurine());
        return String.format(templateFor(notification), figurine, position.getRow(), position.getColumn());
    }

    public String readText(GameEndedNotification notification) {
        return String.format(templateFor(notification), notification.getWinner());
    }

    public String readText(GamePausedNotification notification) {
        return templateFor(notification);
    }

    public String readText(GameStatusNotification notification) {
        String boardType = boardTypeAsString(notification.getBoardStatus().getBoardType());
        return String.format(templateFor(notification), boardType, notification.getPlayers().values());
    }

    public String readText(PairOfDiceNotification notification) {
        return String.format(templateFor(notification), notification.getFirst(), notification.getSecond());
    }

    public String readText(PlayerJoinedNotification notification) {
        return String.format(templateFor(notification), notification.getUser());
    }

    public String readText(PlayerOutNotification notification) {
        return String.format(templateFor(notification), notification.getUser(), cardsAsString(notification.getCardSet()));
    }

    public String readText(PlayerStatusNotification notification) {
        return String.format(templateFor(notification), cardAsString(notification.getFigurine()), cardsAsString(notification.getCardSet()));
    }

    public String readText(ShowYourCardNotification notification) {
        return templateFor(notification);
    }

    public String readText(SuspicionNotification notification) {
        String suspect = cardAsString(notification.getSuspect());
        String room = cardAsString(notification.getRoom());
        String weapon = cardAsString(notification.getWeapon());
        return String.format(templateFor(notification), suspect, room, weapon);
    }

    private List<String> cardsAsString(Set<Card> cards) {
        return cards.stream().map(this::cardAsString).collect(Collectors.toList());
    }

    private String templateFor(Notification notification) {
        return getResourceBundle().getString(notification.getClass().getSimpleName());
    }

    private String templateFor(String template) {
        return getResourceBundle().getString(template);
    }

    private String cardAsString(Card card) {
        return getResourceBundle().getString(card.name());
    }

    private String boardTypeAsString(BoardType boardType) {
        return getResourceBundle().getString(boardType.name());
    }

    private ResourceBundle getResourceBundle() {
        return resourceBundle.get();
    }

    private void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle.set(resourceBundle);
    }

    private ObjectProperty<ResourceBundle> resourceBundleProperty() {
        return resourceBundle;
    }

    public Locale getLocale() {
        return locale.get();
    }

    public void setLocale(Locale locale) {
        this.locale.set(locale);
    }

    public ObjectProperty<Locale> localeProperty() {
        return locale;
    }
}
