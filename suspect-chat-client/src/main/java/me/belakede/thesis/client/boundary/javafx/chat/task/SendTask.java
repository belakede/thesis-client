package me.belakede.thesis.client.boundary.javafx.chat.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.chat.model.Message;

public class SendTask extends Task<Message> {

    private final String message;

    public SendTask(String message) {
        this.message = message;
    }

    @Override
    protected Message call() throws Exception {
        return null;
    }
}
