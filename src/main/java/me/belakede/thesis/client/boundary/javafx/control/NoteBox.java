package me.belakede.thesis.client.boundary.javafx.control;

import com.google.common.base.CaseFormat;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.game.equipment.*;

import java.util.*;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class NoteBox extends GridPane {

    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();
    private final MapProperty<String, Integer> playersOrder = new SimpleMapProperty<>();
    private final MapProperty<Card, Integer> cardsOrder = new SimpleMapProperty<>();
    private final ListProperty<Note> notes = new SimpleListProperty<>();

    public NoteBox() {
        load(this);
        hookupChangeListeners();
    }

    public MapProperty<Suspect, String> playersProperty() {
        return players;
    }

    public ListProperty<Note> notesProperty() {
        return notes;
    }

    private void hookupChangeListeners() {
        players.addListener((observable, oldValue, newValue) -> {
            playersOrder.set(FXCollections.observableMap(new HashMap<>()));
            cardsOrder.set(FXCollections.observableMap(new HashMap<>()));
            displayHeadlines();
            displayCardHeadlines();
            displayNoteFields();
        });
        notes.addListener((observable, oldValue, newValue) -> {
            displayCardHeadlines();
            displayNoteFields();
        });
    }

    private void displayHeadlines() {
        int columnIndex = 1;
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        for (Map.Entry<Suspect, String> entry : players.entrySet()) {
            Label player = new Label(entry.getValue());
            player.getStyleClass().add(entry.getKey().name().toLowerCase());
            player.setRotate(75);
            add(createPlayerHeaderBox(player, calculateLabelWidth(fontLoader, player)), columnIndex, 1);
            playersOrder.put(entry.getValue(), columnIndex);
            columnIndex++;
        }
    }

    private float calculateLabelWidth(FontLoader fontLoader, Label player) {
        return fontLoader.computeStringWidth(player.getText(), player.getFont());
    }

    private void displayCardHeadlines() {
        int rowIndex = 2;
        rowIndex = createCardHeaderBoxes(rowIndex, Suspect.values());
        rowIndex = createCardHeaderBoxes(rowIndex, Room.values());
        rowIndex = createCardHeaderBoxes(rowIndex, Weapon.values());
    }

    private <T extends Enum<T>> int createCardHeaderBoxes(int rowIndex, T[] values) {
        for (T t : values) {
            add(createCardHeaderBox(convertCardToHuman(t.name())), 0, rowIndex);
            cardsOrder.put((Card) t, rowIndex++);
        }
        return rowIndex + 2;
    }

    private VBox createPlayerHeaderBox(Label player, float fontWidth) {
        VBox result = new VBox(player);
        result.setPrefHeight(fontWidth);
        result.setMinHeight(fontWidth);
        result.setMaxHeight(fontWidth);
        result.setAlignment(Pos.CENTER);
        result.setPadding(new Insets(0, 0, 10, 0));
        return result;
    }

    private String convertCardToHuman(String name) {
        List<String> nameParts = new ArrayList<>(Arrays.asList(name.split("_")));
        StringBuilder builder = new StringBuilder();
        builder.append(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, nameParts.get(0)));
        nameParts.remove(0);
        nameParts.forEach(part -> builder.append(" ").append(part.toLowerCase()));
        return builder.toString();
    }

    private VBox createCardHeaderBox(String card) {
        VBox result = new VBox(new Label(card));
        result.setAlignment(Pos.CENTER_RIGHT);
        result.setPadding(new Insets(0, 5, 0, 0));
        return result;
    }

    private void displayNoteFields() {
        Map<Card, List<String>> notes = createCardPlayerMap();
        notesProperty().forEach(note -> {
            NoteField noteField = new NoteField(note.getOwner(), note.getCard(), note.getMarker());
            add(noteField, playersOrder.get(note.getOwner()), cardsOrder.get(note.getCard()));
            correctNotes(notes, note);
        });
        notes.forEach((card, players) -> players.forEach(player -> {
            NoteField noteField = new NoteField(player, card, Marker.NONE);
            add(noteField, playersOrder.get(player), cardsOrder.get(card));
        }));
    }

    private void correctNotes(Map<Card, List<String>> notes, Note note) {
        notes.get(note.getCard()).remove(note.getOwner());
        if (notes.get(note.getCard()).isEmpty()) {
            notes.remove(note.getCard());
        }
    }

    private Map<Card, List<String>> createCardPlayerMap() {
        Map<Card, List<String>> notes = new HashMap<>();
        Arrays.stream(Suspect.values()).forEach(suspect -> {
            notes.put(suspect, new ArrayList<>());
            players.values().forEach(player -> notes.get(suspect).add(player));
        });
        Arrays.stream(Room.values()).forEach(room -> {
            notes.put(room, new ArrayList<>());
            players.values().forEach(player -> notes.get(room).add(player));
        });
        Arrays.stream(Weapon.values()).forEach(weapon -> {
            notes.put(weapon, new ArrayList<>());
            players.values().forEach(player -> notes.get(weapon).add(player));
        });
        return notes;
    }
}
