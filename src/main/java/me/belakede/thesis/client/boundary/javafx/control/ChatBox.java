package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.BorderPane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class ChatBox extends BorderPane {

    public ChatBox() {
        load();
    }

    private void load() {
        ControlLoader.load(this);
    }
}
