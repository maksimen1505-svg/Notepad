package com.example.notepad;

public class Note {
    private String id;      // id документа Firestore
    private long localId;   // id строки в SQLite
    private String text;

    public Note() {
        // пустой конструктор нужен для Firestore (десериализация)
    }

    public Note(String text) {
        this.text = text;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public long getLocalId() { return localId; }
    public void setLocalId(long localId) { this.localId = localId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
