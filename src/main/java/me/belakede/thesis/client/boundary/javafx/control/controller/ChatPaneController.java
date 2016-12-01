package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import me.belakede.thesis.client.boundary.javafx.control.ChatBox;
import me.belakede.thesis.client.boundary.javafx.model.UserMessage;
import me.belakede.thesis.client.service.MessageService;
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
    private final MessageService messageService;
    private ResourceBundle resources;

    @FXML
    private StackPane parent;
    @FXML
    private Button chatButton;
    @FXML
    private Circle notification;
    private PopOver popOver;

    @Autowired
    public ChatPaneController(PlayerService playerService, MessageService messageService) {
        this.playerService = playerService;
        this.messageService = messageService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResources(resources);
        setupPopover();
        hookupChangeListeners();
        setupBindings();
    }

    private void setupPopover() {
        popOver = new PopOver(new ChatBox());
        popOver.setAnimated(true);
        popOver.setTitle(getResources().getString("Chat"));
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
    }

    private void hookupChangeListeners() {
        figurine.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                chatButton.getStyleClass().remove(oldValue.name().toLowerCase());
                notification.getStyleClass().remove(oldValue.name().toLowerCase());
            }
            chatButton.getStyleClass().add(newValue.name().toLowerCase());
            notification.getStyleClass().add(newValue.name().toLowerCase());
        });
        messageService.messagesProperty().addListener((Change<? extends UserMessage> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    notification.setVisible(true);
                }
            }
        });
        chatButton.setOnAction(event -> {
            popOver.show(parent);
            notification.setVisible(false);
        });
    }

    private void setupBindings() {
        figurine.bind(playerService.figurineProperty());
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }
}
