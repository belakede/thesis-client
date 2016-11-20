package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.server.game.request.MoveRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MoveTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveTask.class);
    private final UserService userService;
    private final int row;
    private final int column;

    public MoveTask(UserService userService, int row, int column) {
        this.userService = userService;
        this.row = row;
        this.column = column;
    }

    @Override
    protected Void call() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(userService.getUrl("/game/move"));
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .post(Entity.json(new MoveRequest(row, column)));
        if (response.getStatus() != 200) {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.warn("{}", response.toString());
            throw new RuntimeException("Moving failed. Maybe it's not your turn.");
        }
        return null;
    }
}
