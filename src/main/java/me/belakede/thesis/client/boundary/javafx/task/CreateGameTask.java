package me.belakede.thesis.client.boundary.javafx.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.configuration.UserConfiguration;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.jackson.JacksonContextResolver;
import me.belakede.thesis.server.game.request.GamesRequest;
import me.belakede.thesis.server.game.response.GamesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class CreateGameTask extends Task<GameSummary> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateGameTask.class);

    private final BoardType boardType;
    private final ObservableList<String> players;

    public CreateGameTask(BoardType boardType, ObservableList<String> players) {
        this.boardType = boardType;
        this.players = players;
    }

    @Override
    protected GameSummary call() throws Exception {
        UserConfiguration configuration = UserConfiguration.getInstance();
        Client client = ClientBuilder.newBuilder().register(JacksonContextResolver.class).build();
        WebTarget webTarget = client.target(configuration.getBaseUrl() + "/games");
        LOGGER.debug("WebTarget: {}", webTarget);
        GamesResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + configuration.getToken().getAccessToken())
                .post(Entity.json(new GamesRequest(boardType, players)), GamesResponse.class);
        LOGGER.info("Game has been successful created!");
        return new GameSummary(response.getId(), response.getRoomId(), response.getTime(), response.getBoardType(), response.getStatus(), FXCollections.observableMap(response.getUsers()));
    }
}
