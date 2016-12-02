package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import me.belakede.thesis.client.boundary.javafx.converter.RoomStringConverter;
import me.belakede.thesis.client.boundary.javafx.converter.SuspectStringConverter;
import me.belakede.thesis.client.boundary.javafx.converter.WeaponStringConverter;
import me.belakede.thesis.client.boundary.javafx.task.AccuseTask;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class AccusePaneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccusePaneController.class);
    private final UserService userService;
    private ResourceBundle resourceBundle;

    @FXML
    private ChoiceBox<Suspect> suspect;
    @FXML
    private ChoiceBox<Room> room;
    @FXML
    private ChoiceBox<Weapon> weapon;
    @FXML
    private Button submit;

    @Autowired
    public AccusePaneController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        fillChoiceBoxes();
        setupActionListeners();
    }

    private void fillChoiceBoxes() {
        suspect.setConverter(new SuspectStringConverter(getResourceBundle()));
        room.setConverter(new RoomStringConverter(getResourceBundle()));
        weapon.setConverter(new WeaponStringConverter(getResourceBundle()));
        suspect.setItems(FXCollections.observableArrayList(Suspect.values()));
        room.setItems(FXCollections.observableArrayList(Room.values()));
        weapon.setItems(FXCollections.observableArrayList(Weapon.values()));
    }

    private void setupActionListeners() {
        submit.setOnAction(event -> {
            LOGGER.info("I suggest it was {}, in the {}, with the {}.", suspect.getValue(), room.getValue(), weapon.getValue());
            Task<Void> task = new AccuseTask(userService, suspect.getValue(), room.getValue(), weapon.getValue());
            task.setOnSucceeded(e -> {
                suspect.getSelectionModel().clearSelection();
                room.getSelectionModel().clearSelection();
                weapon.getSelectionModel().clearSelection();
            });
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
