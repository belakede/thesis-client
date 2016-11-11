package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Card;
import org.controlsfx.control.PopOver;

public class ActionPane extends VBox {

    @FXML
    private Button suspect;
    @FXML
    private Button show;
    @FXML
    private Button accuse;
    private CardPane cardPane;
    private PopOver cardPopOver;
    private PopOver suspectPopOver;
    private PopOver accusePopOver;

    public ActionPane() {
        load();
        setupCardPane();
        setupCardPopOver();
        setupSuspectPopOver();
        setupAccusePopOver();
        setupActionEvents();
    }

    public ListProperty<Card> cardsProperty() {
        return cardPane.cardsProperty();
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setupCardPane() {
        cardPane = new CardPane();
    }

    private void setupCardPopOver() {
        cardPopOver = new PopOver(cardPane);
        setupPopOver(cardPopOver);
    }

    private void setupSuspectPopOver() {
        suspectPopOver = new PopOver(new SuggestionPane(SuggestionPane.Type.SUSPECT));
        setupPopOver(suspectPopOver);
    }

    private void setupAccusePopOver() {
        accusePopOver = new PopOver(new SuggestionPane(SuggestionPane.Type.ACCUSE));
        setupPopOver(accusePopOver);
    }

    private void setupActionEvents() {
        suspect.setOnAction(event -> suspectPopOver.show(suspect));
        show.setOnAction(event -> cardPopOver.show(show));
        accuse.setOnAction(event -> accusePopOver.show(accuse));
    }

    private void setupPopOver(PopOver popOver) {
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        popOver.setDetachable(false);
        popOver.setDetached(false);
    }

}
