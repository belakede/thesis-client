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
import me.belakede.thesis.game.Game;
import me.belakede.thesis.game.equipment.*;
import me.belakede.thesis.internal.game.util.GameBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

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
            gamePane.setGame(game);
            gamePane.setCards(cards);
            gamePane.setFigurine(Suspect.GREEN);
            notePane.setFigurine(Suspect.GREEN);
            notePane.setUsers(players);
            historyPane.setFigurine(Suspect.GREEN);
            chatPane.setFigurine(Suspect.GREEN);
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

}
