package me.belakede.thesis.client.boundary.javafx.converter;

import javafx.util.StringConverter;
import me.belakede.thesis.game.equipment.Suspect;

import java.util.ResourceBundle;

public class SuspectStringConverter extends StringConverter<Suspect> {

    private final ResourceBundle resourceBundle;

    public SuspectStringConverter(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public String toString(Suspect object) {
        return resourceBundle.getString(object.name());
    }

    @Override
    public Suspect fromString(String string) {
        return resourceBundle.keySet().stream().filter(key -> string.equals(resourceBundle.getString(key))).map(Suspect::valueOf).findFirst().get();
    }
}
