package me.belakede.thesis.client.boundary.javafx.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.jackson.JacksonContextResolver;
import me.belakede.thesis.server.game.response.GamesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class DownloadGamesTask extends Task<ObservableList<GameSummary>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadGamesTask.class);

    private final UserService userService;

    public DownloadGamesTask(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected ObservableList<GameSummary> call() throws Exception {
        ObservableList<GameSummary> games = FXCollections.observableArrayList();

        Client client = ClientBuilder.newBuilder().register(JacksonContextResolver.class).build();
        WebTarget webTarget = client.target(userService.getUrl("/games"));
        LOGGER.debug("WebTarget: {}", webTarget);
        List<GamesResponse> response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .get(new GenericType<List<GamesResponse>>() {
                });
        LOGGER.info("Game list downloaded: {}", response);
        response.stream().map(this::transform).forEach(games::add);

        return games;
    }

    private GameSummary transform(GamesResponse game) {
        LOGGER.info("game: #{}, roomID: {}", game.getId(), game.getRoomId());
        return new GameSummary(game.getId(), game.getRoomId(), game.getTime(), game.getBoardType(), game.getStatus(), FXCollections.observableMap(game.getUsers()));
    }

}
