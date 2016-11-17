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
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.game.Game;
import me.belakede.thesis.game.equipment.*;
import me.belakede.thesis.internal.game.util.GameBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Controller
public class GameController implements Initializable {

    private final NotificationService notificationService;

    @FXML
    private GamePane gamePane;
    @FXML
    private NotePane notePane;
    @FXML
    private HistoryPane historyPane;
    @FXML
    private ChatPane chatPane;

    @Autowired
    public GameController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Game game = GameBuilder.create().boardType(BoardType.DEFAULT).mystery().players(4).positions().build();
            ObservableList<Card> cards = FXCollections.observableList(Arrays.asList(Weapon.CANDLESTICK, Room.DINING_ROOM, Weapon.LEAD_PIPE, Suspect.PEACOCK, Room.BILLIARD_ROOM));
            ObservableMap<Suspect, String> players = FXCollections.observableMap(notificationService.getGameStatusNotification().getPlayers());
            gamePane.setGame(game);
            gamePane.setCards(cards);
            gamePane.setFigurine(Suspect.GREEN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
