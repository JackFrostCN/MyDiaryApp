package com.example.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button addNoteButton;
    private Button showNotesButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteButton = findViewById(R.id.addNoteButton);
        showNotesButton = findViewById(R.id.showNotesButton);
        settingsButton = findViewById(R.id.settingsButton);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,Add.class));
                Toast.makeText(MainActivity.this, "Add a Note", Toast.LENGTH_SHORT).show();
            }
        });

        showNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,view.class));
                Toast.makeText(MainActivity.this, "Show Notes", Toast.LENGTH_SHORT).show();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,settings.class));

                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();

            }

            private void startActivity() {
            }
        });
    }
}

