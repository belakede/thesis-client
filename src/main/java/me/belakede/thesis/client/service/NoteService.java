package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import me.belakede.thesis.game.equipment.Card;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final ListProperty<Note> notes = new SimpleListProperty<>();
    private final MapProperty<Card, Integer> cardsOrder = new SimpleMapProperty<>();

    public ObservableList<Note> getNotes() {
        return notes.get();
    }

    public void setNotes(ObservableList<Note> notes) {
        this.notes.set(notes);
    }

    public ListProperty<Note> notesProperty() {
        return notes;
    }

    public ObservableMap<Card, Integer> getCardsOrder() {
        return cardsOrder.get();
    }

    public void setCardsOrder(ObservableMap<Card, Integer> cardsOrder) {
        this.cardsOrder.set(cardsOrder);
    }

    public MapProperty<Card, Integer> cardsOrderProperty() {
        return cardsOrder;
    }
}
