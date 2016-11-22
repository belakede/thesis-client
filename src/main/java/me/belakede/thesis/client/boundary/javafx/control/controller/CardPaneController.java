package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.application.Platform;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import me.belakede.thesis.client.boundary.javafx.control.CardBox;
import me.belakede.thesis.client.service.PlayerService;
import me.belakede.thesis.game.equipment.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class CardPaneController implements Initializable {

    private final MapProperty<Card, CardBox> cardBoxes = new SimpleMapProperty<>();
    private final PlayerService playerService;

    @FXML
    private HBox parent;

    @Autowired
    public CardPaneController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCardBoxex();
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

    private void initCardBoxex() {
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

}
