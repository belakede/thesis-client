package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class SideBar extends VBox {

    @FXML
    private RotateConfigurationPane rotateConfiguration;

    public SideBar() {
        load();
    }

    public void bindSize(ReadOnlyDoubleProperty value) {
        rotateConfiguration.sizeProperty().unbind();
        rotateConfiguration.sizeProperty().bind(value);
    }

    public ObservableList<Rotate> getRotates() {
        return rotateConfiguration.getRotates();
    }

    private void load() {
        ControlLoader.load(this);
    }
}
