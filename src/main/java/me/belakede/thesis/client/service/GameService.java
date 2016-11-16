package me.belakede.thesis.client.service;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameService {

    private final LongProperty gameId = new SimpleLongProperty();
    private final StringProperty roomId = new SimpleStringProperty();
    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();
    private final MapProperty<String, Integer> playersOrder = new SimpleMapProperty<>();

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

    public Figurine getFigurine() {
        return figurine.get();
    }

    public void setFigurine(Figurine figurine) {
        this.figurine.set(figurine);
    }

    public ObjectProperty<Figurine> figurineProperty() {
        return figurine;
    }

    public ObservableMap<Suspect, String> getPlayers() {
        return players.get();
    }

    public void setPlayers(ObservableMap<Suspect, String> players) {
        this.players.set(players);
        refreshPlayersOrder();
    }

    public MapProperty<Suspect, String> playersProperty() {
        return players;
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

    private void refreshPlayersOrder() {
        setPlayersOrder(FXCollections.observableHashMap());
        int columnIndex = 1;
        for (Map.Entry<Suspect, String> entry : players.entrySet()) {
            playersOrder.put(entry.getValue(), columnIndex);
            columnIndex++;
        }
    }
}
