package me.belakede.thesis.client.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import me.belakede.thesis.game.board.Board;
import me.belakede.thesis.game.board.RoomField;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.field.Field;
import me.belakede.thesis.internal.game.util.Boards;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class BoardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);

    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();
    private final ObjectProperty<Board> board = new SimpleObjectProperty<>();
    private final NotificationService notificationService;

    @Autowired
    public BoardService(NotificationService notificationService) {
        this.notificationService = notificationService;
        hookupChangeListeners();
    }

    public boolean isAvailable(Field from, Field to, int maxSteps) {
        boolean result = false;
        if (getBoard() != null) {
            result = getBoard().availableFields(from, maxSteps).contains(to);
        }
        return result;
    }

    public Field getField(int row, int column) {
        return getBoard().getField(row, column);
    }

    public Optional<RoomField> getRoomField(Field field) {
        return getBoard().getRoomFields().stream().filter(rf -> rf.getFields().contains(field)).findFirst();
    }

    private void hookupChangeListeners() {
        boardTypeProperty().addListener((observable, oldValue, newValue) -> {
            try {
                setBoard(Boards.getBoardByType(newValue));
            } catch (IOException ex) {
                LOGGER.error("Can't load the {} board. Maybe it's not exists!", newValue, ex);
            }
        });
        notificationService.gameStatusNotificationProperty().addListener((observable, oldValue, newValue) -> {
            setBoardType(newValue.getBoardStatus().getBoardType());
        });
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
}