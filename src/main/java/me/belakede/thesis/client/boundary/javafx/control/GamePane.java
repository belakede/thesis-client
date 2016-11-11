package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.Game;
import me.belakede.thesis.game.equipment.BoardType;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.internal.game.util.GameBuilder;

import java.io.IOException;
import java.util.Collection;

public class GamePane extends BorderPane {

    private final ObjectProperty<Game> game = new SimpleObjectProperty<>();
    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final ListProperty<Card> cards = new SimpleListProperty<>();

    @FXML
    private SideBar sideBar;
    @FXML
    private BoardPane boardPane;

    public GamePane() throws IOException {
        this(GameBuilder.create().boardType(BoardType.DEFAULT).mystery().players(4).positions().build(), Suspect.MUSTARD, FXCollections.emptyObservableList());
    }

    public GamePane(Game game, Figurine figurine, Collection<Card> cards) {
        load();
        hookupChangeListeners();
        setFigurine(figurine);
        setCards(FXCollections.observableArrayList(cards));
        setGame(game);
        transformBoard();
    }

    public Game getGame() {
        return game.get();
    }

    public void setGame(Game game) {
        this.game.set(game);
    }

    public ObjectProperty<Game> gameProperty() {
        return game;
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

    private void hookupChangeListeners() {
        sideBar.figurineProperty().bind(figurineProperty());
        cardsProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                // TODO create card stack
            }
        });
        gameProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                boardPane.setBoardType(newValue.getBoard().getBoardType());
                boardPane.setFigurines(FXCollections.observableMap(newValue.getPositions()));
                sideBar.bindSize(boardPane.widthProperty());
            }
        });
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void transformBoard() {
        boardPane.getTransforms().addAll(sideBar.getRotates());
    }

}
