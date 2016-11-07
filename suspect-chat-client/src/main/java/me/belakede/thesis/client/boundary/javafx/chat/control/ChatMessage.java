package me.belakede.thesis.client.boundary.javafx.chat.control;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.chat.model.Message;

import java.io.IOException;

public class ChatMessage extends VBox {

    private final ObjectProperty<Message> messageProperty = new SimpleObjectProperty<>();
    @FXML
    private Label details;
    @FXML
    private Label content;

    public ChatMessage(Message message) {
        setMessage(message);
        loadFxml();
        hookupChangeListeners();
    }

    private void setMessage(Message message) {
        messageProperty.setValue(message);
    }

    private void loadFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat-message.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void hookupChangeListeners() {
        details.textProperty().bind(Bindings.concat(messageProperty.getValue().getSender(), " @ ", messageProperty.getValue().getTime().toLocalTime()));
        content.textProperty().bind(messageProperty.getValue().contentProperty());
    }

}
