package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class NoteAdapter extends ArrayAdapter<NotesNote> {

    public NoteAdapter(Context context, List<NotesNote> notes) {
        super(context, 0, notes);
        Collections.sort(notes, (note1, note2) -> Boolean.compare(note2.getIsFavourite(), note1.getIsFavourite()));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NotesNote note = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_cell, parent, false);
        }

        TextView title = convertView.findViewById(R.id.cellTitle);
        TextView desc = convertView.findViewById(R.id.cellDesc);
        ImageView favoriteIcon = convertView.findViewById(R.id.favoriteIcon);

        title.setText(note.getTitle());
        desc.setText(note.getCont());

        if (note.getIsFavourite()) {
            favoriteIcon.setVisibility(View.VISIBLE);
        } else {
            favoriteIcon.setVisibility(View.GONE);
        }

        return convertView;
    }
}
