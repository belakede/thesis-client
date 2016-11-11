package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.HBox;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class MarkerPane extends HBox {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerPane.class);

    private final ObjectProperty<String> owner = new SimpleObjectProperty<>();
    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    private final ObjectProperty<Marker> marker = new SimpleObjectProperty<>();

    public MarkerPane(String owner, Card card) {
        load(this);
        hookupChangeListeners();
        setOwner(owner);
        setCard(card);
        setMarker(Marker.NONE);
    }

    public String getOwner() {
        return owner.get();
    }

    public void setOwner(String owner) {
        this.owner.set(owner);
    }

    public ObjectProperty<String> ownerProperty() {
        return owner;
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

    public Marker getMarker() {
        return marker.get();
    }

    public void setMarker(Marker marker) {
        this.marker.set(marker);
    }

    public ObjectProperty<Marker> markerProperty() {
        return marker;
    }

    private void hookupChangeListeners() {
        marker.addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Marke {}'s {} card with {}", newValue);
        });
    }
}
