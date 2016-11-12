package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.*;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotePane extends StackPane {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final MapProperty<Suspect, String> users = new SimpleMapProperty<>();
    @FXML
    private Button noteButton;
    private PopOver popOver;

    public NotePane() {
        load();
        setupPopover();
        hookupChangeListeners();
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

    public ObservableMap<Suspect, String> getUsers() {
        return users.get();
    }

    public void setUsers(ObservableMap<Suspect, String> users) {
        this.users.set(users);
    }

    public MapProperty<Suspect, String> usersProperty() {
        return users;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setupPopover() {
        Map<Suspect, String> players = new HashMap<>();
        players.put(Suspect.WHITE, "Player 1");
        players.put(Suspect.GREEN, "Player 2");
        players.put(Suspect.MUSTARD, "Player 3");
        players.put(Suspect.PEACOCK, "Player 4");
        players.put(Suspect.PLUM, "Player 5");
        players.put(Suspect.SCARLET, "Player 6");
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(Suspect.GREEN, "Player 3", Marker.YES));
        notes.add(new Note(Weapon.CANDLESTICK, "Player 2", Marker.MAYBE));
        notes.add(new Note(Room.DINING_ROOM, "Player 5", Marker.MAYBE_NOT));
        notes.add(new Note(Suspect.MUSTARD, "Player 6", Marker.QUESTION));
        notes.add(new Note(Weapon.KNIFE, "Player 1", Marker.NOT));
        NoteBox noteBox = new NoteBox(FXCollections.observableMap(players), FXCollections.observableList(notes));
        popOver = new PopOver(new VBox(noteBox));
        popOver.setAnimated(true);
        popOver.setTitle("Notes");
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
