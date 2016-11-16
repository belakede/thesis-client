package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.Pane;
import me.belakede.thesis.client.boundary.javafx.control.controller.NoteFieldController;
import me.belakede.thesis.client.service.SpringFxmlLoader;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;

public class NoteField extends Pane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public NoteField(String owner, Card card, Marker marker) {
        NoteFieldController controller = SPRING_FXML_LOADER.load(this);
        controller.setMarkerPane(new MarkerPane(owner, card, marker));
    }

}
