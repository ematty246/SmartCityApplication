package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Admin extends Person implements ReportManager {
    private String adminId;
    private String password;

    public Admin(String name, int age, String adminId, String password) {
        super(name, age);
        this.adminId = adminId;
        this.password = password;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    @Override
    public void display() {
        System.out.println("Admin Information:");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Admin ID: " + adminId);
    }

    @Override
    public void generateReport() {
        System.out.println("Generating report...");
    }

    private static String tempComplaintId;
    private static String tempAdminName;

    public static void setResolveContext(String complaintId, String adminName) {
        tempComplaintId = complaintId;
        tempAdminName = adminName;
    }

    // Add this static method
    public static void setTempContext(String complaintId, String adminName) {
        tempComplaintId = complaintId;
        tempAdminName = adminName;
    }

    public String viewComplaintsWeb() {
        StringBuilder html = new StringBuilder();

        html.append("""
                    <div style='
                        font-family: Arial, sans-serif;
                        background-color: #f9f9f9;
                        padding: 20px;
                        border-radius: 10px;
                        box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        margin: 20px 0;
                    '>
                    <h2 style='color: #007acc;'>All Complaints</h2>
                """);

        boolean anyComplaints = false;

        try (Scanner sc = new Scanner(new java.io.File("complaints.txt"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",", 5);

                if (parts.length == 5) {
                    String complaintId = parts[0];
                    String citizenId = parts[1];
                    String category = parts[2];
                    String description = parts[3];
                    String status = parts[4];

                    html.append(String.format("""
                                <div style='
                                    border: 1px solid #ccc;
                                    padding: 15px;
                                    margin: 10px 0;
                                    border-radius: 8px;
                                    background-color: #fff;
                                '>
                                    <p><strong>Complaint ID:</strong> %s</p>
                                    <p><strong>Citizen ID:</strong> %s</p>
                                    <p><strong>Category:</strong> %s</p>
                                    <p><strong>Description:</strong> %s</p>
                                    <p><strong>Status:</strong> <span style='color:%s;'>%s</span></p>
                                </div>
                            """, complaintId, citizenId, category, description,
                            status.equalsIgnoreCase("Pending") ? "orange" : "green", status));

                    anyComplaints = true;
                }
            }

            if (!anyComplaints) {
                html.append("<p style='color: gray;'>No complaints found.</p>");
            }

        } catch (IOException e) {
            return "<div style='color:red;'>Error reading complaints: " + e.getMessage() + "</div>";
        }

        html.append("</div>");
        return html.toString();
    }

    public boolean resolveComplaintWeb() {
        String resolveId = tempComplaintId;
        String name = tempAdminName;
        boolean found = false;

        try {
            File inputFile = new File("complaints.txt");
            Scanner reader = new Scanner(inputFile);

            FileWriter tempWriter = new FileWriter("temp.txt");
            FileWriter resolvedWriter = new FileWriter("resolved.txt", true);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (line.startsWith(resolveId + ",")) {
                    int lastComma = line.lastIndexOf(",");
                    String resolvedLine = line.substring(0, lastComma) + ",Resolved by " + name;
                    resolvedWriter.write(resolvedLine + "\n");
                    found = true;
                    System.out.println("Complaint moved to resolved.txt.");
                } else {
                    tempWriter.write(line + "\n");
                }
            }

            reader.close();
            tempWriter.close();
            resolvedWriter.close();
            inputFile.delete();
            new File("temp.txt").renameTo(inputFile);

        } catch (Exception e) {
            System.out.println("Error resolving complaint: " + e.getMessage());
        }

        return found;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }
}
