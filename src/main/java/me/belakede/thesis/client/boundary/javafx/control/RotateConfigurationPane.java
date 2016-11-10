package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class RotateConfigurationPane extends VBox {

    private final ObservableList<Rotate> rotates = FXCollections.observableArrayList();
    private final DoubleProperty size = new SimpleDoubleProperty();
    private final DoubleProperty pivot = new SimpleDoubleProperty();

    @FXML
    private Slider roll;
    @FXML
    private Slider pitch;
    @FXML
    private Label rollValue;
    @FXML
    private Label pitchValue;

    public RotateConfigurationPane() {
        load();
        hookupChangeListeners();
    }

    public RotateConfigurationPane(double size) {
        load();
        hookupChangeListeners();
        setSize(size);
    }

    public ObservableList<Rotate> getRotates() {
        if (rotates.isEmpty()) {
            uploadRotates();
        }
        return rotates;
    }

    public double getSize() {
        return size.get();
    }

    public void setSize(double size) {
        this.size.set(size);
    }

    public DoubleProperty sizeProperty() {
        return size;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void uploadRotates() {
        rotates.addAll(createRotation(roll, Rotate.Z_AXIS), createRotation(pitch, Rotate.X_AXIS));
    }

    private Rotate createRotation(Slider slider, Point3D point3D) {
        Rotate rotate = new Rotate(0, 0, 0, 0, point3D);
        rotate.pivotXProperty().bind(pivot);
        rotate.pivotYProperty().bind(pivot);
        rotate.pivotZProperty().bind(pivot);
        rotate.angleProperty().bind(slider.valueProperty());
        return rotate;
    }

    private void hookupChangeListeners() {
        pivot.bind(sizeProperty().divide(2.0));
        roll.valueProperty().addListener((observable, oldValue, newValue) -> roll.setValue(newValue.intValue()));
        pitch.valueProperty().addListener((observable, oldValue, newValue) -> pitch.setValue(newValue.intValue()));
        rollValue.textProperty().bind(roll.valueProperty().asString("Roll with %.0f deg"));
        pitchValue.textProperty().bind(pitch.valueProperty().asString("Pitch with %.0f deg"));
    }
}
