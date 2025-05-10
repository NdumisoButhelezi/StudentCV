package com.example.studentcv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecruiterApplicationsAdapter extends RecyclerView.Adapter<RecruiterApplicationsAdapter.ApplicationViewHolder> {

    private List<JobApplication> applicationList;

    public RecruiterApplicationsAdapter(List<JobApplication> applicationList) {
        this.applicationList = applicationList;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application_recruiter, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        JobApplication application = applicationList.get(position);
        holder.txtStudentId.setText("Student ID: " + application.getStudentId());
        holder.txtJobId.setText("Job ID: " + application.getJobId());
        holder.txtAppliedDate.setText("Applied: " + application.getAppliedDate().toDate().toString());
        holder.txtStatus.setText("Status: " + application.getStatus());

        // If CV is available, decode Base64 to display in the preview image (optional)
        if (application.getCvBase64() != null && !application.getCvBase64().isEmpty()) {
            byte[] decodedString = Base64.decode(application.getCvBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.cvPreviewImageView.setImageBitmap(decodedByte);
        } else {
            holder.cvPreviewImageView.setImageResource(android.R.color.transparent);
        }

        // Approve button
        holder.btnApprove.setOnClickListener(view -> {
            FirebaseFirestore.getInstance().collection("job_applications")
                    .document(application.getJobId())
                    .update("status", "Approved")
                    .addOnSuccessListener(aVoid -> {
                        application.setStatus("Approved");
                        notifyItemChanged(position);
                    });
        });

        // Disapprove button
        holder.btnDisapprove.setOnClickListener(view -> {
            FirebaseFirestore.getInstance().collection("job_applications")
                    .document(application.getJobId())
                    .update("status", "Disapproved")
                    .addOnSuccessListener(aVoid -> {
                        application.setStatus("Disapproved");
                        notifyItemChanged(position);
                    });
        });
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView txtStudentId, txtJobId, txtAppliedDate, txtStatus;
        ImageView cvPreviewImageView;
        Button btnApprove, btnDisapprove;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStudentId = itemView.findViewById(R.id.txtStudentId);
            txtJobId = itemView.findViewById(R.id.txtJobId);
            txtAppliedDate = itemView.findViewById(R.id.txtAppliedDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            cvPreviewImageView = itemView.findViewById(R.id.cvPreviewImageView);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnDisapprove = itemView.findViewById(R.id.btnDisapprove);
        }
    }
}