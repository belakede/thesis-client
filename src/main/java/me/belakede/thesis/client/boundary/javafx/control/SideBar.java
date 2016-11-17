package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.control.controller.SideBarController;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class SideBar extends VBox {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();
    private final SideBarController controller;

    public SideBar() {
        controller = SPRING_FXML_LOADER.load(this);
    }

    public void bindSize(ReadOnlyDoubleProperty value) {
        controller.getRotateConfiguration().sizeProperty().unbind();
        controller.getRotateConfiguration().sizeProperty().bind(value);
    }

    public ObservableList<Rotate> getRotates() {
        return controller.getRotateConfiguration().getRotates();
    }

}
