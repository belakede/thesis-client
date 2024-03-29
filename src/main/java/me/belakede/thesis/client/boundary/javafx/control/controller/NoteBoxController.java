package me.belakede.thesis.client.boundary.javafx.control.controller;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import me.belakede.thesis.client.boundary.javafx.control.NoteField;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.NoteService;
import me.belakede.thesis.client.service.PlayerService;
import me.belakede.thesis.game.equipment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.*;

@Controller
public class NoteBoxController implements Initializable {

    private final MapProperty<Card, Integer> cardsOrder = new SimpleMapProperty<>();
    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();
    private final ListProperty<Note> notes = new SimpleListProperty<>();

    private final FontLoader fontLoader;
    private final GameService gameService;
    private final PlayerService playerService;
    private final NoteService noteService;

    @FXML
    private GridPane parent;
    private ResourceBundle resources;

    @Autowired
    public NoteBoxController(GameService gameService, PlayerService playerService, NoteService noteService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.noteService = noteService;
        this.fontLoader = Toolkit.getToolkit().getFontLoader();
        this.cardsOrder.setValue(FXCollections.observableHashMap());
        this.players.setValue(FXCollections.observableHashMap());
        this.notes.setValue(FXCollections.observableArrayList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResources(resources);
        hookupChangeListeners();
        setupBindings();
    }

    private void hookupChangeListeners() {
        players.addListener((MapChangeListener.Change<? extends Suspect, ? extends String> change) -> {
            displayHeadLines();
            displayCardHeadlines();
            displayNoteFields();
        });
        notes.addListener((observable, oldValue, newValue) -> {
            displayCardHeadlines();
            displayNoteFields();
        });
    }

    private void setupBindings() {
        players.bind(gameService.playersProperty());
        notes.bind(noteService.notesProperty());
    }

    private void displayHeadLines() {
        players.entrySet().forEach((entry -> {
            Label headlineLabel = createHeadlineLabel(entry.getKey(), entry.getValue());
            Double boxWidth = calculateLabelWidth(entry.getValue(), headlineLabel.getFont());
            VBox headerBox = createPlayerHeaderBox(headlineLabel, boxWidth);
            parent.add(headerBox, gameService.getPlayersOrder().get(entry.getValue()), 1);
        }));
    }

    private VBox createPlayerHeaderBox(Label player, double fontWidth) {
        VBox result = new VBox(player);
        result.setPrefHeight(fontWidth);
        result.setMinHeight(fontWidth);
        result.setMaxHeight(fontWidth);
        result.setAlignment(Pos.CENTER);
        result.setPadding(new Insets(0, 0, 10, 0));
        return result;
    }

    private Label createHeadlineLabel(Suspect figurine, String player) {
        Label label = new Label(player);
        label.getStyleClass().add(figurine.name().toLowerCase());
        label.setRotate(75);
        return label;
    }

    private Double calculateLabelWidth(String player, Font font) {
        return (double) fontLoader.computeStringWidth(player, font);
    }

    private void displayCardHeadlines() {
        int rowIndex = 2;
        rowIndex = createCardHeaderBoxes(rowIndex, Suspect.values());
        parent.add(createSeparator(), 0, rowIndex - 1, players.size() + 1, 1);
        rowIndex = createCardHeaderBoxes(rowIndex, Room.values());
        parent.add(createSeparator(), 0, rowIndex - 1, players.size() + 1, 1);
        rowIndex = createCardHeaderBoxes(rowIndex, Weapon.values());
    }

    private <T extends Enum<T>> int createCardHeaderBoxes(int rowIndex, T[] values) {
        for (T t : values) {
            parent.add(createCardHeaderBox(convertCardToHuman(t.name())), 0, rowIndex);
            cardsOrder.put((Card) t, rowIndex++);
        }
        return rowIndex + 2;
    }

    private HBox createSeparator() {
        Separator separator = new Separator();
        HBox result = new HBox(separator);
        result.setAlignment(Pos.CENTER);
        separator.prefWidthProperty().bind(result.widthProperty().subtract(50));
        result.setPadding(new Insets(10, 0, 10, 0));
        return result;
    }

    private String convertCardToHuman(String name) {
        return getResources().getString(name);
    }

    private VBox createCardHeaderBox(String card) {
        VBox result = new VBox(new Label(card));
        result.setAlignment(Pos.CENTER_RIGHT);
        result.setPadding(new Insets(0, 5, 0, 0));
        return result;
    }

    private void displayNoteFields() {
        Map<Card, List<String>> notes = createCardPlayerMap();
        noteService.getNotes().forEach(note -> {
            NoteField noteField = new NoteField(note.getOwner(), note.getCard(), note.getMarker());
            parent.add(noteField, gameService.getPlayersOrder().get(note.getOwner()), cardsOrder.get(note.getCard()));
            correctNotes(notes, note);
        });
        notes.forEach((card, players) -> players.forEach(player -> {
            Marker marker;
            if (playerService.getUsername().equals(player)) {
                marker = (playerService.getCards().contains(card)) ? Marker.YES : Marker.NOT;
            } else {
                marker = (playerService.getCards().contains(card)) ? Marker.NOT : Marker.NONE;
            }
            NoteField noteField = new NoteField(player, card, marker);
            if (!Marker.NONE.equals(marker)) {
                noteField.setDisable(true);
            }
            parent.add(noteField, gameService.getPlayersOrder().get(player), cardsOrder.get(card));
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

    public ResourceBundle getResources() {
        return resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }
}
