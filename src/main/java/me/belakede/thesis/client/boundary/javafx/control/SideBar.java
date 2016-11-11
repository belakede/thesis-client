package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Figurine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SideBar extends VBox {

    private static final Logger LOGGER = LoggerFactory.getLogger(SideBar.class);

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final ListProperty<Card> cards = new SimpleListProperty<>();

    @FXML
    private ImageView imageView;
    @FXML
    private RotateConfigurationPane rotateConfiguration;

    public SideBar() {
        load();
        hookupChangeListeners();
    }

    public SideBar(Figurine figurine, ObservableList<Card> cards) {
        load();
        hookupChangeListeners();
        setFigurine(figurine);
        setCards(cards);
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

    public ObservableList<Card> getCards() {
        return cards.get();
    }

    public void setCards(ObservableList<Card> cards) {
        this.cards.set(cards);
    }

    public ListProperty<Card> cardsProperty() {
        return cards;
    }

    public void bindSize(ReadOnlyDoubleProperty value) {
        rotateConfiguration.sizeProperty().unbind();
        rotateConfiguration.sizeProperty().bind(value);
    }

    public ObservableList<Rotate> getRotates() {
        return rotateConfiguration.getRotates();
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void hookupChangeListeners() {
        figurineProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Figurine changed from {} to {}", oldValue, newValue);
            if (newValue != null) {
                if (oldValue != null) {
                    imageView.getStyleClass().remove(oldValue.name().toLowerCase());
                }
                imageView.getStyleClass().add(newValue.name().toLowerCase());
            }
        });
    }
}
