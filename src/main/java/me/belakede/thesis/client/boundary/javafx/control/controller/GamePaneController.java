package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.control.BoardPane;
import me.belakede.thesis.client.boundary.javafx.control.SideBar;
import me.belakede.thesis.client.boundary.javafx.model.GameSummary;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Card;
import me.belakede.thesis.game.equipment.Figurine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class GamePaneController implements Initializable {

    private final ObjectProperty<GameSummary> game = new SimpleObjectProperty<>();
    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final ListProperty<Card> cards = new SimpleListProperty<>();
    private final UserService userService;
    private final GameService gameService;
    @FXML
    private SideBar sideBar;
    @FXML
    private BoardPane boardPane;

    @Autowired
    public GamePaneController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        transformBoard();
    }

    private void hookupChangeListeners() {
        sideBar.bindSize(boardPane.widthProperty());
        sideBar.getRotates().addListener((ListChangeListener.Change<? extends Rotate> change) -> {
            while (change.next()) {
                boardPane.getTransforms().clear();
                boardPane.getTransforms().addAll(change.getAddedSubList());
            }
        });
    }

    private void transformBoard() {
        boardPane.getTransforms().addAll(sideBar.getRotates());
    }
}
