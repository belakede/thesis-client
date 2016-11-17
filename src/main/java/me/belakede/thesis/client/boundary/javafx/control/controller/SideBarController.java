package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import me.belakede.thesis.client.boundary.javafx.control.RotateConfigurationPane;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.game.equipment.Figurine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SideBarController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SideBarController.class);
    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    private final GameService gameService;

    @FXML
    private ImageView imageView;
    @FXML
    private RotateConfigurationPane rotateConfiguration;

    @Autowired
    public SideBarController(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        setupBindings();
    }

    public RotateConfigurationPane getRotateConfiguration() {
        return rotateConfiguration;
    }

    private void hookupChangeListeners() {
        figurine.addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Figurine changed from {} to {}", oldValue, newValue);
            if (oldValue != null) {
                imageView.getStyleClass().remove(oldValue.name().toLowerCase());
            }
            imageView.getStyleClass().add(newValue.name().toLowerCase());
        });
    }

    private void setupBindings() {
        figurine.bind(gameService.figurineProperty());
    }

    public void bindSize(ReadOnlyDoubleProperty value) {
        rotateConfiguration.sizeProperty().unbind();
        rotateConfiguration.sizeProperty().bind(value);
    }

    public ObservableList<Rotate> getRotates() {
        return rotateConfiguration.getRotates();
    }

}
