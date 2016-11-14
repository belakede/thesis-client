package me.belakede.thesis.client.boundary.javafx.model;

import javafx.beans.property.*;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Suspect;

import java.time.LocalDateTime;

public class GameSummary {

    private final LongProperty id = new SimpleLongProperty();
    private final ObjectProperty<LocalDateTime> created = new SimpleObjectProperty<>();
    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();
    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();

    public GameSummary() {
    }

    public GameSummary(Long id, LocalDateTime created, BoardType boardType, ObservableMap<Suspect, String> players) {
        setId(id);
        setCreated(created);
        setBoardType(boardType);
        setPlayers(players);
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public LongProperty idProperty() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created.get();
    }

    public void setCreated(LocalDateTime created) {
        this.created.set(created);
    }

    public ObjectProperty<LocalDateTime> createdProperty() {
        return created;
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

    public ObservableMap<Suspect, String> getPlayers() {
        return players.get();
    }

    public void setPlayers(ObservableMap<Suspect, String> players) {
        this.players.set(players);
    }

    public MapProperty<Suspect, String> playersProperty() {
        return players;
    }

    @Override
    public String toString() {
        return "#" + getId() + ", " + getBoardType();
    }
}
