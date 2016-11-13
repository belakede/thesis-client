package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.server.auth.request.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class RegistrationTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationTask.class);

    private final String server;
    private final String username;
    private final String password;

    public RegistrationTask(String server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Void call() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(server + "/register");
        LOGGER.debug("WebTarget: {}", webTarget);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new UserRequest(username, password)));
        if (response.getStatus() != 200) {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.warn("{}", response.toString());
            throw new RuntimeException("Authentication failed!");
        } else {
            LOGGER.info("User was successfully created!");
        }
        return null;
    }
}
