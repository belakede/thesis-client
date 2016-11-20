package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NextTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NextTask.class);
    private final UserService userService;

    public NextTask(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected Void call() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(userService.getUrl("/game/next"));
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .post(null);
        if (response.getStatus() != 200) {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.warn("{}", response.toString());
            throw new RuntimeException("Player changing failed. Maybe it's not your turn.");
        }
        return null;
    }
}
