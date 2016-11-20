package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.ChatBox;
import me.belakede.thesis.client.service.PlayerService;
import me.belakede.thesis.game.equipment.Figurine;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ChatPaneController implements Initializable {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final PlayerService playerService;

    @FXML
    private StackPane parent;
    @FXML
    private Button chatButton;
    private PopOver popOver;

    @Autowired
    public ChatPaneController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPopover();
        hookupChangeListeners();
        setupBindings();
    }

    private void setupPopover() {
        popOver = new PopOver(new ChatBox());
        popOver.setAnimated(true);
        popOver.setTitle("Chat");
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
    }

    private void hookupChangeListeners() {
        figurine.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                chatButton.getStyleClass().remove(oldValue.name().toLowerCase());
            }
            chatButton.getStyleClass().add(newValue.name().toLowerCase());
        });
        chatButton.setOnAction(event -> popOver.show(parent));
    }

    private void setupBindings() {
        figurine.bind(playerService.figurineProperty());
    }
}
