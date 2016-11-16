package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import me.belakede.thesis.client.boundary.javafx.control.MarkerPane;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope("prototype")
public class NoteFieldController implements Initializable {

    private final ObjectProperty<MarkerPane> markerPane = new SimpleObjectProperty<>();

    @FXML
    private Pane parent;
    @FXML
    private Button markerButton;
    @FXML
    private Glyph markerGlyph;
    private PopOver popOver;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPopOver();
        setupActionEvents();
        hookupChangeListeners();
    }

    public MarkerPane getMarkerPane() {
        return markerPane.get();
    }

    public void setMarkerPane(MarkerPane markerPane) {
        this.markerPane.set(markerPane);
    }

    public ObjectProperty<MarkerPane> markerPaneProperty() {
        return markerPane;
    }

    private void setupPopOver() {
        popOver = new PopOver();
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        popOver.setAutoHide(true);
    }

    private void setupActionEvents() {
        markerButton.setOnAction(event -> popOver.show(parent));
    }

    private void hookupChangeListeners() {
        markerPane.addListener((observable, oldValue, newValue) -> {
            popOver.setContentNode(newValue);
            markerGlyph.iconProperty().bind(newValue.iconProperty());
            markerGlyph.iconProperty().addListener((observable1, oldValue1, newValue1) -> popOver.hide());
        });
    }
}
