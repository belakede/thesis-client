package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;
import me.belakede.thesis.game.equipment.Figurine;
import org.controlsfx.control.PopOver;

public class HistoryPane extends StackPane {

    private final ObjectProperty<Figurine> figurine = new SimpleObjectProperty<>();
    @FXML
    private Button historyButton;
    private PopOver popOver;

    public HistoryPane() {
        load();
        setupPopover();
        hookupChangeListeners();
    }

    public HistoryPane(Figurine figurine) {
        load();
        setupPopover();
        hookupChangeListeners();
        setFigurine(figurine);
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

    private void load() {
        ControlLoader.load(this);
    }

    private void setupPopover() {
        popOver = new PopOver(new HistoryBox());
        popOver.setAnimated(true);
        popOver.setTitle("History");
        popOver.setHeaderAlwaysVisible(true);
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
    }

    private void hookupChangeListeners() {
        historyButton.setOnAction(event -> popOver.show(this));
    }

}
