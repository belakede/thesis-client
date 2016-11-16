package me.belakede.thesis.client.service;

import javafx.beans.property.*;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.equipment.Suspect;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final LongProperty gameId = new SimpleLongProperty();
    private final StringProperty roomId = new SimpleStringProperty();
    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();

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
}
