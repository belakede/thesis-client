package me.belakede.thesis.client.boundary.javafx.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.task.NoteWriterTask;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteWriterService extends javafx.concurrent.Service<Void> {

    private final StringProperty owner = new SimpleStringProperty();
    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    private final ObjectProperty<Marker> marker = new SimpleObjectProperty<>();

    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public NoteWriterService(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public String getOwner() {
        return owner.get();
    }

    public void setOwner(String owner) {
        this.owner.set(owner);
    }

    public StringProperty ownerProperty() {
        return owner;
    }

    public Card getCard() {
        return card.get();
    }

    public void setCard(Card card) {
        this.card.set(card);
    }

    public ObjectProperty<Card> cardProperty() {
        return card;
    }

    public Marker getMarker() {
        return marker.get();
    }

    public void setMarker(Marker marker) {
        this.marker.set(marker);
    }

    public ObjectProperty<Marker> markerProperty() {
        return marker;
    }

    @Override
    protected Task<Void> createTask() {
        return new NoteWriterTask(userService, gameService.getRoomId(), getCard(), getOwner(), getMarker());
    }
}
