package me.belakede.thesis.client.boundary.javafx.service;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.task.DownloadPlayersTask;
import me.belakede.thesis.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadPlayersService extends javafx.concurrent.Service<ObservableList<String>> {

    private final UserService userService;

    @Autowired
    public DownloadPlayersService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected Task<ObservableList<String>> createTask() {
        return new DownloadPlayersTask(userService);
    }
}
