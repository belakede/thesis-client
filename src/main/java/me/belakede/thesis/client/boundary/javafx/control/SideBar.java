package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Figurine;

public class SideBar extends VBox {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    @FXML
    private RotateConfigurationPane rotateConfiguration;

    public SideBar() {
        load();
    }

    public SideBar(Figurine figurine) {
        load();
        setFigurine(figurine);
    }

    public Figurine getFigurine() {
        return figurine.get();
    }

    public void setFigurine(Figurine figurine) {
        this.figurine.set(figurine);
    }

    public ObjectProperty<Figurine> figurineProperty() {
        return figurine;
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
