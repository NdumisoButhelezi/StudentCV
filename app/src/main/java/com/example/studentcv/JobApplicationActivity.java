package com.example.studentcv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class JobApplicationActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore firestore;
    private String jobId;
    // Get the actual student id from FirebaseAuth
    private String studentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String cvBase64;
    private ImageView cvPreviewImageView;
    private Button selectImageButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_application);

        firestore = FirebaseFirestore.getInstance();
        jobId = getIntent().getStringExtra("jobId");

        cvPreviewImageView = findViewById(R.id.cvPreviewImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        submitButton = findViewById(R.id.submitButton);

        selectImageButton.setOnClickListener(v -> openImageChooser());
        submitButton.setOnClickListener(v -> submitApplication());
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select CV Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                cvPreviewImageView.setImageBitmap(bitmap);
                // Convert the bitmap image into a Base64 string
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                cvBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error processing image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitApplication() {
        if(cvBase64 == null || cvBase64.isEmpty()) {
            Toast.makeText(this, "Please select a CV image", Toast.LENGTH_SHORT).show();
            return;
        }
        JobApplication application = new JobApplication(jobId, studentId, cvBase64);
        firestore.collection("job_applications")
                .add(application)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(JobApplicationActivity.this, "Application submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(JobApplicationActivity.this, "Failed to submit application: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}