package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import me.belakede.thesis.client.boundary.javafx.converter.SuspectStringConverter;
import me.belakede.thesis.client.boundary.javafx.converter.WeaponStringConverter;
import me.belakede.thesis.client.boundary.javafx.task.SuspectTask;
import me.belakede.thesis.client.service.BoardService;
import me.belakede.thesis.client.service.PlayerService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.board.RoomField;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class SuspectPaneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuspectPaneController.class);
    private final UserService userService;
    private final BoardService boardService;
    private final PlayerService playerService;
    private ResourceBundle resourceBundle;

    @FXML
    private ChoiceBox<Suspect> suspect;
    @FXML
    private ChoiceBox<Weapon> weapon;
    @FXML
    private Button submit;

    @Autowired
    public SuspectPaneController(UserService userService, BoardService boardService, PlayerService playerService) {
        this.userService = userService;
        this.boardService = boardService;
        this.playerService = playerService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        fillChoiceBoxes();
        setupActionListeners();
    }

    private void fillChoiceBoxes() {
        suspect.setConverter(new SuspectStringConverter(getResourceBundle()));
        weapon.setConverter(new WeaponStringConverter(getResourceBundle()));
        suspect.setItems(FXCollections.observableArrayList(Suspect.values()));
        weapon.setItems(FXCollections.observableArrayList(Weapon.values()));
    }


    private void setupActionListeners() {
        submit.setOnAction(event -> {
            Optional<RoomField> roomField = boardService.getRoomField(playerService.getField());
            if (roomField.isPresent()) {
                Room room = roomField.get().getRoom();
                LOGGER.info("I suggest it was {}, in the {}, with the {}.", suspect.getValue(), room, weapon.getValue());
                Task<Void> task = new SuspectTask(userService, suspect.getValue(), room, weapon.getValue());
                task.setOnSucceeded(event1 -> {
                    suspect.getSelectionModel().clearSelection();
                    weapon.getSelectionModel().clearSelection();
                });
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            } else {
                LOGGER.warn("Your current field is {} which not a room field", playerService.getField());
            }
        });
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
