package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import me.belakede.thesis.client.boundary.javafx.control.SuggestionPane;
import me.belakede.thesis.client.boundary.javafx.model.SuggestionType;
import me.belakede.thesis.client.boundary.javafx.task.AccuseTask;
import me.belakede.thesis.client.boundary.javafx.task.SuspectTask;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static me.belakede.thesis.client.boundary.javafx.model.SuggestionType.ACCUSE;

@Controller
@Scope("prototype")
public class SuggestionPaneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionPane.class);
    private final ObjectProperty<SuggestionType> type = new SimpleObjectProperty<>();
    private final UserService userService;

    @FXML
    private ChoiceBox<Suspect> suspect;
    @FXML
    private ChoiceBox<Room> room;
    @FXML
    private ChoiceBox<Weapon> weapon;
    @FXML
    private Button submit;

    @Autowired
    public SuggestionPaneController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillChoiceBoxes();
        hookupChangeListeners();
        setupActionListeners();
    }

    public SuggestionType getType() {
        return type.get();
    }

    public void setType(SuggestionType type) {
        this.type.set(type);
    }

    public ObjectProperty<SuggestionType> typeProperty() {
        return type;
    }

    private void fillChoiceBoxes() {
        suspect.setItems(FXCollections.observableArrayList(Suspect.values()));
        room.setItems(FXCollections.observableArrayList(Room.values()));
        weapon.setItems(FXCollections.observableArrayList(Weapon.values()));
    }

    private void hookupChangeListeners() {
        typeProperty().addListener((observable, oldValue, newValue) -> {
            submit.setText(newValue.getLabel());
        });
    }

    private void setupActionListeners() {
        submit.setOnAction(event -> {
            LOGGER.info("I suggest it was {}, in the {}, with the {}.", suspect.getValue(), room.getValue(), weapon.getValue());
            Task<Void> task = createTask(suspect.getValue(), room.getValue(), weapon.getValue());
            task.setOnSucceeded(event1 -> {
                suspect.getSelectionModel().clearSelection();
                room.getSelectionModel().clearSelection();
                weapon.getSelectionModel().clearSelection();
            });
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
    }

    private Task<Void> createTask(Suspect suspect, Room room, Weapon weapon) {
        return ACCUSE.equals(getType())
                ? new AccuseTask(userService, suspect, room, weapon)
                : new SuspectTask(userService, suspect, room, weapon);
    }
}
