package me.belakede.thesis.client.boundary.javafx.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;

public class Note {

    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    private final StringProperty owner = new SimpleStringProperty();
    private final SimpleObjectProperty<Marker> marker = new SimpleObjectProperty<>();

    public Note(Card card, String owner, Marker marker) {
        setCard(card);
        setOwner(owner);
        setMarker(marker);
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

    public String getOwner() {
        return owner.get();
    }

    public void setOwner(String owner) {
        this.owner.set(owner);
    }

    public StringProperty ownerProperty() {
        return owner;
    }

    public Marker getMarker() {
        return marker.get();
    }

    public void setMarker(Marker marker) {
        this.marker.set(marker);
    }

    public SimpleObjectProperty<Marker> markerProperty() {
        return marker;
    }
}
