package me.belakede.thesis.client.service;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.equipment.Suspect;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameService {

    private final LongProperty gameId = new SimpleLongProperty();
    private final StringProperty roomId = new SimpleStringProperty();
    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();
    private final MapProperty<String, Suspect> reversedPlayers = new SimpleMapProperty<>();
    private final MapProperty<String, Integer> playersOrder = new SimpleMapProperty<>();

    public GameService() {
        hookupChangeListeners();
    }

    public long getGameId() {
        return gameId.get();
    }

    public void setGameId(long gameId) {
        this.gameId.set(gameId);
    }

    public LongProperty gameIdProperty() {
        return gameId;
    }

    public String getRoomId() {
        return roomId.get();
    }

    public void setRoomId(String roomId) {
        this.roomId.set(roomId);
    }

    public StringProperty roomIdProperty() {
        return roomId;
    }

    public ObservableMap<Suspect, String> getPlayers() {
        return players.get();
    }

    public void setPlayers(ObservableMap<Suspect, String> players) {
        this.players.set(players);
    }

    public MapProperty<Suspect, String> playersProperty() {
        return players;
    }

    public ObservableMap<String, Suspect> getReversedPlayers() {
        return reversedPlayers.get();
    }

    public MapProperty<String, Suspect> reversedPlayersProperty() {
        return reversedPlayers;
    }

    public void setReversePlayers(ObservableMap<String, Suspect> reversedPlayers) {
        this.reversedPlayers.set(reversedPlayers);
    }

    public ObservableMap<String, Integer> getPlayersOrder() {
        return playersOrder.get();
    }

    public void setPlayersOrder(ObservableMap<String, Integer> playersOrder) {
        this.playersOrder.set(playersOrder);
    }

    public MapProperty<String, Integer> playersOrderProperty() {
        return playersOrder;
    }

    private void hookupChangeListeners() {
        playersProperty().addListener((observable, oldValue, newValue) -> {
            int columnIndex = 1;
            setPlayersOrder(FXCollections.observableHashMap());
            setReversePlayers(FXCollections.observableHashMap());
            for (Map.Entry<Suspect, String> entry : newValue.entrySet()) {
                getReversedPlayers().put(entry.getValue(), entry.getKey());
                getPlayersOrder().put(entry.getValue(), columnIndex);
                columnIndex++;
            }
        });
    }

}
