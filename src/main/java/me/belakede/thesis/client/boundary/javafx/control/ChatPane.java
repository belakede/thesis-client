package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import org.controlsfx.control.PopOver;

public class ChatPane extends StackPane {

    @FXML
    private Button chatButton;
    private PopOver popOver;

    public ChatPane() {
        load();
        setupPopover();
        hookupChangeListeners();
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
        chatButton.setOnAction(event -> {
            if (popOver.isShowing()) {
                popOver.hide();
            } else {
                popOver.show(this);
            }
        });
    }

}
