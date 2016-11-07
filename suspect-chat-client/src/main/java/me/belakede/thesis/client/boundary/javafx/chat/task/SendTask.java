package me.belakede.thesis.client.boundary.javafx.chat.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.chat.model.Message;

import java.time.LocalDateTime;

public class SendTask extends Task<Message> {

    private final String message;

    public SendTask(String message) {
        this.message = message;
    }

    @Override
    protected Message call() throws Exception {
        String username = "admin";
        LocalDateTime now = LocalDateTime.now();
        return new Message(username, now, message);
    }
}
