package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Suspect;
import org.controlsfx.control.PopOver;

public class NoteField extends Pane {

    @FXML
    private Button markerButton;
    private PopOver popOver;

    public NoteField(String owner, Card card) {
        load();
        setupPopOver();
        setupActionEvents();
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setupPopOver() {
        popOver = new PopOver(new MarkerPane("admin", Suspect.SCARLET));
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        popOver.setAutoHide(true);
    }

    private void setupActionEvents() {
        markerButton.setOnAction(event -> popOver.show(this));
    }
}
