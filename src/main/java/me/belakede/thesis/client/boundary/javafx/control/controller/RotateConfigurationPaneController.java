package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class RotateConfigurationPaneController implements Initializable {

    private final ObservableList<Rotate> rotates = FXCollections.observableArrayList();
    private final DoubleProperty size = new SimpleDoubleProperty();
    private final DoubleProperty pivot = new SimpleDoubleProperty();
    private ResourceBundle resources;

    @FXML
    private Slider roll;
    @FXML
    private Label rollValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResources(resources);
        hookupChangeListeners();
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
        rotates.addAll(createRotation(roll, Rotate.Z_AXIS));
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
        rollValue.textProperty().bind(roll.valueProperty().asString(getResources().getString("Roll with %.0f deg")));
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }
}
