package com.example.studentcv;

import com.google.firebase.Timestamp;

public class JobApplication {
    private String jobId;
    private String studentId;
    private String cvBase64;
    private Timestamp appliedDate;
    // New status field: "Pending", "Approved", "Disapproved"
    private String status;

    // Empty constructor for Firestore
    public JobApplication() {}

    public JobApplication(String jobId, String studentId, String cvBase64) {
        this.jobId = jobId;
        this.studentId = studentId;
        this.cvBase64 = cvBase64;
        this.appliedDate = Timestamp.now();
        this.status = "Pending";
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCvBase64() {
        return cvBase64;
    }

    public void setCvBase64(String cvBase64) {
        this.cvBase64 = cvBase64;
    }

    public Timestamp getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(Timestamp appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}