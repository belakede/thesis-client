package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private TilePane boardPane;
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
        Rotate horizontalRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate verticalRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate theThirdOneRotate = new Rotate(0, Rotate.Z_AXIS);
        horizontalRotate.angleProperty().bind(sliderHorizontal.valueProperty());
        verticalRotate.angleProperty().bind(sliderVertical.valueProperty());
        theThirdOneRotate.angleProperty().bind(sliderTheThirdOne.valueProperty());
        sliderHorizontalLabel.textProperty().bind(sliderHorizontal.valueProperty().asString("%.2f"));
        sliderVerticalLabel.textProperty().bind(sliderVertical.valueProperty().asString("%.2f"));
        sliderTheThirdOneLabel.textProperty().bind(sliderTheThirdOne.valueProperty().asString("%.2f"));
        boardPane.getTransforms().addAll(horizontalRotate, verticalRotate, theThirdOneRotate);
    }
}
