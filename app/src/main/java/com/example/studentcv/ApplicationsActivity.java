package com.example.studentcv;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ApplicationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);

        // Example logic for managing applications
        Button viewApplicationsButton = findViewById(R.id.viewApplicationsButton);
        viewApplicationsButton.setOnClickListener(v ->
                Toast.makeText(ApplicationsActivity.this, "View Applications clicked!", Toast.LENGTH_SHORT).show()
        );
    }
}