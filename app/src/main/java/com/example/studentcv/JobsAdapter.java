package com.example.studentcv;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobViewHolder> {

    private List<JobModel> jobList;

    public JobsAdapter(List<JobModel> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobModel job = jobList.get(position);
        holder.jobTitle.setText(job.getJobTitle());
        holder.companyName.setText(job.getCompanyName());
        holder.jobDescription.setText(job.getJobDescription());
        holder.jobType.setText(job.getJobType());
        holder.jobLocation.setText(job.getJobLocation());

        // Set click listener for Apply Now button
        holder.applyButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), JobApplicationActivity.class);
            // Pass the job id (assumes JobModel has a getJobId() method)
            intent.putExtra("jobId", job.getJobId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, companyName, jobDescription, jobType, jobLocation;
        MaterialButton applyButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyName = itemView.findViewById(R.id.companyName);
            jobDescription = itemView.findViewById(R.id.jobDescription);
            jobType = itemView.findViewById(R.id.jobType);
            jobLocation = itemView.findViewById(R.id.jobLocation);
            applyButton = itemView.findViewById(R.id.applyButton);
        }
    }
}