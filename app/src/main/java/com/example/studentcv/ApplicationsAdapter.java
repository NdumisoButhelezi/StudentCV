package com.example.studentcv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder> {

    private List<JobApplication> applicationList;

    public ApplicationsAdapter(List<JobApplication> applicationList) {
        this.applicationList = applicationList;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        JobApplication application = applicationList.get(position);
        holder.jobId.setText("Job ID: " + application.getJobId());
        holder.appliedDate.setText("Applied: " + application.getAppliedDate().toDate().toString());
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView jobId;
        TextView appliedDate;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            jobId = itemView.findViewById(R.id.jobId);
            appliedDate = itemView.findViewById(R.id.appliedDate);
        }
    }
}