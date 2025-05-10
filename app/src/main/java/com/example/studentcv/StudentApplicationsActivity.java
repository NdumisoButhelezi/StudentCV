package com.example.studentcv;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentApplicationsActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private RecyclerView applicationsRecyclerView;
    private ApplicationsAdapter applicationsAdapter;
    private List<JobApplication> applicationList;
    // Get the real student id from FirebaseAuth
    private String studentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_applications);

        firestore = FirebaseFirestore.getInstance();
        applicationsRecyclerView = findViewById(R.id.applicationsRecyclerView);
        applicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        applicationList = new ArrayList<>();
        applicationsAdapter = new ApplicationsAdapter(applicationList);
        applicationsRecyclerView.setAdapter(applicationsAdapter);

        fetchApplicationsFromFirestore();
    }

    private void fetchApplicationsFromFirestore() {
        firestore.collection("job_applications")
                .whereEqualTo("studentId", studentId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    applicationList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        JobApplication application = document.toObject(JobApplication.class);
                        applicationList.add(application);
                    }
                    applicationsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(StudentApplicationsActivity.this, "Error fetching applications: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}