package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.NoteBox;
import me.belakede.thesis.client.boundary.javafx.service.DownloadNotesService;
import me.belakede.thesis.client.boundary.javafx.task.NoteRegistrationTask;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Figurine;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class NotePaneController implements Initializable {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();

    private final UserService userService;
    private final GameService gameService;
    private final DownloadNotesService downloadNotesService;

    @FXML
    private StackPane parent;
    @FXML
    private Button noteButton;
    private PopOver popOver;

    @Autowired
    public NotePaneController(UserService userService, GameService gameService, DownloadNotesService downloadNotesService) {
        this.userService = userService;
        this.gameService = gameService;
        this.downloadNotesService = downloadNotesService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPopover();
        hookupChangeListeners();
        registerNoteRoom();
    }

    private void hookupChangeListeners() {
        figurine.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                noteButton.getStyleClass().remove(oldValue.name().toLowerCase());
            }
            noteButton.getStyleClass().add(newValue.name().toLowerCase());
        });
        noteButton.setOnAction(event -> popOver.show(parent));
    }


    private void registerNoteRoom() {
        Task registrationTask = new NoteRegistrationTask(userService, gameService);
        registrationTask.setOnSucceeded(event -> downloadNotesService.start());
        new Thread(registrationTask).start();
    }

    private void setupPopover() {
        popOver = new PopOver(new NoteBox());
        popOver.setAnimated(true);
        popOver.setTitle("Notes");
        popOver.setDetached(false);
        popOver.setDetachable(false);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);
    }

}
