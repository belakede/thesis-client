package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
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

    private final ListProperty<Card> cards = new SimpleListProperty<>();
    private final PlayerService playerService;
    @FXML
    private HBox parent;


    @Autowired
    public CardPaneController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        setupBindings();

    }

    public ObservableList<Card> getCards() {
        return cards.get();
    }

    public void setCards(ObservableList<Card> cards) {
        this.cards.set(cards);
    }

    public ListProperty<Card> cardsProperty() {
        return cards;
    }

    private void hookupChangeListeners() {
        cardsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                parent.getChildren().clear();
                newValue.stream().map(CardBox::new).forEach(card -> parent.getChildren().add(card));
            }
        });
    }

    private void setupBindings() {
        cardsProperty().bind(playerService.cardsProperty());
    }
}
