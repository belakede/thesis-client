package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.HBox;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class CardPane extends HBox {

    public CardPane() {
        load();
    }

    private void load() {
        ControlLoader.load(this);
    }

}
