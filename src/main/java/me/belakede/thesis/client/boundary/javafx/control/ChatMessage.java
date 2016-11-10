package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class ChatMessage extends VBox {

    private final StringProperty message = new SimpleStringProperty("");
    private final StringProperty sender = new SimpleStringProperty("");
    @FXML
    private Text messageText;
    @FXML
    private Text senderText;

    public ChatMessage() {
        load();
        hookupChangeListeners();
    }

    public ChatMessage(String message, String sender) {
        load();
        hookupChangeListeners();
        setMessage(message);
        setSender(sender);
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public StringProperty messageProperty() {
        return message;
    }

    public String getSender() {
        return sender.get();
    }

    public void setSender(String sender) {
        this.sender.set(sender);
    }

    public StringProperty senderProperty() {
        return sender;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void hookupChangeListeners() {
        messageText.textProperty().bind(message);
        senderText.textProperty().bind(sender);
    }
}
