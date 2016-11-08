package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class Card extends StackPane {

    @FXML
    private Rectangle rectangle;
    @FXML
    private Text text;

    public Card() {
        load();
        rotate();
    }

    public Card(me.belakede.thesis.game.equipment.Card card) {
        load();
        setLabel(card);
        rotate();
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setLabel(me.belakede.thesis.game.equipment.Card card) {
        text.textProperty().setValue(card.name());
    }

    private void rotate() {
        rectangle.rotateProperty().setValue(Math.random() * 40 - 20);
    }

}
