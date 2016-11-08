package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.control.CardPane;
import me.belakede.thesis.client.boundary.javafx.control.FieldPane;
import me.belakede.thesis.client.boundary.javafx.model.Card;
import me.belakede.thesis.client.boundary.javafx.model.Field;
import me.belakede.thesis.game.Game;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.internal.game.util.GameBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private TilePane boardPane;
    @FXML
    private AnchorPane cardContainer;
    @FXML
    private Slider sliderHorizontal;
    @FXML
    private Slider sliderVertical;
    @FXML
    private Slider sliderTheThirdOne;
    @FXML
    private Label sliderHorizontalLabel;
    @FXML
    private Label sliderVerticalLabel;
    @FXML
    private Label sliderTheThirdOneLabel;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Game game = GameBuilder.create().boardType(BoardType.DEFAULT).mystery().players(4).positions().build();
            addFields(game, BoardType.DEFAULT.getSize());
            addCards();
            hookupChangeListeners();
            addRotation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFields(Game game, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardPane.getChildren().add(new FieldPane(new Field(game.getBoard().getField(i, j))));
            }
        }
    }

    private void addCards() {
        List<CardPane> cardPanes = Arrays.asList(
                new CardPane(new Card(Suspect.GREEN)),
                new CardPane(new Card(Suspect.MUSTARD)),
                new CardPane(new Card(Suspect.PLUM)),
                new CardPane(new Card(Suspect.PEACOCK)),
                new CardPane(new Card(Suspect.WHITE)),
                new CardPane(new Card(Suspect.SCARLET)),
                new CardPane(new Card(Weapon.ROPE)),
                new CardPane(new Card(Weapon.LEAD_PIPE)),
                new CardPane(new Card(Weapon.REVOLVER))
        );
        double result = 10.0;
        for (int i = 0; i < cardPanes.size(); i++) {
            AnchorPane.setLeftAnchor(cardPanes.get(i), result);
            AnchorPane.setBottomAnchor(cardPanes.get(i), Math.random() * 30 - 15);
            result = result + 55.0;
        }
        cardContainer.getChildren().addAll(cardPanes);
    }

    private void addRotation() {
        Rotate horizontalRotate = new Rotate(0, 202.5, 202.5, 0, Rotate.X_AXIS);
        Rotate verticalRotate = new Rotate(0, 202.5, 202.5, 0, Rotate.Y_AXIS);
        Rotate theThirdOneRotate = new Rotate(0, 202.5, 202.5, 202.5, Rotate.Z_AXIS);
        horizontalRotate.angleProperty().bind(sliderHorizontal.valueProperty());
        verticalRotate.angleProperty().bind(sliderVertical.valueProperty());
        theThirdOneRotate.angleProperty().bind(sliderTheThirdOne.valueProperty());
        sliderHorizontalLabel.textProperty().bind(sliderHorizontal.valueProperty().asString("%.2f"));
        sliderVerticalLabel.textProperty().bind(sliderVertical.valueProperty().asString("%.2f"));
        sliderTheThirdOneLabel.textProperty().bind(sliderTheThirdOne.valueProperty().asString("%.2f"));
        boardPane.getTransforms().addAll(horizontalRotate, verticalRotate, theThirdOneRotate);
    }

    private void hookupChangeListeners() {
        sliderHorizontal.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderHorizontal.setValue(Math.round(newValue.doubleValue()));
        });
        sliderVertical.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderVertical.setValue(Math.round(newValue.doubleValue()));
        });
        sliderTheThirdOne.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderTheThirdOne.setValue(Math.round(newValue.doubleValue()));
        });
    }
}
