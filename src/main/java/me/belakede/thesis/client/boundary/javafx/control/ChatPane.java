package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Figurine;
import org.controlsfx.control.PopOver;

public class ChatPane extends StackPane {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    @FXML
    private Button chatButton;
    private PopOver popOver;

    public ChatPane() {
        load();
        setupPopover();
        hookupChangeListeners();
    }

    public ChatPane(Figurine figurine) {
        load();
        setupPopover();
        hookupChangeListeners();
        setFigurine(figurine);
    }

    public Figurine getFigurine() {
        return figurine.get();
    }

    public void setFigurine(Figurine figurine) {
        this.figurine.set(figurine);
    }

    public ObjectProperty<Figurine> figurineProperty() {
        return figurine;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void setupPopover() {
        popOver = new PopOver(new ChatBox());
        popOver.setAnimated(true);
        popOver.setTitle("Chat");
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
    }

    private void hookupChangeListeners() {
        figurine.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue != null) {
                    chatButton.getStyleClass().remove(oldValue.name().toLowerCase());
                }
                chatButton.getStyleClass().add(newValue.name().toLowerCase());
            }
        });
        chatButton.setOnAction(event -> {
            if (popOver.isShowing()) {
                popOver.hide();
            } else {
                popOver.show(this);
            }
        });
    }

}
