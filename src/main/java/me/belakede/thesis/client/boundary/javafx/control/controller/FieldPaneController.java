package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.control.FigurinePane;
import me.belakede.thesis.client.boundary.javafx.task.MoveTask;
import me.belakede.thesis.client.service.*;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.PairOfDice;
import me.belakede.thesis.game.field.Field;
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
    private final PositionService positionService;
    private final PlayerService playerService;
    private final BoardService boardService;
    private final UserService userService;
    private final RollService rollService;

    @FXML
    private StackPane parent;
    @FXML
    private FigurinePane figurinePane;

    @Autowired
    public FieldPaneController(PositionService positionService, PlayerService playerService, BoardService boardService, UserService userService, RollService rollService) {
        this.positionService = positionService;
        this.playerService = playerService;
        this.boardService = boardService;
        this.userService = userService;
        this.rollService = rollService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        setupActionEvents();
    }

    private void setupActionEvents() {
        parent.setOnMouseClicked(event -> {
            if (!parent.isDisabled()) {
                MoveTask task = new MoveTask(userService, getField().getRow(), getField().getColumn());
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            }
        });
    }


    private void hookupChangeListeners() {
        fieldProperty().addListener((observable, oldValue, newValue) -> {
            Figurine figurine = positionService.getInversePositions().get(newValue);
            if (figurine != null) {
                figurinePane.setFigurine(figurine);
                figurinePane.setVisible(true);
            }
        });
        positionService.positionsProperty().addListener((MapChangeListener.Change<? extends Figurine, ? extends Field> change) -> {
            if (change.wasAdded() && change.wasRemoved()) {
                if (getField().equals(change.getValueRemoved())) {
                    figurinePane.setVisible(false);
                } else if (getField().equals(change.getValueAdded())) {
                    figurinePane.setFigurine(change.getKey());
                    figurinePane.setVisible(true);
                }
                parent.getStyleClass().remove("available");
                parent.setDisable(true);
            }
        });
        rollService.rollsProperty().addListener((Change<? extends PairOfDice> change) -> {
            if (playerService.isCurrent()) {
                while (change.next()) {
                    change.getAddedSubList().forEach(pairOfDice -> {
                        if (boardService.isAvailable(playerService.getField(), getField(), pairOfDice.getResult())) {
                            parent.getStyleClass().add("available");
                            parent.setDisable(false);
                        } else {
                            parent.getStyleClass().remove("available");
                            parent.setDisable(true);
                        }
                    });
                }
            }
        });
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
        if (boardService.getBoard() == null) {
            boardService.boardProperty().addListener((observable, oldValue, newValue) -> {
                setField(newValue.getField(row, column));
            });
        } else {
            setField(boardService.getField(row, column));
        }
    }
}
