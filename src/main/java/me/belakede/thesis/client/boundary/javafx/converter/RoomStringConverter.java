package me.belakede.thesis.client.boundary.javafx.converter;

import javafx.util.StringConverter;
import me.belakede.thesis.game.equipment.Room;

import java.util.ResourceBundle;

public class RoomStringConverter extends StringConverter<Room> {

    private final ResourceBundle resourceBundle;

    public RoomStringConverter(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public String toString(Room object) {
        return resourceBundle.getString(object.name());
    }

    @Override
    public Room fromString(String string) {
        return resourceBundle.keySet().stream().filter(key -> string.equals(resourceBundle.getString(key))).map(Room::valueOf).findFirst().get();
    }
}
