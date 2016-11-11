package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Figurine;
import org.controlsfx.control.PopOver;

public class NotePane extends StackPane {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    @FXML
    private Button noteButton;
    private PopOver popOver;

    public NotePane() {
        load();
        setupPopover();
        hookupChangeListeners();
    }

    public NotePane(Figurine figurine) {
        load();
        setupPopover();
        hookupChangeListeners();
        setFigurine(figurine);
    }

    public Figurine getFigurine() {
        return figurine.get();
    }

    public void setFigurine(Figurine figurine) {
        this.figurine.set(figurine);
    }

    public ObjectProperty<Figurine> figurineProperty() {
        return figurine;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setupPopover() {
        popOver = new PopOver(new VBox(50, new Label("Hello World")));
        popOver.setAnimated(true);
        popOver.setTitle("Note");
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);
    }

    private void hookupChangeListeners() {
        figurine.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue != null) {
                    noteButton.getStyleClass().remove(oldValue.name().toLowerCase());
                }
                noteButton.getStyleClass().add(newValue.name().toLowerCase());
            }
        });
        noteButton.setOnAction(event -> {
            if (popOver.isShowing()) {
                popOver.hide();
            } else {
                popOver.show(this);
            }
        });
    }

}
