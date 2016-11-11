package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import me.belakede.thesis.client.boundary.javafx.control.ChatPane;
import me.belakede.thesis.client.boundary.javafx.control.GamePane;
import me.belakede.thesis.client.boundary.javafx.control.NotePane;
import me.belakede.thesis.game.Game;
import me.belakede.thesis.game.equipment.*;
import me.belakede.thesis.internal.game.util.GameBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private GamePane gamePane;
    @FXML
    private NotePane notePane;
    @FXML
    private ChatPane chatPane;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Game game = GameBuilder.create().boardType(BoardType.DEFAULT).mystery().players(4).positions().build();
            ObservableList<Card> cards = FXCollections.observableList(Arrays.asList(Weapon.CANDLESTICK, Room.DINING_ROOM, Weapon.LEAD_PIPE, Suspect.PEACOCK, Room.BILLIARD_ROOM));
            gamePane.setGame(game);
            gamePane.setFigurine(Suspect.PLUM);
            gamePane.setCards(cards);
            notePane.setFigurine(Suspect.PLUM);
            chatPane.setFigurine(Suspect.PLUM);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
