package com.example;

public class Complaint {
    private String complaintId;
    private String category;
    private String description;
    private String status;

    public Complaint(String complaintId, String category, String description, String status) {
        this.complaintId = complaintId;
        this.category = category;
        this.description = description;
        this.status = status;
    }

    public void display() {
        System.out.println("Complaint ID: " + complaintId);
        System.out.println("Category: " + category);
        System.out.println("Description: " + description);
        System.out.println("Status: " + status);
    }

    public String getComplaintId() {
        return complaintId;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
