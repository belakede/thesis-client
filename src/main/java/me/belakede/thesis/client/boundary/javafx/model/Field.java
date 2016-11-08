package me.belakede.thesis.client.boundary.javafx.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.field.FieldType;

public class Field {

    private final ObjectProperty<me.belakede.thesis.game.field.Field> field = new SimpleObjectProperty<>();
    private final ObjectProperty<FieldType> fieldType = new SimpleObjectProperty<>();
    private final IntegerProperty row = new SimpleIntegerProperty();
    private final IntegerProperty column = new SimpleIntegerProperty();

    public Field() {
    }

    public Field(me.belakede.thesis.game.field.Field field) {
        setField(field);
        setFieldType(field.getFieldType());
        setRow(field.getRow());
        setColumn(field.getColumn());
    }

    public me.belakede.thesis.game.field.Field getField() {
        return field.get();
    }

    public void setField(me.belakede.thesis.game.field.Field field) {
        this.field.set(field);
    }

    public ObjectProperty<me.belakede.thesis.game.field.Field> fieldProperty() {
        return field;
    }

    public FieldType getFieldType() {
        return fieldType.get();
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType.set(fieldType);
    }

    public ObjectProperty<FieldType> fieldTypeProperty() {
        return fieldType;
    }

    public int getRow() {
        return row.get();
    }

    public void setRow(int row) {
        this.row.set(row);
    }

    public IntegerProperty rowProperty() {
        return row;
    }

    public int getColumn() {
        return column.get();
    }

    public void setColumn(int column) {
        this.column.set(column);
    }

    public IntegerProperty columnProperty() {
        return column;
    }
}
