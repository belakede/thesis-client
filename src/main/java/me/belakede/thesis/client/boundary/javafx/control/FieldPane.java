package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.model.Field;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class FieldPane extends StackPane {

    private final SimpleObjectProperty<Field> field = new SimpleObjectProperty<>();
    @FXML
    private Pane content;

    public FieldPane(Field field) {
        load();
        setField(field);
        setStyleClasses();
    }

    public Field getField() {
        return field.get();
    }

    public void setField(Field field) {
        this.field.set(field);
    }

    public SimpleObjectProperty<Field> fieldProperty() {
        return field;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setStyleClasses() {
        content.getStyleClass().add(fieldProperty().getValue().getFieldType().name().toLowerCase());
    }

}
