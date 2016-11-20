package me.belakede.thesis.client.service;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableList;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.field.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);

    private final StringProperty username = new SimpleStringProperty();
    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final ObjectProperty<Field> field = new SimpleObjectProperty<>();
    private final ListProperty<Card> cards = new SimpleListProperty<>();
    private final BooleanProperty current = new SimpleBooleanProperty();
    private final NotificationService notificationService;
    private final PositionService positionService;
    private final UserService userService;

    @Autowired
    public PlayerService(NotificationService notificationService, PositionService positionService, UserService userService) {
        this.notificationService = notificationService;
        this.positionService = positionService;
        this.userService = userService;
        setCards(FXCollections.observableArrayList());
        setCurrent(false);
        setupBindings();
        hookupChangeListeners();
    }

    private void setupBindings() {
        usernameProperty().bind(userService.usernameProperty());
    }

    private void hookupChangeListeners() {
        positionService.getPositions().addListener((Change<? extends Figurine, ? extends Field> change) -> {
            if (getField() == null) {
                setField(positionService.getPositions().get(getFigurine()));
            } else {
                if (change.getKey().equals(getFigurine())) {
                    setField(change.getValueAdded());
                }
            }
        });
        notificationService.playerStatusNotificationProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Storing figurine: {}", newValue.getFigurine());
            setFigurine(newValue.getFigurine());
            LOGGER.info("Storing cards: {}", newValue.getCards());
            setCards(FXCollections.observableArrayList(newValue.getCardSet()));
        });
        notificationService.currentPlayerNotificationProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Current player is: {}", newValue.getCurrent());
            setCurrent(getUsername().equals(newValue.getCurrent()));
        });
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
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

    public Field getField() {
        return field.get();
    }

    public void setField(Field field) {
        this.field.set(field);
    }

    public ObjectProperty<Field> fieldProperty() {
        return field;
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

    public boolean isCurrent() {
        return current.get();
    }

    public void setCurrent(boolean current) {
        this.current.set(current);
    }

    public BooleanProperty currentProperty() {
        return current;
    }
}
