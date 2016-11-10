package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class ChatPane extends StackPane {

    public ChatPane() {
        load();
    }

    private void load() {
        ControlLoader.load(this);
    }

}
