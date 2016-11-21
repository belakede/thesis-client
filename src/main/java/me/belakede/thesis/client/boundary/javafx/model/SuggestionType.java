package me.belakede.thesis.client.boundary.javafx.model;

public enum SuggestionType {

    SUSPECT("Suspect"), ACCUSE("Accuse");

    private final String label;

    SuggestionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
