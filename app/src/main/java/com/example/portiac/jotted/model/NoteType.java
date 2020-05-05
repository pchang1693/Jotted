package com.example.portiac.jotted.model;

public enum NoteType {
    ENTRY, DREAM, SPROUT;
    public final static String entryString = "Entry";
    public final static String dreamString = "Dream";
    public final static String sproutString = "Sprout";

    public String getNoteTypeString() {
        switch (this) {
            case ENTRY:
                return entryString;
            case DREAM:
                return dreamString;
            case SPROUT:
                return sproutString;
        }
        return entryString;
    }
}
