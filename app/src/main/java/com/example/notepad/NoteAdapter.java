package com.example.notepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public interface OnNoteClickListener {
        void onClick(Note note);
        void onLongClick(Note note);
    }

    private List<Note> notes;
    private final OnNoteClickListener listener;

    public NoteAdapter(List<Note> notes, OnNoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.textView.setText(note.getText());

        holder.itemView.setOnClickListener(v -> listener.onClick(note));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(note);
            return true; // true = событие обработано, обычный клик дальше не сработает
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        NoteViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvNoteText);
        }
    }
}
