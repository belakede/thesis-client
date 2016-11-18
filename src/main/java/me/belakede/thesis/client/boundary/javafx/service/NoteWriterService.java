package me.belakede.thesis.client.boundary.javafx.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.task.NoteWriterTask;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.NoteService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class NoteWriterService extends javafx.concurrent.Service<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteWriterService.class);

    private final StringProperty owner = new SimpleStringProperty();
    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    private final ObjectProperty<Marker> marker = new SimpleObjectProperty<>();
    private final ObjectProperty<Object> icon = new SimpleObjectProperty<>();

    private final UserService userService;
    private final NoteService noteService;
    private final GameService gameService;

    @Autowired
    public NoteWriterService(UserService userService, NoteService noteService, GameService gameService) {
        this.userService = userService;
        this.noteService = noteService;
        this.gameService = gameService;
        setupService();
        hookupChaneListeners();
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

    public Object getIcon() {
        return icon.get();
    }

    public void setIcon(Object icon) {
        this.icon.set(icon);
    }

    public ObjectProperty<Object> iconProperty() {
        return icon;
    }

    @Override
    protected Task<Void> createTask() {
        return new NoteWriterTask(userService, noteService, gameService.getRoomId(), getCard(), getOwner(), getMarker());
    }

    private void setupService() {
        setOnSucceeded(event -> {
            LOGGER.trace("{}'s {} card has been marked with {}", getOwner(), getCard(), getMarker());
            setIcon(mapToIcon(getMarker()));
        });
    }

    private void hookupChaneListeners() {
        markerProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.trace("Marking {}'s {} card with {}", getOwner(), getCard(), getMarker());
            restart();
        });
    }

    private Object mapToIcon(Marker marker) {
        switch (marker) {
            case YES:
                return FontAwesome.Glyph.CHECK;
            case NOT:
                return FontAwesome.Glyph.TIMES;
            case QUESTION:
                return FontAwesome.Glyph.QUESTION;
            case MAYBE:
                return FontAwesome.Glyph.PLUS;
            case MAYBE_NOT:
                return FontAwesome.Glyph.MINUS;
            default:
                return " ";
        }
    }
}
