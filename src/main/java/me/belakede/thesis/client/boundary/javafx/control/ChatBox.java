package me.belakede.thesis.client.boundary.javafx.control;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.model.UserMessage;
import me.belakede.thesis.client.boundary.javafx.task.MessageReceiverTask;
import me.belakede.thesis.client.boundary.javafx.task.MessageSenderTask;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatBox extends BorderPane {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatBox.class);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox messageContainer;
    @FXML
    private TextArea textArea;
    @FXML
    private Button sendButton;

    public ChatBox() {
        load();
        setupActionEvents();
        hookupChangeListeners();
        openCommunicationChannel();
        fixElementsWidth();
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setupActionEvents() {
        sendButton.setOnAction(event -> sendMessage());
    }

    private void hookupChangeListeners() {
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

    private void openCommunicationChannel() {
        MessageReceiverTask messageReceiverTask = new MessageReceiverTask();
        messageReceiverTask.getUserMessages().addListener((ListChangeListener.Change<? extends UserMessage> c) -> {
            LOGGER.info("UserMessage(s) arrived: {}", c);
            while (c.next()) {
                if (c.wasAdded()) {
                    Platform.runLater(() -> c.getAddedSubList().forEach(m -> appendChatMessage(m)));
                }
            }
        });
        Thread thread = new Thread(messageReceiverTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void appendChatMessage(UserMessage m) {
        ChatMessage chatMessage = new ChatMessage(m.getMessage(), m.getSender());
        chatMessage.getStyleClass().add(m.getFigurine().name().toLowerCase());
        messageContainer.getChildren().add(chatMessage);
    }

    private void sendMessage() {
        String text = textArea.getText().trim();
        Task task = new MessageSenderTask(text);
        task.setOnSucceeded(event -> textArea.setText(""));
        Thread thread = new Thread(task);
        thread.start();
    }
}
