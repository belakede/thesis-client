package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.configuration.UserConfiguration;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;
import me.belakede.thesis.server.note.request.NoteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NoteWriterTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteWriterTask.class);

    private final Card card;
    private final String owner;
    private final Marker marker;

    public NoteWriterTask(Card card, String owner, Marker marker) {
        this.card = card;
        this.owner = owner;
        this.marker = marker;
    }

    @Override
    protected Void call() throws Exception {
        UserConfiguration configuration = UserConfiguration.getInstance();
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(configuration.getBaseUrl() + "/notes");
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + configuration.getToken().getAccessToken())
                .post(Entity.json(new NoteRequest(configuration.getRoomId(), card, owner, marker)));
        if (response.getStatus() != 200) {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.warn("{}", response.toString());
            throw new RuntimeException("Note registration failed!");
        } else {
            LOGGER.info("Note was successfully stored!");
        }
        return null;
    }
}
