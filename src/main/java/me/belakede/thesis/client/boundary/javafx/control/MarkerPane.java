package me.belakede.thesis.client.boundary.javafx.control;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import me.belakede.thesis.client.boundary.javafx.task.NoteWriterTask;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class MarkerPane extends HBox {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerPane.class);

    private final ObjectProperty<String> owner = new SimpleObjectProperty<>();
    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    private final ObjectProperty<Marker> marker = new SimpleObjectProperty<>();
    private final ObjectProperty<Object> icon = new SimpleObjectProperty<>();

    @FXML
    private Button yesButton;
    @FXML
    private Button notButton;
    @FXML
    private Button questionButton;
    @FXML
    private Button maybeYesButton;
    @FXML
    private Button maybeNotButton;

    public MarkerPane(String owner, Card card) {
        this(owner, card, Marker.NONE);
    }

    public MarkerPane(String owner, Card card, Marker marker) {
        load(this);
        setupActionEvents();
        hookupChangeListeners();
        setOwner(owner);
        setCard(card);
        setMarker(marker);
    }

    public String getOwner() {
        return owner.get();
    }

    public void setOwner(String owner) {
        this.owner.set(owner);
    }

    public ObjectProperty<String> ownerProperty() {
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

    public ObjectProperty<Object> iconProperty() {
        return icon;
    }

    private void setIcon(Object icon) {
        this.icon.set(icon);
    }

    private void setupActionEvents() {
        yesButton.setOnAction(event -> setMarker(Marker.YES));
        notButton.setOnAction(event -> setMarker(Marker.NOT));
        questionButton.setOnAction(event -> setMarker(Marker.QUESTION));
        maybeYesButton.setOnAction(event -> setMarker(Marker.MAYBE));
        maybeNotButton.setOnAction(event -> setMarker(Marker.MAYBE_NOT));
    }

    private void hookupChangeListeners() {
        marker.addListener((observable, oldValue, newValue) -> {
            Task task = new NoteWriterTask(getCard(), getOwner(), newValue);
            task.setOnSucceeded(event -> {
                LOGGER.info("Mark {}'s {} card with {}", getOwner(), getCard(), newValue);
                Platform.runLater(() -> setIcon(mapToIcon(newValue)));
            });
            new Thread(task).start();
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
