package me.belakede.thesis.client.boundary.javafx.control;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.util.ControlLoader;

public class HistoryBox extends VBox {

    private ListProperty<String> events = new SimpleListProperty<>();
    @FXML
    private ListView<String> eventList;

    public HistoryBox() {
        load();
        //bindEvents();
    }

    public ObservableList<String> getEvents() {
        return events.get();
    }

    public void setEvents(ObservableList<String> events) {
        this.events.set(events);
    }

    public ListProperty<String> eventsProperty() {
        return events;
    }

    private void load() {
        ControlLoader.load(this);
    }

    private void bindEvents() {
        eventList.itemsProperty().bind(events);
    }


}
