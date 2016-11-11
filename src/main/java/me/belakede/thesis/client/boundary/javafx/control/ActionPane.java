package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import org.controlsfx.control.PopOver;

public class ActionPane extends VBox {

    @FXML
    private Button show;
    private CardPane cardPane;
    private PopOver cardPopOver;

    public ActionPane() {
        load();
        setupCardPane();
        setupCardPopOver();
        setupActionEvents();
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setupCardPane() {
        cardPane = new CardPane();
    }

    private void setupCardPopOver() {
        cardPopOver = new PopOver(cardPane);
        cardPopOver.setAnimated(true);
        cardPopOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        cardPopOver.setDetachable(false);
        cardPopOver.setDetached(false);
    }

    private void setupActionEvents() {
        show.setOnAction(event -> {
            if (cardPopOver.isShowing()) {
                cardPopOver.hide();
            } else {
                cardPopOver.show(show);
            }
        });
    }

}
