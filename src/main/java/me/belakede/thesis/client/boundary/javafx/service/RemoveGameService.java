package me.belakede.thesis.client.boundary.javafx.service;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.task.RemoveGameTask;
import me.belakede.thesis.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoveGameService extends javafx.concurrent.Service<Void> {

    private final LongProperty id = new SimpleLongProperty();
    private final UserService userService;

    @Autowired
    public RemoveGameService(UserService userService) {
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
        return new RemoveGameTask(userService, getId());
    }
}
