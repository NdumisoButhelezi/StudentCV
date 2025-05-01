package com.example.studentcv;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CVPreviewActivity extends AppCompatActivity {

    private TextView cvContent;
    private Button downloadButton;
    private String cvData; // Store the CV data for PDF generation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_preview);

        cvContent = findViewById(R.id.cvContent);
        downloadButton = findViewById(R.id.downloadButton);

        // Get CV data from intent
        cvData = getIntent().getStringExtra("cvData");
        if (cvData != null) {
            cvContent.setText(cvData);
        }

        // Handle download button click
        downloadButton.setOnClickListener(v -> {
            if (checkPermission()) {
                generatePDF();
            } else {
                requestPermission();
            }
        });
    }

    // Check if the app has write permission
    private boolean checkPermission() {
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return writePermission == PackageManager.PERMISSION_GRANTED;
    }

    // Request write permission
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDF();
            } else {
                Toast.makeText(this, "Permission denied. Cannot generate PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Generate the CV as a PDF
    private void generatePDF() {
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        String fileName = "GeneratedCV.pdf";
        File file = new File(directoryPath, fileName);

        try {
            // Create a PdfWriter instance
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));

            // Create a PdfDocument instance
            PdfDocument pdfDocument = new PdfDocument(writer);

            // Create a Document instance
            Document document = new Document(pdfDocument);

            // Add CV content to the PDF
            document.add(new Paragraph(cvData));

            // Close the document
            document.close();

            Toast.makeText(this, "CV downloaded as PDF: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}