package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.control.controller.RotateConfigurationPaneController;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class RotateConfigurationPane extends VBox {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();
    private final RotateConfigurationPaneController controller;

    public RotateConfigurationPane() {
        controller = SPRING_FXML_LOADER.load(this);
    }

    public DoubleProperty sizeProperty() {
        return controller.sizeProperty();
    }

    public ObservableList<Rotate> getRotates() {
        return controller.getRotates();
    }

}
