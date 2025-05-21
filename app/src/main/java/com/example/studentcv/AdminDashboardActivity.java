package com.example.studentcv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        // Adjust window insets (padding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup navigation buttons
        View btnUsers = findViewById(R.id.cardUsers);
        View btnJobs = findViewById(R.id.cardJobs);
        View btnOther = findViewById(R.id.cardOther);

        btnUsers.setOnClickListener((View v) -> {
            Intent intent = new Intent(AdminDashboardActivity.this, UsersActivity.class);
            startActivity(intent);
        });

        btnJobs.setOnClickListener((View v) -> {
            Intent intent = new Intent(AdminDashboardActivity.this, JobsActivity.class);
            startActivity(intent);
        });

        btnOther.setOnClickListener((View v) -> {
            // Placeholder for additional admin functionality.
            // For example, you could navigate to another activity:
            // Intent intent = new Intent(AdminDashboardActivity.this, OtherActivity.class);
            // startActivity(intent);
        });
    }
}