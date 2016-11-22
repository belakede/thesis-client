package me.belakede.thesis.client.boundary.javafx.control;

import javafx.scene.layout.AnchorPane;
import me.belakede.thesis.client.boundary.javafx.control.controller.CardBoxController;
import me.belakede.thesis.client.service.SpringFxmlLoader;
import me.belakede.thesis.game.equipment.Card;

public class CardBox extends AnchorPane {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public CardBox(Card card) {
        CardBoxController controller = SPRING_FXML_LOADER.load(this);
        controller.setCard(card);
    }

}
