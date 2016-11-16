package me.belakede.thesis.client.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.el.parser.Token;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty baseUrl = new SimpleStringProperty();
    private final StringProperty roomId = new SimpleStringProperty();
    private final ObjectProperty<Token> token = new SimpleObjectProperty<>();

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getBaseUrl() {
        return baseUrl.get();
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl.set(baseUrl);
    }

    public StringProperty baseUrlProperty() {
        return baseUrl;
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

    public Token getToken() {
        return token.get();
    }

    public void setToken(Token token) {
        this.token.set(token);
    }

    public ObjectProperty<Token> tokenProperty() {
        return token;
    }

    public String getUrl(String suffix) {
        return getBaseUrl().concat(suffix);
    }

}
