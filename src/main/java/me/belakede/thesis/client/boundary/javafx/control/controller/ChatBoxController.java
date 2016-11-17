package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.control.ChatMessage;
import me.belakede.thesis.client.boundary.javafx.model.UserMessage;
import me.belakede.thesis.client.boundary.javafx.service.MessageSenderService;
import me.belakede.thesis.client.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ChatBoxController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatBoxController.class);

    private final MessageService messageService;
    private final MessageSenderService messageSenderService;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox messageContainer;
    @FXML
    private TextArea textArea;
    @FXML
    private Button sendButton;

    @Autowired
    public ChatBoxController(MessageService messageService, MessageSenderService messageSenderService) {
        this.messageService = messageService;
        this.messageSenderService = messageSenderService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMessageSenderService();
        setupActionEvents();
        hookupChangeListeners();
        fixElementsWidth();
    }

    private void setupMessageSenderService() {
        messageSenderService.setOnSucceeded(event -> textArea.setText(""));
    }

    private void setupActionEvents() {
        sendButton.setOnAction(event -> sendMessage());
    }

    private void hookupChangeListeners() {
        messageService.messagesProperty().addListener((ListChangeListener.Change<? extends UserMessage> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    LOGGER.info("Arrived messages: {}", change.getAddedSubList());
                    Platform.runLater(() -> change.getAddedSubList().forEach(this::appendChatMessage));
                }
            }
        });
        textArea.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
    }

    private void fixElementsWidth() {
        scrollPane.viewportBoundsProperty().addListener((ov, oldBounds, bounds) -> {
            messageContainer.setPrefWidth(bounds.getWidth());
            messageContainer.setPrefHeight(bounds.getHeight());
        });
    }

    private void appendChatMessage(UserMessage m) {
        LOGGER.info("{} sent the following message {}", m.getSender(), m.getMessage());
        ChatMessage chatMessage = new ChatMessage(m.getMessage(), m.getSender());
        chatMessage.getStyleClass().add(m.getFigurine().name().toLowerCase());
        messageContainer.getChildren().add(chatMessage);
    }

    private void sendMessage() {
        String text = textArea.getText().trim();
        messageSenderService.setMessageContent(text);
        LOGGER.info("Sending message: {}", messageSenderService.getMessageContent());
        messageSenderService.reset();
        messageSenderService.start();
    }
}
