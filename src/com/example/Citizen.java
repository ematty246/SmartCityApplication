package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class Citizen extends Person {
    private String citizenId;
    private String password;

    public Citizen(String name, int age, String citizenId, String password) {
        super(name, age);
        this.citizenId = citizenId;
        this.password = password;
    }

    public void display() {
        System.out.println("Citizen Information:");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Citizen ID: " + citizenId);
    }

    public String fileComplaintWeb(String category, String description) {
        String complaintId = "C" + UUID.randomUUID().toString().substring(0, 5);
        String status = "Pending";

        // Save to complaints.txt
        try (FileWriter writer = new FileWriter("complaints.txt", true)) {
            writer.write(complaintId + "," + citizenId + "," + category + "," + description + "," + status + "\n");
        } catch (IOException e) {
            return "<div style='color: red;'>Error writing complaint to file: " + e.getMessage() + "</div>";
        }

        return """
                    <div style='
                        font-family: Arial, sans-serif;
                        background-color: #f0f8ff;
                        padding: 20px;
                        border-radius: 10px;
                        box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        margin: 20px 0;
                    '>
                        <h2 style='color: #007acc;'>Complaint Filed Successfully</h2>
                        <p><strong>Complaint ID:</strong> %s</p>
                        <p><strong>Citizen ID:</strong> %s</p>
                        <p><strong>Category:</strong> %s</p>
                        <p><strong>Description:</strong> %s</p>
                        <p><strong>Status:</strong> <span style='color: orange;'>%s</span></p>
                    </div>
                """.formatted(complaintId, citizenId, category, description, status);
    }

    public String getCitizenId() {
        return citizenId;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
