package me.belakede.thesis.client.service;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.board.Board;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.field.Field;
import me.belakede.thesis.internal.game.util.Boards;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    private final LongProperty gameId = new SimpleLongProperty();
    private final StringProperty roomId = new SimpleStringProperty();
    private final ListProperty<Card> cards = new SimpleListProperty<>();
    private final ObjectProperty<BoardType> boardType = new SimpleObjectProperty<>();
    private final ObjectProperty<Board> board = new SimpleObjectProperty<>();
    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final MapProperty<Suspect, String> players = new SimpleMapProperty<>();
    private final MapProperty<Figurine, Field> positions = new SimpleMapProperty<>();
    private final MapProperty<String, Integer> playersOrder = new SimpleMapProperty<>();

    public GameService() {
        hookupChangeListeners();
    }

    public long getGameId() {
        return gameId.get();
    }

    public void setGameId(long gameId) {
        this.gameId.set(gameId);
    }

    public LongProperty gameIdProperty() {
        return gameId;
    }

    public String getRoomId() {
        return roomId.get();
    }

    public void setRoomId(String roomId) {
        this.roomId.set(roomId);
    }

    public StringProperty roomIdProperty() {
        return roomId;
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

    public Figurine getFigurine() {
        return figurine.get();
    }

    public void setFigurine(Figurine figurine) {
        this.figurine.set(figurine);
    }

    public ObjectProperty<Figurine> figurineProperty() {
        return figurine;
    }

    public ObservableMap<Suspect, String> getPlayers() {
        return players.get();
    }

    public void setPlayers(ObservableMap<Suspect, String> players) {
        this.players.set(players);
    }

    public MapProperty<Suspect, String> playersProperty() {
        return players;
    }

    public ObservableMap<Figurine, Field> getPositions() {
        return positions.get();
    }

    public void setPositions(ObservableMap<Figurine, Field> positions) {
        this.positions.set(positions);
    }

    public MapProperty<Figurine, Field> positionsProperty() {
        return positions;
    }

    public ObservableMap<String, Integer> getPlayersOrder() {
        return playersOrder.get();
    }

    public void setPlayersOrder(ObservableMap<String, Integer> playersOrder) {
        this.playersOrder.set(playersOrder);
    }

    public MapProperty<String, Integer> playersOrderProperty() {
        return playersOrder;
    }

    private void hookupChangeListeners() {
        playersProperty().addListener((observable, oldValue, newValue) -> {
            int columnIndex = 1;
            setPlayersOrder(FXCollections.observableHashMap());
            for (Map.Entry<Suspect, String> entry : newValue.entrySet()) {
                getPlayersOrder().put(entry.getValue(), columnIndex);
                columnIndex++;
            }
        });
        boardTypeProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Board board = Boards.getBoardByType(newValue);
                setBoard(board);
            } catch (IOException e) {
                LOGGER.warn("{} board not found. ", newValue, e);
            }
        });
    }

}
