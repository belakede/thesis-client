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
    private CardPane cardPane;
    private PopOver cardPopOver;
    private PopOver suspectPopOver;

    public ActionPane() {
        load();
        setupCardPane();
        setupCardPopOver();
        setupSuspectPopOver();
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

    private void setupActionEvents() {
        suspect.setOnAction(event -> suspectPopOver.show(suspect));
        show.setOnAction(event -> {
            if (cardPopOver.isShowing()) {
                cardPopOver.hide();
            } else {
                cardPopOver.show(show);
            }
        });
    }

    private void setupPopOver(PopOver popOver) {
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        popOver.setDetachable(false);
        popOver.setDetached(false);
    }

}
