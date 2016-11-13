package me.belakede.thesis.client.boundary.javafx.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.client.configuration.UserConfiguration;
import me.belakede.thesis.internal.game.util.Cards;
import me.belakede.thesis.server.note.response.NotesResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class NoteDownloaderTask extends Task<ObservableList<Note>> {

    @Override
    protected ObservableList<Note> call() throws Exception {
        UserConfiguration configuration = UserConfiguration.getInstance();
        ObservableList<Note> notes = FXCollections.observableArrayList();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(configuration.getBaseUrl() + "/notes").path(configuration.getRoomId());
        NotesResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + configuration.getToken().getAccessToken())
                .get(NotesResponse.class);

        response.getNotes().stream()
                .map(nr -> new Note(Cards.valueOf(nr.getCard()).get(), nr.getOwner(), nr.getMarker()))
                .forEach(notes::add);

        return notes;
    }

}
