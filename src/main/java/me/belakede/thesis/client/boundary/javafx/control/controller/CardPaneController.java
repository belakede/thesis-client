package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.application.Platform;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import me.belakede.thesis.client.boundary.javafx.control.CardBox;
import me.belakede.thesis.client.boundary.javafx.task.ShowTask;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.client.service.PlayerService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.server.game.response.CardNotification;
import me.belakede.thesis.server.game.response.SuspicionNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class CardPaneController implements Initializable {

    private final MapProperty<Card, CardBox> cardBoxes = new SimpleMapProperty<>();
    private final UserService userService;
    private final PlayerService playerService;
    private final NotificationService notificationService;

    @FXML
    private HBox parent;

    @Autowired
    public CardPaneController(UserService userService, PlayerService playerService, NotificationService notificationService) {
        this.userService = userService;
        this.playerService = playerService;
        this.notificationService = notificationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCardBoxes();
        hookupChangeListeners();
    }

    public ObservableMap<Card, CardBox> getCardBoxes() {
        return cardBoxes.get();
    }

    public void setCardBoxes(ObservableMap<Card, CardBox> cardBoxes) {
        this.cardBoxes.set(cardBoxes);
    }

    public MapProperty<Card, CardBox> cardBoxesProperty() {
        return cardBoxes;
    }

    private void initCardBoxes() {
        if (playerService.getCards() != null) {
            uploadCardBoxes(playerService.getCards());
        }
    }

    private void hookupChangeListeners() {
        playerService.cardsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                uploadCardBoxes(newValue);
            }
        });
        notificationService.currentPlayerNotificationProperty().addListener((observable, oldValue, newValue) -> {
            cardBoxes.values().forEach(cardBox -> cardBox.setDisable(true));
        });
        notificationService.suspicionNotificationsProperty().addListener((ListChangeListener.Change<? extends SuspicionNotification> change) -> {
            while (change.next()) {
                if (playerService.isNext() && change.wasAdded()) {
                    change.getAddedSubList().forEach(newValue -> {
                        if (playerService.hasAnyOfThem(newValue.getSuspect(), newValue.getRoom(), newValue.getWeapon())) {
                            enableCardBox(newValue.getSuspect());
                            enableCardBox(newValue.getRoom());
                            enableCardBox(newValue.getWeapon());
                        } else {
                            sendEmptyShowRequest();
                        }
                    });
                }
            }
        });
        notificationService.cardNotificationsProperty().addListener((ListChangeListener.Change<? extends CardNotification> change) -> {
            cardBoxes.values().forEach(cardBox -> cardBox.setDisable(true));
        });
    }

    private void uploadCardBoxes(ObservableList<Card> newValue) {
        setCardBoxes(FXCollections.observableHashMap());
        newValue.forEach(card -> getCardBoxes().put(card, createCardBox(card)));
        Platform.runLater(() -> {
            parent.getChildren().clear();
            parent.getChildren().addAll(getCardBoxes().values());
        });
    }

    private CardBox createCardBox(Card card) {
        CardBox cardBox = new CardBox(card);
        cardBox.setDisable(true);
        return cardBox;
    }

    private void enableCardBox(Card card) {
        CardBox cardBox = getCardBoxes().get(card);
        if (cardBox != null) {
            cardBox.setDisable(false);
            cardBox.setOnMouseClicked(event -> {
                ShowTask task = new ShowTask(userService, cardBox.getCard());
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            });
        }
    }

    private void sendEmptyShowRequest() {
        ShowTask task = new ShowTask(userService, null);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
