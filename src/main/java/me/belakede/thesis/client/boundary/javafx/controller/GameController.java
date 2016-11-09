package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.control.BoardPane;
import me.belakede.thesis.client.boundary.javafx.control.CardPane;
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
    @FXML
    private BorderPane hiddenPane;
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
        List<me.belakede.thesis.game.equipment.Card> cards = Arrays.asList(Suspect.MUSTARD, Suspect.PLUM, Weapon.CANDLESTICK, Weapon.KNIFE, Weapon.WRENCH);
        loadBoard(BoardType.DEFAULT);
        addCards(cards);
        hookupChangeListeners();
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
        hiddenPane.setOnMouseEntered(event -> pane.setPinnedSide(Side.RIGHT));
        hiddenPane.setOnMouseExited(event -> pane.setPinnedSide(null));
    }
}
