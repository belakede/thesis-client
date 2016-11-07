package me.belakede.thesis.client.boundary.javafx.chat.control;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.chat.model.Message;
import me.belakede.thesis.client.javafx.ControlLoader;

import java.time.format.DateTimeFormatter;

public class ChatMessage extends VBox {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("hh:mm");
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
        ControlLoader.load(this);
    }

    private void hookupChangeListeners() {
        details.textProperty().bind(Bindings.concat(messageProperty.getValue().getSender(), " @ ", FORMATTER.format(messageProperty.getValue().getTime().toLocalTime())));
        content.textProperty().bind(messageProperty.getValue().contentProperty());
    }

}
