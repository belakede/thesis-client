package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.configuration.UserConfiguration;
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

    private final String message;

    public MessageSenderTask(String message) {
        this.message = message;
    }

    @Override
    protected Void call() throws Exception {
        UserConfiguration configuration = UserConfiguration.getInstance();
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(configuration.getBaseUrl() + "/chat/send");
        LOGGER.debug("WebTarget: {}", webTarget);
        Response response = webTarget.request()
                .header("Authorization", "Bearer " + configuration.getToken().getAccessToken())
                .post(Entity.json(new ChatRequest(configuration.getRoomId(), message)));
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
