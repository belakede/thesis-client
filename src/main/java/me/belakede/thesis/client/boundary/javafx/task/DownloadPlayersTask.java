package me.belakede.thesis.client.boundary.javafx.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.configuration.UserConfiguration;
import me.belakede.thesis.server.auth.response.UsersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class DownloadPlayersTask extends Task<ObservableList<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadPlayersTask.class);

    @Override
    protected ObservableList<String> call() throws Exception {
        UserConfiguration configuration = UserConfiguration.getInstance();
        ObservableList<String> players = FXCollections.observableArrayList();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(configuration.getBaseUrl() + "/users");
        UsersResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + configuration.getToken().getAccessToken())
                .get(UsersResponse.class);
        LOGGER.info("User list downloaded: {}", response);
        response.getUsers().stream()
                .map(ur -> ur.getUsername())
                .forEach(players::add);

        return players;
    }
}
