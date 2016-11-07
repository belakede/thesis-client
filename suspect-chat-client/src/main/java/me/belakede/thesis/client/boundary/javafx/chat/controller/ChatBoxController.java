package me.belakede.thesis.client.boundary.javafx.chat.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.chat.control.ChatMessage;
import me.belakede.thesis.client.boundary.javafx.chat.task.SendTask;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {

    @FXML
    private VBox messageContainer;
    @FXML
    private TextField messageField;

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void send(ActionEvent actionEvent) {
        String message = messageField.textProperty().getValue();
        if (!message.isEmpty()) {
            SendTask sendTask = new SendTask(message);
            sendTask.setOnSucceeded(event -> {
                messageContainer.getChildren().add(new ChatMessage(sendTask.getValue()));
                messageField.clear();
            });
            new Thread(sendTask).start();
        }
    }
}
