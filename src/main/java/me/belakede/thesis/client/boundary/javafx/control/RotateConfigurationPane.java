package me.belakede.thesis.client.boundary.javafx.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

import java.math.BigDecimal;

public class RotateConfigurationPane extends VBox {

    private final ObservableList<Rotate> rotates = FXCollections.observableArrayList();
    private BigDecimal size;
    @FXML
    private Slider roll;
    @FXML
    private Slider pitch;
    @FXML
    private Label rollValue;
    @FXML
    private Label pitchValue;

    public RotateConfigurationPane() {
        this.size = BigDecimal.valueOf(getScene().getWidth());
        load();
    }

    public RotateConfigurationPane(double size) {
        this.size = BigDecimal.valueOf(size);
        load();
    }

    public ObservableList<Rotate> getRotates() {
        if (rotates.isEmpty()) {
            uploadRotates();
        }
        return rotates;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void uploadRotates() {
        rotates.addAll(createRotation(roll, Rotate.Z_AXIS), createRotation(pitch, Rotate.X_AXIS));
    }

    private Rotate createRotation(Slider slider, Point3D point3D) {
        double center = size.divide(BigDecimal.valueOf(2)).doubleValue();
        Rotate rotate = new Rotate(0, center, center, 0, point3D);
        rotate.angleProperty().bind(slider.valueProperty());
        return rotate;
    }
}
