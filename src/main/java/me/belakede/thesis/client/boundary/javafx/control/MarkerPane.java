package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.HBox;
import me.belakede.thesis.client.boundary.javafx.control.controller.MarkerPaneController;
import me.belakede.thesis.client.service.SpringFxmlLoader;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Marker;

public class MarkerPane extends HBox {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();
    private final ObjectProperty<Object> icon = new SimpleObjectProperty<>();

    public MarkerPane(String owner, Card card, Marker marker) {
        MarkerPaneController controller = SPRING_FXML_LOADER.load(this);
        icon.bind(controller.iconProperty());
        controller.setCard(card);
        controller.setOwner(owner);
        controller.setMarker(marker);
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
}
