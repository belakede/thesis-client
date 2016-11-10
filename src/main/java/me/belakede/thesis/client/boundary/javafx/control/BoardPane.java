package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.layout.TilePane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.board.Board;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.field.Field;
import me.belakede.thesis.internal.game.util.Boards;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoardPane extends TilePane {

    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();
    private final ObjectProperty<Board> board = new SimpleObjectProperty<>();
    private final MapProperty<Figurine, Field> figurines = new SimpleMapProperty<>();

    public BoardPane() {
        load();
        hookupChangeListeners();
    }

    public BoardPane(BoardType boardType, ObservableMap<Figurine, Field> figurines) {
        load();
        hookupChangeListeners();
        setBoardType(boardType);
        setFigurines(figurines);
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

    public Board getBoard() {
        return board.get();
    }

    public void setBoard(Board board) {
        this.board.set(board);
    }

    public ObjectProperty<Board> boardProperty() {
        return board;
    }

    public ObservableMap<Figurine, Field> getFigurines() {
        return figurines.get();
    }

    public void setFigurines(ObservableMap<Figurine, Field> figurines) {
        this.figurines.set(figurines);
    }

    public MapProperty<Figurine, Field> figurinesProperty() {
        return figurines;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void hookupChangeListeners() {
        boardType.addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setBoardSafely(newValue);
                setFields(newValue);
                setDimension(newValue);
                setStyleClass(oldValue, newValue);
            }
        });
    }

    private void setBoardSafely(BoardType newValue) {
        try {
            setBoard(Boards.getBoardByType(newValue));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setFields(BoardType boardType) {
        IntStream.range(0, boardType.getSize())
                .mapToObj(i -> IntStream.range(0, getBoardType().getSize())
                        .mapToObj(j -> new FieldPane(getBoard().getField(i, j)))
                        .collect(Collectors.toList()))
                .forEach(fieldPanes -> getChildren().addAll(fieldPanes));
    }

    private void setDimension(BoardType newValue) {
        int newSize = newValue.getSize() * 25 + 2 * 10;
        setMinSize(newSize, newSize);
        setMaxSize(newSize, newSize);
        setPrefSize(newSize, newSize);
    }

    private void setStyleClass(BoardType oldValue, BoardType newValue) {
        if (oldValue != null) {
            getStyleClass().remove(oldValue.name().toLowerCase());
        }
        getStyleClass().add(newValue.name().toLowerCase());
    }


}
