package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.server.chat.request.ChatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class MessageSenderTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSenderTask.class);

    private final UserService userService;
    private final GameService gameService;
    private final String message;

    public MessageSenderTask(UserService userService, GameService gameService, String message) {
        this.userService = userService;
        this.gameService = gameService;
        this.message = message;
    }

    @Override
    protected Void call() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(userService.getUrl("/chat/send"));
        LOGGER.debug("WebTarget: {}", webTarget);
        Response response = webTarget.request()
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .post(Entity.json(new ChatRequest(gameService.getRoomId(), message)));
        if (response.getStatus() == 200) {
            LOGGER.info("User was successfully created!");
        } else {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.info("Response: {}", response.toString());
            throw new RuntimeException("Authentication failed!");
        }
        return null;
    }
}
