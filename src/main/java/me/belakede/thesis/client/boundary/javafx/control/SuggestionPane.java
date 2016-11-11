package me.belakede.thesis.client.boundary.javafx.control;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;

public class SuggestionPane extends GridPane {

    @FXML
    private ChoiceBox<Suspect> suspect;
    @FXML
    private ChoiceBox<Room> room;
    @FXML
    private ChoiceBox<Weapon> weapon;
    @FXML
    private Button submit;

    public SuggestionPane(Type type) {
        load();
        fillChoiceBoxes();
        updateSubmitButton(type);
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void fillChoiceBoxes() {
        suspect.setItems(FXCollections.observableArrayList(Suspect.values()));
        room.setItems(FXCollections.observableArrayList(Room.values()));
        weapon.setItems(FXCollections.observableArrayList(Weapon.values()));
    }

    private void updateSubmitButton(Type type) {
        submit.setText(type.getLabel());
    }

    public enum Type {
        SUSPECT("Suspect"), ACCUSE("Accuse");

        private final String label;

        Type(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
