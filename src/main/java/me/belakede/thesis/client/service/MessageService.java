package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import me.belakede.thesis.client.boundary.javafx.model.UserMessage;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final ListProperty<UserMessage> messages = new SimpleListProperty<>();

    public ObservableList<UserMessage> getMessages() {
        return messages.get();
    }

    public void setMessages(ObservableList<UserMessage> messages) {
        this.messages.set(messages);
    }

    public ListProperty<UserMessage> messagesProperty() {
        return messages;
    }
}
