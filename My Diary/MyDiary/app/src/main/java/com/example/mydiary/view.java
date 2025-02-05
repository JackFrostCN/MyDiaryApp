package com.example.mydiary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class  view extends AppCompatActivity {

    private ListView noteListView;
    private EditText searchEditText;
    private ArrayList<String> notesList;
    private ArrayAdapter<String> notesAdapter;

    private static final String PREFS_NAME = "MyNotesPrefs";
    private static final String NOTES_SET_KEY = "notesSet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        noteListView = findViewById(R.id.noteListView);
        searchEditText = findViewById(R.id.searchEditText);

        // Load notes from SharedPreferences
        notesList = loadNotes();

        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        noteListView.setAdapter(notesAdapter);

        // Implement search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Long click listener for deleting notes
        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Note");
                builder.setMessage("Are you sure you want to delete this note?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notesList.remove(position);
                        notesAdapter.notifyDataSetChanged();
                        saveNotes(notesList); // Save notes after removing
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true; // Return true to consume the long click event
            }
        });

        // Click listener for editing notes
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Edit Note");

                // Set up the input
                final EditText input = new EditText(view.getContext());
                input.setText(notesList.get(position)); // Pre-fill the edit text with the selected note
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editedNote = input.getText().toString();
                        if (!editedNote.isEmpty()) {
                            notesList.set(position, editedNote); // Update the note in the list
                            notesAdapter.notifyDataSetChanged();
                            saveNotes(notesList); // Save notes after editing
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                builder.show();
            }
        });
    }

    private ArrayList<String> loadNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> notesSet = sharedPreferences.getStringSet(NOTES_SET_KEY, new HashSet<String>());
        return new ArrayList<>(notesSet);
    }

    private void saveNotes(ArrayList<String> notesList) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> notesSet = new HashSet<>(notesList);
        editor.putStringSet(NOTES_SET_KEY, notesSet);
        editor.apply();
    }
}
