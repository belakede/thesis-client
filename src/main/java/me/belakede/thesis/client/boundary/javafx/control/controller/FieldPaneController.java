package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.MapChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import me.belakede.thesis.client.boundary.javafx.control.FigurinePane;
import me.belakede.thesis.client.boundary.javafx.task.MoveTask;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.field.Field;
import me.belakede.thesis.server.game.response.PairOfDiceNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope("prototype")
public class FieldPaneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldPaneController.class);

    private final SimpleObjectProperty<Field> field = new SimpleObjectProperty<>();
    private final GameService gameService;
    private final UserService userService;
    private final NotificationService notificationService;

    @FXML
    private Pane content;

    @Autowired
    public FieldPaneController(GameService gameService, UserService userService, NotificationService notificationService) {
        this.gameService = gameService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        setupActionEvent();
    }

    public Field getField() {
        return field.get();
    }

    public void setField(Field field) {
        this.field.set(field);
    }

    public SimpleObjectProperty<Field> fieldProperty() {
        return field;
    }

    public void setField(int row, int column) {
        setField(gameService.getBoard().getField(row, column));
    }

    private void hookupChangeListeners() {
        fieldProperty().addListener((observable, oldValue, newValue) -> {
            if (gameService.getPositions().containsKey(newValue)) {
                content.getChildren().add(new FigurinePane(gameService.getPositions().get(newValue)));
            }
            content.getStyleClass().add(newValue.getFieldType().name().toLowerCase());
        });
        notificationService.pairOfDiceNotificationProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Pair of dice notification arrived: {}", newValue);
            if (gameService.isAvailableFromCurrentPosition(getField(), newValue.getFirst() + newValue.getSecond())) {
                content.setDisable(false);
            } else {
                content.setDisable(true);
            }
        });
        gameService.getPositions().addListener((Change<? extends Field, ? extends Figurine> change) -> {
            if (change.getKey().equals(getField())) {
                if (change.wasAdded()) {
                    content.getChildren().add(new FigurinePane(change.getValueAdded()));
                } else {
                    content.getChildren().clear();
                }
            }
        });
    }

    private void setupActionEvent() {
        content.setOnMouseClicked(event -> {
            if (!content.isDisabled()) {
                MoveTask task = new MoveTask(userService, getField().getRow(), getField().getColumn());
                task.setOnSucceeded(e -> notificationService.setPairOfDiceNotification(new PairOfDiceNotification(0, 0)));
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            }
        });

    }

}
