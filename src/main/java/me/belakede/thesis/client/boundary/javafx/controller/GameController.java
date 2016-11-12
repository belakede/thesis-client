package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import me.belakede.thesis.client.boundary.javafx.control.ChatPane;
import me.belakede.thesis.client.boundary.javafx.control.GamePane;
import me.belakede.thesis.client.boundary.javafx.control.HistoryPane;
import me.belakede.thesis.client.boundary.javafx.control.NotePane;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.game.Game;
import me.belakede.thesis.game.equipment.*;
import me.belakede.thesis.internal.game.util.GameBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    @FXML
    private GamePane gamePane;
    @FXML
    private NotePane notePane;
    @FXML
    private HistoryPane historyPane;
    @FXML
    private ChatPane chatPane;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Game game = GameBuilder.create().boardType(BoardType.DEFAULT).mystery().players(4).positions().build();
            ObservableList<Card> cards = FXCollections.observableList(Arrays.asList(Weapon.CANDLESTICK, Room.DINING_ROOM, Weapon.LEAD_PIPE, Suspect.PEACOCK, Room.BILLIARD_ROOM));
            ObservableMap<Suspect, String> players = FXCollections.observableMap(createSuspectPlayerMap());
            ObservableList<Note> notes = FXCollections.observableList(createNoteList());
            gamePane.setGame(game);
            gamePane.setCards(cards);
            gamePane.setFigurine(Suspect.PLUM);
            notePane.setFigurine(Suspect.PLUM);
            notePane.setUsers(players);
            notePane.setNotes(notes);
            historyPane.setFigurine(Suspect.PLUM);
            chatPane.setFigurine(Suspect.PLUM);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<Suspect, String> createSuspectPlayerMap() {
        Map<Suspect, String> players = new HashMap<>();
        players.put(Suspect.WHITE, "Player 1");
        players.put(Suspect.GREEN, "Player 2");
        players.put(Suspect.MUSTARD, "Player 3");
        players.put(Suspect.PEACOCK, "Player 4");
        players.put(Suspect.PLUM, "Player 5");
        players.put(Suspect.SCARLET, "Player 6");
        return players;
    }

    private List<Note> createNoteList() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(Suspect.MUSTARD, "Player 5", Marker.NOT));
        notes.add(new Note(Suspect.PEACOCK, "Player 6", Marker.QUESTION));
        notes.add(new Note(Weapon.CANDLESTICK, "Player 2", Marker.MAYBE_NOT));
        notes.add(new Note(Weapon.CANDLESTICK, "Player 3", Marker.MAYBE));
        notes.add(new Note(Room.DINING_ROOM, "Player 2", Marker.YES));
        return notes;
    }

}
