package me.belakede.thesis.client.boundary.javafx.converter;

import javafx.util.StringConverter;
import me.belakede.thesis.game.equipment.Weapon;

import java.util.ResourceBundle;

public class WeaponStringConverter extends StringConverter<Weapon> {

    private final ResourceBundle resourceBundle;

    public WeaponStringConverter(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public String toString(Weapon object) {
        return resourceBundle.getString(object.name());
    }

    @Override
    public Weapon fromString(String string) {
        return resourceBundle.keySet().stream().filter(key -> string.equals(resourceBundle.getString(key))).map(Weapon::valueOf).findFirst().get();
    }
}
