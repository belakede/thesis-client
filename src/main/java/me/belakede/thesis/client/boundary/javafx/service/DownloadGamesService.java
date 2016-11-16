package me.belakede.thesis.client.boundary.javafx.service;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.boundary.javafx.task.DownloadGamesTask;
import me.belakede.thesis.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadGamesService extends javafx.concurrent.Service<ObservableList<GameSummary>> {

    private final UserService userService;

    @Autowired
    public DownloadGamesService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected Task<ObservableList<GameSummary>> createTask() {
        return new DownloadGamesTask(userService);
    }
}
