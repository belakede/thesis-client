package me.belakede.thesis.client.boundary.javafx.service;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.task.StartGameTask;
import me.belakede.thesis.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartGameService extends javafx.concurrent.Service<Void> {

    private final UserService userService;
    private final LongProperty id = new SimpleLongProperty();

    @Autowired
    public StartGameService(UserService userService) {
        this.userService = userService;
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public LongProperty idProperty() {
        return id;
    }

    @Override
    protected Task<Void> createTask() {
        return new StartGameTask(userService, getId());
    }
}
