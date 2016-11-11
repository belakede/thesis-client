package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Card;

public class CardPane extends HBox {

    private final ListProperty<Card> cards = new SimpleListProperty<>();

    public CardPane() {
        load();
        hookupChangeListeners();
    }

    public CardPane(ObservableList<Card> cards) {
        load();
        hookupChangeListeners();
        setCards(cards);
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

    private void load() {
        ControlLoader.load(this);
    }

    private void hookupChangeListeners() {
        cardsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getChildren().clear();
                newValue.stream().map(CardBox::new).forEach(card -> getChildren().add(card));
            }
        });
    }

}
