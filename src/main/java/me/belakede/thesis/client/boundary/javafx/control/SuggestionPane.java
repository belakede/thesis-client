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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuggestionPane extends GridPane {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionPane.class);

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
        setupActionListeners();
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

    private void setupActionListeners() {
        submit.setOnAction(event -> {
            LOGGER.info("I suggest it was {}, in the {}, with the {}.", suspect.getValue(), room.getValue(), weapon.getValue());
            suspect.getSelectionModel().clearSelection();
            room.getSelectionModel().clearSelection();
            weapon.getSelectionModel().clearSelection();
        });
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
