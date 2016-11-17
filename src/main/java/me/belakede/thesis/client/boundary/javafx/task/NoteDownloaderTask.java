package me.belakede.thesis.client.boundary.javafx.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.internal.game.util.Cards;
import me.belakede.thesis.server.note.response.NoteResponse;
import me.belakede.thesis.server.note.response.NotesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class NoteDownloaderTask extends Task<ObservableList<Note>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteDownloaderTask.class);

    private final UserService userService;
    private final GameService gameService;

    public NoteDownloaderTask(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    protected ObservableList<Note> call() throws Exception {
        ObservableList<Note> notes = FXCollections.observableArrayList();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(userService.getUrl("/notes")).path(gameService.getRoomId());
        NotesResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .get(NotesResponse.class);

        response.getNotes().stream()
                .map(this::createNote)
                .forEach(notes::add);

        return notes;
    }

    private Note createNote(NoteResponse nr) {
        Note note = new Note(Cards.valueOf(nr.getCard()).get(), nr.getOwner(), nr.getMarker());
        LOGGER.debug("Note downloaded: {}", note);
        return note;
    }

}
