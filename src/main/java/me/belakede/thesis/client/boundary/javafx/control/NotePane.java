package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import org.controlsfx.control.PopOver;

public class NotePane extends StackPane {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final MapProperty<Suspect, String> users = new SimpleMapProperty<>();
    private final ListProperty<Note> notes = new SimpleListProperty<>();
    @FXML
    private Button noteButton;
    private PopOver popOver;
    private NoteBox noteBox;

    public NotePane() {
        load();
        setupNoteBox();
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

    public ObservableList<Note> getNotes() {
        return notes.get();
    }

    public void setNotes(ObservableList<Note> notes) {
        this.notes.set(notes);
    }

    public ListProperty<Note> notesProperty() {
        return notes;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setupPopover() {
        popOver = new PopOver(noteBox);
        popOver.setAnimated(true);
        popOver.setTitle("Notes");
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);
    }

    private void setupNoteBox() {
        noteBox = new NoteBox();
        noteBox.playersProperty().bind(usersProperty());
        noteBox.notesProperty().bind(notesProperty());
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
