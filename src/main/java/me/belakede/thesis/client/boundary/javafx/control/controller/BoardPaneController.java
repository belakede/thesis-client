package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.TilePane;
import me.belakede.thesis.client.boundary.javafx.control.FieldPane;
import me.belakede.thesis.client.service.BoardService;
import me.belakede.thesis.game.equipment.BoardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

@Controller
public class BoardPaneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardPaneController.class);
    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();

    private final BoardService boardService;

    @FXML
    private TilePane parent;

    @Autowired
    public BoardPaneController(BoardService boardService) {
        this.boardService = boardService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        setupBindings();
    }

    public BoardType getBoardType() {
        return boardType.get();
    }

    public void setBoardType(BoardType boardType) {
        this.boardType.set(boardType);
    }

    public ObjectProperty<BoardType> boardTypeProperty() {
        return boardType;
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
        boardType.bind(boardService.boardTypeProperty());
    }

    private void setFields() {
        int size = getBoardType().getSize();
        IntStream.range(0, size)
                .forEach(i -> IntStream.range(0, size)
                        .forEach(j -> parent.getChildren().add(new FieldPane(i, j))));
    }

    private void setDimension() {
        int newSize = getBoardType().getSize() * 25;
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
