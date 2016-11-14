package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.task.DownloadPlayersTask;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class PlayersPane extends VBox {

    private final ListProperty<String> players = new SimpleListProperty<>();

    @FXML
    private VBox parent;
    @FXML
    private ListView<String> playersView;
    @FXML
    private Button refreshButton;

    public PlayersPane() {
        load(this);
        setupActionEvents();
        hookupChangeListeners();
        downloadPlayers();
    }

    private void setupActionEvents() {
        refreshButton.setOnAction(event -> downloadPlayers());
    }

    private void hookupChangeListeners() {
        playersView.itemsProperty().bind(players);
    }

    private void downloadPlayers() {
        Task<ObservableList<String>> task = new DownloadPlayersTask();
        task.setOnSucceeded(event -> players.setValue(task.getValue()));
        Thread thread = new Thread(task);
        thread.start();
    }


}
