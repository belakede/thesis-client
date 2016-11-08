package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import me.belakede.thesis.client.boundary.javafx.model.Card;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class CardPane extends StackPane {

    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    @FXML
    private Rectangle rectangle;
    @FXML
    private Text text;

    public CardPane() {
        this(new Card());
    }

    public CardPane(Card card) {
        load();
        setCard(card);
        setBindings();
        rotate();
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

    private void setBindings() {
        text.textProperty().bind(cardProperty().get().cardProperty().asString());
    }

    private void rotate() {
        rectangle.rotateProperty().setValue(Math.random() * 40 - 20);
    }

}
