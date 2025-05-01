package com.example.studentcv;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AvailableJobsActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private RecyclerView jobsRecyclerView;
    private JobsAdapter jobsAdapter;
    private List<JobModel> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_available_jobs);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        jobsRecyclerView = findViewById(R.id.jobsRecyclerView);
        jobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        jobsAdapter = new JobsAdapter(jobList);
        jobsRecyclerView.setAdapter(jobsAdapter);

        // Fetch jobs from Firestore
        fetchJobsFromFirestore();
    }

    private void fetchJobsFromFirestore() {
        firestore.collection("job_posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    jobList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        JobModel job = document.toObject(JobModel.class);
                        jobList.add(job);
                    }
                    jobsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(AvailableJobsActivity.this, "Error fetching jobs: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}