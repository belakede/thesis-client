package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import me.belakede.thesis.game.equipment.Figurine;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope("prototype")
public class FigurinePaneController implements Initializable {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();

    @FXML
    private VBox parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
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

    private void hookupChangeListeners() {
        figurineProperty().addListener((observable, oldValue, newValue) -> {
            parent.getStyleClass().add(newValue.name().toLowerCase().replace("_", "-"));
        });
    }
}