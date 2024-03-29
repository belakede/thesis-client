package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import me.belakede.thesis.client.boundary.javafx.service.DownloadPlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class PlayersPaneController implements Initializable {

    private final DownloadPlayersService service;
    private final ListProperty<String> players = new SimpleListProperty<>();

    @FXML
    private ListView<String> playersView;
    @FXML
    private Button refreshButton;

    @Autowired
    public PlayersPaneController(DownloadPlayersService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupActionEvents();
        hookupChangeListeners();
        downloadPlayers();
    }

    public ListProperty<String> playersProperty() {
        return players;
    }

    private void setupActionEvents() {
        refreshButton.setOnAction(event -> service.restart());
    }

    private void hookupChangeListeners() {
        playersView.itemsProperty().bind(players);
    }

    private void downloadPlayers() {
        service.setOnSucceeded(event -> players.setValue(service.getValue()));
        if (players.isEmpty()) {
            service.start();
        }
    }

}
