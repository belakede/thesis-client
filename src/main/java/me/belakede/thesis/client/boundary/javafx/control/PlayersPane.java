package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class PlayersPane extends VBox {

    @FXML
    private VBox parent;
    @FXML
    private ListView<String> players;

    public PlayersPane() {
        load(this);
    }


}
