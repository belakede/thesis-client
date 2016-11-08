package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.control.CardPane;
import me.belakede.thesis.client.boundary.javafx.model.Card;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;

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
        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < 27; j++) {
                boardPane.getChildren().add(new Rectangle(15, 15, Color.web("#cccccc")));
            }
        }
        addCards();
        hookupChangeListeners();
        addRotation();
    }

    private void addCards() {
        List<CardPane> cardPanes = Arrays.asList(
                new CardPane(new Card(Room.BATHROOM)),
                new CardPane(new Card(Suspect.MUSTARD)),
                new CardPane(new Card(Suspect.PLUM)),
                new CardPane(new Card(Weapon.KNIFE)),
                new CardPane(new Card(Room.HALL)),
                new CardPane(new Card(Room.LIBRARY)),
                new CardPane(new Card(Weapon.ROPE)),
                new CardPane(new Card(Room.DINING_ROOM)),
                new CardPane(new Card(Suspect.PEACOCK))
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
