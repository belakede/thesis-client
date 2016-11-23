package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.server.game.request.ShowRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ShowTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShowTask.class);
    private final UserService userService;
    private final Card card;

    public ShowTask(UserService userService, Card card) {
        this.userService = userService;
        this.card = card;
    }

    @Override
    protected Void call() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(userService.getUrl("/game/show"));
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .post(Entity.json(new ShowRequest(card == null ? null : card.name())));
        if (response.getStatus() != 200) {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.warn("{}", response.toString());
            throw new RuntimeException("Card showing failed. Maybe it's not your turn.");
        }
        return null;
    }
}
