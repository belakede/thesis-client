package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import me.belakede.thesis.client.boundary.javafx.service.NoteWriterService;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope("prototype")
public class MarkerPaneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerPaneController.class);

    private final ObjectProperty<String> owner = new SimpleObjectProperty<>();
    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    private final ObjectProperty<Marker> marker = new SimpleObjectProperty<>();
    private final ObjectProperty<Object> icon = new SimpleObjectProperty<>();
    private final NoteWriterService noteWriterService;

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

    @Autowired
    public MarkerPaneController(NoteWriterService noteWriterService) {
        this.noteWriterService = noteWriterService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupNoteWriterService();
        setupActionEvents();
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

    public Object getIcon() {
        return icon.get();
    }

    public void setIcon(Object icon) {
        this.icon.set(icon);
    }

    public ObjectProperty<Object> iconProperty() {
        return icon;
    }

    private void setupNoteWriterService() {
        iconProperty().bind(noteWriterService.iconProperty());
        noteWriterService.cardProperty().bind(cardProperty());
        noteWriterService.ownerProperty().bind(ownerProperty());
        noteWriterService.markerProperty().bind(markerProperty());
    }

    private void setupActionEvents() {
        yesButton.setOnAction(event -> setMarker(Marker.YES));
        notButton.setOnAction(event -> setMarker(Marker.NOT));
        questionButton.setOnAction(event -> setMarker(Marker.QUESTION));
        maybeYesButton.setOnAction(event -> setMarker(Marker.MAYBE));
        maybeNotButton.setOnAction(event -> setMarker(Marker.MAYBE_NOT));
    }

}
