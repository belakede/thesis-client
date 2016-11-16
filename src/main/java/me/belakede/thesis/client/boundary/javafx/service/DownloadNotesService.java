package me.belakede.thesis.client.boundary.javafx.service;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.client.boundary.javafx.task.NoteDownloaderTask;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadNotesService extends javafx.concurrent.Service<ObservableList<Note>> {

    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public DownloadNotesService(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    protected Task<ObservableList<Note>> createTask() {
        return new NoteDownloaderTask(userService, gameService);
    }
}
