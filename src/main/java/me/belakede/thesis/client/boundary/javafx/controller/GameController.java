package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.layout.AnchorPane;
import me.belakede.thesis.client.boundary.javafx.control.BoardPane;
import me.belakede.thesis.client.boundary.javafx.control.CardPane;
import me.belakede.thesis.client.boundary.javafx.control.RotateConfigurationPane;
import me.belakede.thesis.client.boundary.javafx.model.Card;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import org.controlsfx.control.HiddenSidesPane;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameController implements Initializable {

    @FXML
    private AnchorPane cardContainer;
    @FXML
    private HiddenSidesPane pane;

    public void initialize(URL location, ResourceBundle resources) {
        List<me.belakede.thesis.game.equipment.Card> cards = Arrays.asList(Suspect.MUSTARD, Suspect.PLUM, Weapon.CANDLESTICK, Weapon.KNIFE, Weapon.WRENCH);
        loadBoard(BoardType.DEFAULT);
        addCards(cards);
    }

    private void loadBoard(BoardType boardType) {
        BoardPane boardPane = new BoardPane(boardType, FXCollections.observableHashMap());
        boardPane.getStyleClass().add(boardType.name().toLowerCase());
        addRotation(boardPane);
        pane.contentProperty().set(boardPane);
    }

    private void addCards(Collection<me.belakede.thesis.game.equipment.Card> cards) {
        List<CardPane> cardPanes = cards.stream().map(c -> new CardPane(new Card(c))).collect(Collectors.toList());
        double result = 10.0;
        for (int i = 0; i < cardPanes.size(); i++) {
            AnchorPane.setLeftAnchor(cardPanes.get(i), result);
            AnchorPane.setBottomAnchor(cardPanes.get(i), Math.random() * 30 - 15);
            result = result + 55.0;
        }
        cardContainer.getChildren().addAll(cardPanes);
    }

    private void addRotation(BoardPane boardPane) {
        RotateConfigurationPane configurationPane = new RotateConfigurationPane(boardPane.getMinWidth());
        boardPane.getTransforms().addAll(configurationPane.getRotates());
        pane.setRight(configurationPane);
        pane.getRight().setOnMouseEntered(event -> pane.setPinnedSide(Side.RIGHT));
        pane.getRight().setOnMouseExited(event -> pane.setPinnedSide(null));
    }

}
