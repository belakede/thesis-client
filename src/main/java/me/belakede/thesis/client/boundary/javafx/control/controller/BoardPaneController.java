package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.TilePane;
import me.belakede.thesis.client.boundary.javafx.control.FieldPane;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.game.equipment.BoardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BoardPaneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardPaneController.class);
    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();

    private final GameService gameService;

    @FXML
    private TilePane parent;

    @Autowired
    public BoardPaneController(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        setupBindings();
    }

    private void hookupChangeListeners() {
        boardType.addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Board type has been changed from {} to {}", oldValue, newValue);
            if (null != newValue) {
                setFields();
                setDimension();
                setStyleClass(oldValue, newValue);
            }
        });
    }

    private void setupBindings() {
        boardType.bind(gameService.boardTypeProperty());
    }

    private void setFields() {
        int size = gameService.getBoardType().getSize();
        IntStream.range(0, size)
                .mapToObj(i -> IntStream.range(0, size)
                        .mapToObj(j -> new FieldPane(i, j))
                        .collect(Collectors.toList()))
                .forEach(fieldPanes -> parent.getChildren().addAll(fieldPanes));
    }

    private void setDimension() {
        int newSize = gameService.getBoardType().getSize() * 25;
        parent.setMinSize(newSize, newSize);
        parent.setMaxSize(newSize, newSize);
        parent.setPrefSize(newSize, newSize);
    }

    private void setStyleClass(BoardType oldValue, BoardType newValue) {
        if (oldValue != null) {
            parent.getStyleClass().remove(oldValue.name().toLowerCase());
        }
        parent.getStyleClass().add(newValue.name().toLowerCase());
    }

}
