package me.belakede.thesis.client.configuration;

import me.belakede.thesis.game.equipment.Suspect;

import java.util.Map;

public class GameConfiguration {

    private Long gameId;
    private String roomId;
    private Map<Suspect, String> players;

    private GameConfiguration() {
    }

    public static GameConfiguration getInstance() {
        return GameConfigurationHolder.INSTANCE;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Map<Suspect, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Suspect, String> players) {
        this.players = players;
    }

    private static final class GameConfigurationHolder {
        private final static GameConfiguration INSTANCE = new GameConfiguration();
    }

}
