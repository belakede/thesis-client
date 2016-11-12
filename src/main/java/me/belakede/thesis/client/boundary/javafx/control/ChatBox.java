package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class ChatBox extends BorderPane {

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
        fixElementsWidth();
    }

    private void setupActionEvents() {
        sendButton.setOnAction(event -> sendMessage());
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void hookupChangeListeners() {
        textArea.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String text = textArea.getText().trim();
        messageContainer.getChildren().add(new ChatMessage(text, "admin"));
        textArea.setText("");
    }

    private void fixElementsWidth() {
        scrollPane.viewportBoundsProperty().addListener((ov, oldBounds, bounds) -> {
            messageContainer.setPrefWidth(bounds.getWidth());
            messageContainer.setPrefHeight(bounds.getHeight());
        });
    }
}
