package com.example.notepad;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    private NoteRepository repository;
    private NoteAdapter adapter;
    private EditText etNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Если вдруг сюда попали без входа - отправляем на LoginActivity
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        repository = new NoteRepository(this);

        etNoteText = findViewById(R.id.etNoteText);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnLogout = findViewById(R.id.btnLogout);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        adapter = new NoteAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> addNote());
        btnLogout.setOnClickListener(v -> logout());

        loadNotes();
    }

    private void loadNotes() {
        repository.loadNotes(notes -> adapter.setNotes(notes));
    }

    private void addNote() {
        String text = etNoteText.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Введите текст заметки", Toast.LENGTH_SHORT).show();
            return;
        }
        repository.addNote(text);
        etNoteText.setText("");
        loadNotes();
    }

    // Короткое нажатие - редактирование
    @Override
    public void onClick(Note note) {
        final EditText input = new EditText(this);
        input.setText(note.getText());

        new AlertDialog.Builder(this)
                .setTitle("Редактировать заметку")
                .setView(input)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String newText = input.getText().toString().trim();
                    if (!newText.isEmpty()) {
                        repository.updateNote(note, newText);
                        loadNotes();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    // Долгое нажатие - удаление
    @Override
    public void onLongClick(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Удалить заметку?")
                .setMessage(note.getText())
                .setPositiveButton("Удалить", (dialog, which) -> {
                    repository.deleteNote(note);
                    loadNotes();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
