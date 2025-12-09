package com.example.dzienniktreningowy;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTrainingActivity extends AppCompatActivity {

    private EditText editName, editReps, editDuration;
    private Spinner spinnerDifficulty;
    private Button buttonSave, buttonCancel;
    private TrainingDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);


        dbHelper = new TrainingDbHelper(this);


        editName = findViewById(R.id.editExerciseName);
        editReps = findViewById(R.id.editRepsCount);
        editDuration = findViewById(R.id.editDuration);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTraining();
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveTraining() {

        String name = editName.getText().toString().trim();
        String repsString = editReps.getText().toString().trim();
        String durationString = editDuration.getText().toString().trim();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();


        if (name.isEmpty() || repsString.isEmpty() || durationString.isEmpty()) {
            Toast.makeText(this, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }


        int reps, duration;
        try {
            reps = Integer.parseInt(repsString);
            duration = Integer.parseInt(durationString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Wprowadź poprawne liczby", Toast.LENGTH_SHORT).show();
            return;
        }


        if (reps <= 0) {
            Toast.makeText(this, "Liczba powtórzeń musi być większa od 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (duration <= 0) {
            Toast.makeText(this, "Czas trwania musi być większy od 0 minut", Toast.LENGTH_SHORT).show();
            return;
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(TrainingDbHelper.COLUMN_NAME, name);
        values.put(TrainingDbHelper.COLUMN_REPS, reps);
        values.put(TrainingDbHelper.COLUMN_DURATION, duration);
        values.put(TrainingDbHelper.COLUMN_DATE, currentDate);
        values.put(TrainingDbHelper.COLUMN_DIFFICULTY, difficulty);


        long newRowId = db.insert(TrainingDbHelper.TABLE_NAME, null, values);


        if (newRowId == -1) {
            Toast.makeText(this, "Błąd zapisu do bazy danych", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Trening został zapisany", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}