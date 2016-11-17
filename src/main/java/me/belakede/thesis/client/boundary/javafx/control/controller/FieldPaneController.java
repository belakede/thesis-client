package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.game.field.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope("prototype")
public class FieldPaneController implements Initializable {

    private final SimpleObjectProperty<Field> field = new SimpleObjectProperty<>();
    private final GameService gameService;

    @FXML
    private Pane content;

    @Autowired
    public FieldPaneController(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
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
            content.getStyleClass().add(newValue.getFieldType().name().toLowerCase());
        });
    }

}
