package com.example.mydiary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Add extends AppCompatActivity {

    private EditText noteEditText;
    private Button addButton, removeButton;
    private ListView noteListView;
    private ArrayList<String> notesList;
    private ArrayAdapter<String> notesAdapter;

    private static final String PREFS_NAME = "MyNotesPrefs";
    private static final String NOTES_SET_KEY = "notesSet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        noteEditText = findViewById(R.id.noteEditText);
        addButton = findViewById(R.id.addButton);
        removeButton = findViewById(R.id.removeButton);
        noteListView = findViewById(R.id.noteListView);

        // Load notes from SharedPreferences
        notesList = loadNotes();

        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        noteListView.setAdapter(notesAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteText = noteEditText.getText().toString();
                if (!noteText.isEmpty()) {
                    // Append system time
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    String currentTime = sdf.format(new Date());

                    noteText = noteText + "    (" + currentTime + ")";

                    notesList.add(noteText);
                    notesAdapter.notifyDataSetChanged();
                    noteEditText.setText("");
                    saveNotes(notesList); // Save notes after adding
                }
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesList.clear();
                notesAdapter.notifyDataSetChanged();
                saveNotes(notesList); // Save notes after removing
                Toast.makeText(Add.this, "All notes removed", Toast.LENGTH_SHORT).show();
            }
        });

        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add.this);
                builder.setTitle("Delete Note");
                builder.setMessage("Are you sure you want to delete this note?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notesList.remove(position);
                        notesAdapter.notifyDataSetChanged();
                        saveNotes(notesList); // Save notes after removing
                        Toast.makeText(Add.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true; // Return true to consume the long click event
            }
        });

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add.this);
                builder.setTitle("Edit Note");

                // Set up the input
                final EditText input = new EditText(Add.this);
                input.setText(notesList.get(position)); // Pre-fill the edit text with the selected note
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editedNote = input.getText().toString();
                        if (!editedNote.isEmpty()) {
                            // Append system time
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            String currentTime = sdf.format(new Date());

                            editedNote = editedNote + "    (" + currentTime + ")";

                            notesList.set(position, editedNote); // Update the note in the list
                            notesAdapter.notifyDataSetChanged();
                            saveNotes(notesList); // Save notes after editing
                            Toast.makeText(Add.this, "Note edited", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                builder.show();
            }
        });
    }

    private void saveNotes(ArrayList<String> notesList) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> notesSet = new HashSet<>(notesList);
        editor.putStringSet(NOTES_SET_KEY, notesSet);
        editor.apply();
    }

    private ArrayList<String> loadNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> notesSet = sharedPreferences.getStringSet(NOTES_SET_KEY, new HashSet<String>());
        return new ArrayList<>(notesSet);
    }
}
