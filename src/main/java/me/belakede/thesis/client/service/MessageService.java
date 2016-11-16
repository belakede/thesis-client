package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.belakede.thesis.client.boundary.javafx.model.UserMessage;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final ListProperty<UserMessage> messages = new SimpleListProperty<>();

    public MessageService() {
        setMessages(FXCollections.observableArrayList());
    }

    public ObservableList<UserMessage> getMessages() {
        return messages.get();
    }

    public void setMessages(ObservableList<UserMessage> messages) {
        this.messages.set(messages);
    }

    public ListProperty<UserMessage> messagesProperty() {
        return messages;
    }

    public void add(UserMessage userMessage) {
        messagesProperty().add(userMessage);
    }
}
