package me.belakede.thesis.client.boundary.javafx.task;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.service.UserService;
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

    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();
    private final ListProperty<String> players = new SimpleListProperty<>();
    private final UserService userService;

    public CreateGameTask(UserService userService) {
        this.userService = userService;
    }

    public BoardType getBoardType() {
        return boardType.get();
    }

    public void setBoardType(BoardType boardType) {
        this.boardType.set(boardType);
    }

    public ObjectProperty<BoardType> boardTypeProperty() {
        return boardType;
    }

    public ObservableList<String> getPlayers() {
        return players.get();
    }

    public void setPlayers(ObservableList<String> players) {
        this.players.set(players);
    }

    public ListProperty<String> playersProperty() {
        return players;
    }

    @Override
    protected GameSummary call() throws Exception {
        Client client = ClientBuilder.newBuilder().register(JacksonContextResolver.class).build();
        WebTarget webTarget = client.target(userService.getUrl("/games"));
        LOGGER.debug("WebTarget: {}", webTarget);
        GamesResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .post(Entity.json(new GamesRequest(getBoardType(), getPlayers())), GamesResponse.class);
        LOGGER.info("Game has been successful created!");
        return new GameSummary(response.getId(), response.getRoomId(), response.getTime(), response.getBoardType(), response.getStatus(), FXCollections.observableMap(response.getUsers()));
    }
}
