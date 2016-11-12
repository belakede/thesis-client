package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.game.equipment.Suspect;

import java.util.Map;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class NoteBox extends GridPane {

    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();
    private final ListProperty<Note> notes = new SimpleListProperty<>();

    public NoteBox(ObservableMap<Suspect, String> players, ObservableList<Note> notes) {
        load(this);
        setPlayers(players);
        setNotes(notes);
        displayHeadlines();
    }

    public ObservableMap<Suspect, String> getPlayers() {
        return players.get();
    }

    public void setPlayers(ObservableMap<Suspect, String> players) {
        this.players.set(players);
    }

    public MapProperty<Suspect, String> playersProperty() {
        return players;
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

    private void displayHeadlines() {
        int columnIndex = 1;
        for (Map.Entry<Suspect, String> entry : players.entrySet()) {
            Text player = new Text(entry.getValue());
            player.getStyleClass().add(entry.getKey().name().toLowerCase());
            player.setRotate(75);
            add(player, columnIndex, 1);
            columnIndex++;
        }
    }
}
