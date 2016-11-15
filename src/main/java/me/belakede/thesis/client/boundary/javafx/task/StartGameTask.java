package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.configuration.UserConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class StartGameTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartGameTask.class);

    private final long id;

    public StartGameTask(long id) {
        this.id = id;
    }

    @Override
    protected Void call() throws Exception {
        UserConfiguration configuration = UserConfiguration.getInstance();
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(configuration.getBaseUrl() + "/games/start").path(String.valueOf(id));
        LOGGER.debug("WebTarget: {}", webTarget);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + configuration.getToken().getAccessToken())
                .post(Entity.json(null));
        if (response.getStatus() != 200) {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.warn("{}", response.toString());
            throw new RuntimeException("Starting game failed! Another game is already running!");
        } else {
            LOGGER.info("Game has been successful started!");
        }
        return null;
    }
}
