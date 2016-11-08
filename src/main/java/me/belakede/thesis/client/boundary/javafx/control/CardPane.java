package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.model.Card;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class CardPane extends StackPane {

    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    @FXML
    private Pane content;

    public CardPane(Card card) {
        load();
        setCard(card);
        setBindings();
        setOnMouseEnteredEvent();
        rotate();
    }

    private void setBindings() {
        content.getStyleClass().addAll(card.getValue().getCard().name().toLowerCase());
    }

    public Card getCard() {
        return card.get();
    }

    public void setCard(Card card) {
        this.card.set(card);
    }

    public ObjectProperty<Card> cardProperty() {
        return card;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setOnMouseEnteredEvent() {
        setOnMouseEntered(event -> ((CardPane) event.getSource()).toFront());
    }

    private void rotate() {
        content.rotateProperty().setValue(Math.random() * 40 - 20);
    }

}
