package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class ActionPane extends VBox {

    public ActionPane() {
        load();
    }

    private void load() {
        ControlLoader.load(this);
    }

}
