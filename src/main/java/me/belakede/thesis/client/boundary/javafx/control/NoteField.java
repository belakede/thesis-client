package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.Glyph;

public class NoteField extends Pane {

    @FXML
    private Button markerButton;
    @FXML
    private Glyph markerGlyph;
    private PopOver popOver;
    private MarkerPane markerPane;

    public NoteField(String owner, Card card, Marker marker) {
        load();
        setupMarkerPane(owner, card, marker);
        setupPopOver();
        setupActionEvents();
        hookupChangeListeners();
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setupMarkerPane(String owner, Card card, Marker marker) {
        markerPane = new MarkerPane(owner, card, marker);
    }

    private void setupPopOver() {
        popOver = new PopOver(markerPane);
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        popOver.setAutoHide(true);
    }

    private void setupActionEvents() {
        markerButton.setOnAction(event -> popOver.show(this));
    }

    private void hookupChangeListeners() {
        markerGlyph.iconProperty().bind(markerPane.iconProperty());
    }
}
