package me.belakede.thesis.client.boundary.javafx.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Card {

    private final ObjectProperty<me.belakede.thesis.game.equipment.Card> card = new SimpleObjectProperty<>();

    public Card() {
    }

    public Card(me.belakede.thesis.game.equipment.Card card) {
        setCard(card);
    }

    public me.belakede.thesis.game.equipment.Card getCard() {
        return card.get();
    }

    public void setCard(me.belakede.thesis.game.equipment.Card card) {
        this.card.set(card);
    }

    public ObjectProperty<me.belakede.thesis.game.equipment.Card> cardProperty() {
        return card;
    }
}
