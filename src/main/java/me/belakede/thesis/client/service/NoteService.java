package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import me.belakede.thesis.client.boundary.javafx.model.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final ListProperty<Note> notes = new SimpleListProperty<>();

    public ObservableList<Note> getNotes() {
        return notes.get();
    }

    public void setNotes(ObservableList<Note> notes) {
        this.notes.set(notes);
    }

    public ListProperty<Note> notesProperty() {
        return notes;
    }

}
