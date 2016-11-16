package me.belakede.thesis.client.boundary.javafx.service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.task.MessageSenderTask;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService extends javafx.concurrent.Service<Void> {

    private final StringProperty messageContent = new SimpleStringProperty();
    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public MessageSenderService(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public String getMessageContent() {
        return messageContent.get();
    }

    public void setMessageContent(String messageContent) {
        this.messageContent.set(messageContent);
    }

    public StringProperty messageContentProperty() {
        return messageContent;
    }

    @Override
    protected Task<Void> createTask() {
        return new MessageSenderTask(userService, gameService, getMessage());
    }
}
