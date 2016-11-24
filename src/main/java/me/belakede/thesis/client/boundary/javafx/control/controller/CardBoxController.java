package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Card;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

@Controller
@Scope("prototype")
public class CardBoxController implements Initializable {

    private final ObjectProperty<Card> card = new SimpleObjectProperty<>();
    private final UserService userService;
    private ResourceBundle resources;

    @FXML
    private AnchorPane parent;
    @FXML
    private Text label;
    @FXML
    private Text labelUpsideDown;

    public CardBoxController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResources(resources);
        setupBindings();
        hookupChangeListeners();
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

    public ResourceBundle getResources() {
        return resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }

    private void setupBindings() {
        labelUpsideDown.textProperty().bind(label.textProperty());
    }

    private void hookupChangeListeners() {
        cardProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                parent.getStyleClass().removeAll(Collections.singleton(createStyleClassFromCard(oldValue)));
            }
            parent.getStyleClass().add(createStyleClassFromCard(newValue));
            label.setText(getResources().getString(newValue.name()));
        });
    }

    private String createStyleClassFromCard(Card card) {
        return card.name().toLowerCase().replace("_", "-");
    }
}
