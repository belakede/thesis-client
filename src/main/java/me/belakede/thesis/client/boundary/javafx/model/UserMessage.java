package me.belakede.thesis.client.boundary.javafx.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import me.belakede.thesis.game.equipment.Suspect;

import java.time.LocalDateTime;

public class UserMessage {

    private StringProperty sender = new SimpleStringProperty();
    private StringProperty message = new SimpleStringProperty();
    private SimpleObjectProperty<Suspect> figurine = new SimpleObjectProperty<>();
    private SimpleObjectProperty<LocalDateTime> time = new SimpleObjectProperty<>();

    public UserMessage(String sender, String message, LocalDateTime time, Suspect figurine) {
        setTime(time);
        setSender(sender);
        setMessage(message);
        setFigurine(figurine);
    }

    public String getSender() {
        return sender.get();
    }

    public void setSender(String sender) {
        this.sender.set(sender);
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public StringProperty messageProperty() {
        return message;
    }

    public Suspect getFigurine() {
        return figurine.get();
    }

    public void setFigurine(Suspect figurine) {
        this.figurine.set(figurine);
    }

    public SimpleObjectProperty<Suspect> figurineProperty() {
        return figurine;
    }

    public LocalDateTime getTime() {
        return time.get();
    }

    public void setTime(LocalDateTime time) {
        this.time.set(time);
    }

    public SimpleObjectProperty<LocalDateTime> timeProperty() {
        return time;
    }
}
