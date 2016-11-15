package me.belakede.thesis.client.configuration;

import me.belakede.thesis.client.domain.Token;

public class UserConfiguration {

    private String username;
    private String baseUrl;
    private String roomId;
    private Token token;

    private UserConfiguration() {
    }

    public static UserConfiguration getInstance() {
        return UserConfigurationHolder.INSTANCE;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    private static final class UserConfigurationHolder {

        private static final UserConfiguration INSTANCE = new UserConfiguration();

    }
}
