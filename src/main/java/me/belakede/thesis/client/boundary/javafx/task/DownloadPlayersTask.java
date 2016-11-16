package me.belakede.thesis.client.boundary.javafx.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.server.auth.response.UserResponse;
import me.belakede.thesis.server.auth.response.UsersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Component
public class DownloadPlayersTask extends Task<ObservableList<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadPlayersTask.class);
    private final UserService userService;

    @Autowired
    public DownloadPlayersTask(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected ObservableList<String> call() throws Exception {
        ObservableList<String> players = FXCollections.observableArrayList();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(userService.getUrl("/users"));
        LOGGER.debug("WebTarget: {}", webTarget);
        UsersResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .get(UsersResponse.class);
        LOGGER.info("User list downloaded: {}", response);
        response.getUsers().stream()
                .map(UserResponse::getUsername)
                .forEach(players::add);
        return players;
    }
}
