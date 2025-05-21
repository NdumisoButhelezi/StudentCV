package com.example.studentcv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {

    private List<JobApplication> applications;
    private Context context;
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public ApplicationsAdapter(List<JobApplication> applications, Context context) {
        this.applications = applications;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_application_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobApplication app = applications.get(position);

        holder.emailTextView.setText("Email: " + app.getStudentEmail());
        holder.jobTitleTextView.setText("Job Title: " + app.getJobTitle());
        holder.statusTextView.setText("Status: " + app.getStatus());

        Timestamp ts = app.getAppliedDate();
        if (ts != null) {
            Date d = ts.toDate();
            holder.dateTextView.setText("Applied: " + DATE_FORMAT.format(d));
        } else {
            holder.dateTextView.setText("Applied: N/A");
        }
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        TextView jobTitleTextView;
        TextView statusTextView;
        TextView dateTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView    = itemView.findViewById(R.id.txtEmail);
            jobTitleTextView = itemView.findViewById(R.id.txtJobTitle);
            statusTextView   = itemView.findViewById(R.id.txtStatus);
            dateTextView     = itemView.findViewById(R.id.txtAppliedDate);
        }
    }
}
