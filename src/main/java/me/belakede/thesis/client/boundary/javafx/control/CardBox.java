package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Card;

public class CardBox extends StackPane {

    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();

    @FXML
    private Text label;

    public CardBox() {
        load();
        hookupChangeListeners();
    }

    public CardBox(Card card) {
        load();
        hookupChangeListeners();
        setCard(card);
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

    private void hookupChangeListeners() {
        label.textProperty().bind(cardProperty().asString());
    }

}
