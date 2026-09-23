package com.example.notepad;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class NoteRepository {

    private final DatabaseHelper dbHelper;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    public interface NotesCallback {
        void onLoaded(List<Note> notes);
    }

    public NoteRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private CollectionReference notesCollection() {
        String uid = auth.getCurrentUser().getUid();
        return firestore.collection("users").document(uid).collection("notes");
    }

    // Сохраняем сначала в Firestore, потом в SQLite (с полученным id)
    public void addNote(String text) {
        Note note = new Note(text);
        notesCollection().add(note).addOnSuccessListener(docRef -> {
            note.setId(docRef.getId());
            dbHelper.insertNote(note);
        });
    }

    public void updateNote(Note note, String newText) {
        note.setText(newText);
        dbHelper.updateNote(note);
        if (note.getId() != null) {
            notesCollection().document(note.getId()).set(note);
        }
    }

    public void deleteNote(Note note) {
        dbHelper.deleteNote(note.getLocalId());
        if (note.getId() != null) {
            notesCollection().document(note.getId()).delete();
        }
    }

    // Загружаем из Firestore и обновляем локальный кэш SQLite
    public void loadNotes(NotesCallback callback) {
        notesCollection().get().addOnSuccessListener(snapshot -> {
            dbHelper.clearAll();
            for (QueryDocumentSnapshot doc : snapshot) {
                Note note = doc.toObject(Note.class);
                note.setId(doc.getId());
                dbHelper.insertNote(note);
            }
            callback.onLoaded(dbHelper.getAllNotes());
        }).addOnFailureListener(e -> {
            // нет интернета — показываем то, что есть локально
            callback.onLoaded(dbHelper.getAllNotes());
        });
    }
}
