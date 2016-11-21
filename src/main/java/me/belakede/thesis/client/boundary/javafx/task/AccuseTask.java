package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;
import me.belakede.thesis.server.game.request.SuspectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AccuseTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccuseTask.class);
    private final UserService userService;
    private final Suspect suspect;
    private final Room room;
    private final Weapon weapon;

    public AccuseTask(UserService userService, Suspect suspect, Room room, Weapon weapon) {
        this.userService = userService;
        this.suspect = suspect;
        this.room = room;
        this.weapon = weapon;
    }

    @Override
    protected Void call() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(userService.getUrl("/game/accuse"));
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .post(Entity.json(new SuspectRequest(suspect, room, weapon)));
        if (response.getStatus() != 200) {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.warn("{}", response.toString());
            throw new RuntimeException("Accusing failed. Maybe it's not your turn.");
        }
        return null;
    }
}
