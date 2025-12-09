package com.example.dzienniktreningowy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testButton = findViewById(R.id.bttn);

        testButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTrainingActivity.class));
        });


    }
}