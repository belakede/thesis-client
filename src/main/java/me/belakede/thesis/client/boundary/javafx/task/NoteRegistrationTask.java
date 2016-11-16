package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.server.note.request.NoteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
public class NoteRegistrationTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteRegistrationTask.class);

    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public NoteRegistrationTask(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    protected Void call() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(userService.getUrl("/notes/join"));
        LOGGER.info("WebTarget: {}", webTarget);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .post(Entity.json(new NoteRequest(gameService.getRoomId())));
        if (response.getStatus() != 200) {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.warn("{}", response.toString());
            throw new RuntimeException("Room registration failed!");
        } else {
            LOGGER.info("User was successfully registered!");
        }
        return null;
    }
}
