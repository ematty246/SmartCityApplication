package com.example;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static Map<String, Admin> adminSessions = new HashMap<>();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // Serve login form
        server.createContext("/", exchange -> {
            String html = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>City Complaint Management System</title>
                        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                        <style>
                            * {
                                margin: 0;
                                padding: 0;
                                box-sizing: border-box;
                            }

                            body {
                                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                min-height: 100vh;
                                display: flex;
                                align-items: center;
                                justify-content: center;
                                padding: 20px;
                            }

                            .main-container {
                                background: rgba(255, 255, 255, 0.95);
                                backdrop-filter: blur(10px);
                                border-radius: 20px;
                                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
                                padding: 40px;
                                width: 100%%;
                                max-width: 520px;
                                border: 1px solid rgba(255, 255, 255, 0.2);
                            }

                            .header {
                                text-align: center;
                                margin-bottom: 40px;
                            }

                            .header h1 {
                                color: #2d3748;
                                font-size: 28px;
                                font-weight: 700;
                                margin-bottom: 8px;
                                background: linear-gradient(135deg, #667eea, #764ba2);
                                -webkit-background-clip: text;
                                -webkit-text-fill-color: transparent;
                                background-clip: text;
                            }

                            .header p {
                                color: #718096;
                                font-size: 14px;
                                font-weight: 500;
                            }

                            .form-group {
                                margin-bottom: 26px;
                                position: relative;
                            }

                            .form-group label {
                                display: block;
                                margin-bottom: 10px;
                                color: #2d3748;
                                font-weight: 600;
                                font-size: 15px;
                                letter-spacing: 0.025em;
                            }

                            .input-wrapper {
                                position: relative;
                            }

                            .input-wrapper i {
                                position: absolute;
                                left: 18px;
                                top: 50%%;
                                transform: translateY(-50%%);
                                color: #a0aec0;
                                font-size: 16px;
                                z-index: 2;
                            }

                            input, select, textarea {
                                width: 100%%;
                                padding: 18px 22px;
                                border: 2px solid #e2e8f0;
                                border-radius: 12px;
                                font-size: 16px;
                                color: #2d3748;
                                background-color: #ffffff;
                                transition: all 0.3s ease;
                                outline: none;
                                font-family: inherit;
                                min-height: 56px;
                            }

                            input.with-icon {
                                padding-left: 52px;
                            }

                            input:focus, select:focus, textarea:focus {
                                border-color: #667eea;
                                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
                                background-color: #ffffff;
                            }

                            input:hover, select:hover, textarea:hover {
                                border-color: #cbd5e0;
                            }

                            textarea {
                                resize: vertical;
                                min-height: 120px;
                                font-family: inherit;
                                padding-top: 18px;
                            }

                            .role-select {
                                background-image: url("data:image/svg+xml;charset=UTF-8,%%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%%3e%%3cpolyline points='6,9 12,15 18,9'%%3e%%3c/polyline%%3e%%3c/svg%%3e");
                                background-repeat: no-repeat;
                                background-position: right 18px center;
                                background-size: 20px;
                                appearance: none;
                                cursor: pointer;
                            }

                            .complaint-fields {
                                background: #f7fafc;
                                border-radius: 12px;
                                padding: 26px;
                                border: 2px dashed #e2e8f0;
                                transition: all 0.3s ease;
                                margin-top: 10px;
                            }

                            .complaint-fields.show {
                                border-color: #667eea;
                                background: linear-gradient(135deg, rgba(102, 126, 234, 0.05), rgba(118, 75, 162, 0.05));
                            }

                            .complaint-header {
                                display: flex;
                                align-items: center;
                                margin-bottom: 22px;
                                color: #4a5568;
                            }

                            .complaint-header i {
                                margin-right: 10px;
                                font-size: 18px;
                            }

                            .complaint-header h3 {
                                font-size: 17px;
                                font-weight: 600;
                                margin: 0;
                            }

                            .submit-btn {
                                width: 100%%;
                                padding: 18px 26px;
                                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                color: white;
                                border: none;
                                border-radius: 12px;
                                font-size: 16px;
                                font-weight: 600;
                                cursor: pointer;
                                transition: all 0.3s ease;
                                text-transform: uppercase;
                                letter-spacing: 0.5px;
                                position: relative;
                                overflow: hidden;
                                min-height: 56px;
                                margin-top: 10px;
                            }

                            .submit-btn:hover {
                                transform: translateY(-2px);
                                box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
                            }

                            .submit-btn:active {
                                transform: translateY(0);
                            }

                            .submit-btn::before {
                                content: '';
                                position: absolute;
                                top: 0;
                                left: -100%%;
                                width: 100%%;
                                height: 100%%;
                                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
                                transition: left 0.5s;
                            }

                            .submit-btn:hover::before {
                                left: 100%%;
                            }

                            @media (max-width: 480px) {
                                body {
                                    padding: 15px;
                                }

                                .main-container {
                                    padding: 30px 25px;
                                    max-width: 100%%;
                                }

                                .header h1 {
                                    font-size: 24px;
                                }

                                input, select, textarea {
                                    padding: 16px 30px;
                                    font-size: 16px;
                                    min-height: 52px;
                                }

                                input.with-icon {
                                    padding-left: 48px;
                                }

                                .complaint-fields {
                                    padding: 22px;
                                }

                                .submit-btn {
                                    padding: 16px 24px;
                                    min-height: 52px;
                                }
                            }

                            .floating-shapes {
                                position: fixed;
                                top: 0;
                                left: 0;
                                width: 100%%;
                                height: 100%%;
                                pointer-events: none;
                                z-index: -1;
                            }

                            .shape {
                                position: absolute;
                                background: rgba(255, 255, 255, 0.1);
                                border-radius: 50%%;
                                animation: float 6s ease-in-out infinite;
                            }

                            .shape:nth-child(1) {
                                width: 80px;
                                height: 80px;
                                top: 20%%;
                                left: 10%%;
                                animation-delay: 0s;
                            }

                            .shape:nth-child(2) {
                                width: 120px;
                                height: 120px;
                                top: 60%%;
                                right: 10%%;
                                animation-delay: 2s;
                            }

                            .shape:nth-child(3) {
                                width: 60px;
                                height: 60px;
                                bottom: 20%%;
                                left: 20%%;
                                animation-delay: 4s;
                            }

                            @keyframes float {
                                0%%, 100%% {
                                    transform: translateY(0px) rotate(0deg);
                                }
                                50%% {
                                    transform: translateY(-20px) rotate(180deg);
                                }
                            }
                        </style>
                        <script>
                            function toggleFields() {
                                var role = document.getElementById('role').value;
                                var complaintFields = document.getElementById('complaintFields');
                                if (role === 'citizen') {
                                    complaintFields.style.display = 'block';
                                    complaintFields.classList.add('show');
                                } else {
                                    complaintFields.style.display = 'none';
                                    complaintFields.classList.remove('show');
                                }
                            }

                            document.addEventListener('DOMContentLoaded', function() {
                                toggleFields();
                            });
                        </script>
                    </head>
                    <body>
                        <div class="floating-shapes">
                            <div class="shape"></div>
                            <div class="shape"></div>
                            <div class="shape"></div>
                        </div>

                        <div class='main-container'>
                            <div class="header">
                                <h1><i class="fas fa-city"></i> City Complaint System</h1>
                                <p>Submit and manage municipal complaints efficiently</p>
                            </div>

                            <form method='POST' action='/login'>
                                <div class="form-group">
                                    <label for="name">Full Name</label>
                                    <div class="input-wrapper">
                                        <i class="fas fa-user"></i>
                                        <input type='text' name='name' id='name' class="with-icon" placeholder="Enter your full name" required />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="age">Age</label>
                                    <div class="input-wrapper">
                                        <i class="fas fa-calendar-alt"></i>
                                        <input type='number' name='age' id='age' class="with-icon" placeholder="Enter your age" min="18" max="100" required />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="id">Citizen ID</label>
                                    <div class="input-wrapper">
                                        <i class="fas fa-id-card"></i>
                                        <input type='text' name='id' id='id' class="with-icon" placeholder="Enter your citizen ID" required />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="password">Password</label>
                                    <div class="input-wrapper">
                                        <i class="fas fa-lock"></i>
                                        <input type='password' name='password' id='password' class="with-icon" placeholder="Enter your password" required />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="role">Select Role</label>
                                    <select name='role' id='role' class="role-select" onchange='toggleFields()'>
                                        <option value='citizen'>👤 Citizen</option>
                                        <option value='admin'>🛡️ Administrator</option>
                                    </select>
                                </div>

                                <div id='complaintFields' class="complaint-fields">
                                    <div class="complaint-header">
                                        <i class="fas fa-exclamation-triangle"></i>
                                        <h3>Complaint Details</h3>
                                    </div>

                                    <div class="form-group">
                                        <label for="category">Category</label>
                                        <div class="input-wrapper">
                                            <i class="fas fa-tags"></i>
                                            <input type='text' name='category' id='category' class="with-icon" placeholder='e.g., Electricity, Water Supply, Roads' />
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="description">Description</label>
                                        <textarea name='description' id='description' placeholder='Please provide detailed information about your complaint...'></textarea>
                                    </div>
                                </div>

                                <button type='submit' class='submit-btn'>
                                    <i class="fas fa-paper-plane"></i> Submit
                                </button>
                            </form>
                        </div>
                    </body>
                    </html>
                    """;
            exchange.sendResponseHeaders(200, html.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(html.getBytes());
            }
        });

        // Handle login
        server.createContext("/login", exchange -> {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String formData = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                String name = extractValue(formData, "name");
                int age = Integer.parseInt(extractValue(formData, "age"));
                String id = extractValue(formData, "id");
                String password = extractValue(formData, "password");
                String role = extractValue(formData, "role");

                String response;

                if ("citizen".equals(role)) {
                    String category = extractValue(formData, "category");
                    String description = extractValue(formData, "description");

                    Citizen c = new Citizen(name, age, id, "citizen123");
                    if (c.checkPassword(password)) {
                        c.fileComplaintWeb(category, description); // still void
                        response = wrapHTML("✅ Complaint Submitted Successfully",
                                "<p>Thank you, <strong>" + name
                                        + "</strong>! Your complaint has been submitted successfully.</p>" +
                                        "<p>Category: <strong>" + category + "</strong></p>" +
                                        "<p>We will review your complaint and take appropriate action.</p>",
                                "success");
                    } else {
                        response = wrapHTML("❌ Authentication Failed",
                                "<p>Incorrect password for citizen. Please try again.</p>", "error");
                    }

                } else if ("admin".equals(role)) {
                    Admin admin = new Admin(name, age, id, "admin123");
                    if (admin.checkPassword(password)) {
                        adminSessions.put(id, admin); // store admin session

                        // Display complaints and form to resolve
                        StringBuilder sb = new StringBuilder();
                        sb.append("<div class='welcome-header'>");
                        sb.append("<h2><i class='fas fa-user-shield'></i> Welcome, Admin ").append(name)
                                .append("</h2>");
                        sb.append("</div>");

                        sb.append("<div class='complaints-section'>");
                        sb.append("<h3><i class='fas fa-list-alt'></i> Active Complaints</h3>");
                        sb.append("<div class='complaints-container'>");

                        try (Scanner sc = new Scanner(new File("complaints.txt"))) {
                            if (!sc.hasNextLine()) {
                                sb.append("<p class='no-complaints'>No complaints found.</p>");
                            } else {
                                while (sc.hasNextLine()) {
                                    String line = sc.nextLine();
                                    sb.append("<div class='complaint-item'>").append(line).append("</div>");
                                }
                            }
                        } catch (IOException e) {
                            sb.append("<div class='error-message'>Error reading complaints file.</div>");
                        }

                        sb.append("</div></div>");

                        sb.append("<div class='resolve-section'>");
                        sb.append("<h3><i class='fas fa-tools'></i> Resolve Complaint</h3>");
                        sb.append("<form method='POST' action='/resolve' class='resolve-form'>");
                        sb.append("<div class='form-group'>");
                        sb.append("<label for='complaintId'>Complaint ID to Resolve:</label>");
                        sb.append(
                                "<input type='text' name='complaintId' id='complaintId' placeholder='Enter complaint ID' required />");
                        sb.append("</div>");
                        sb.append("<input type='hidden' name='adminId' value='" + id + "' />");
                        sb.append(
                                "<button type='submit' class='resolve-btn'><i class='fas fa-check-circle'></i> Resolve Complaint</button>");
                        sb.append("</form>");
                        sb.append("</div>");

                        response = wrapHTML("🛡️ Admin Dashboard", sb.toString(), "admin");

                    } else {
                        response = wrapHTML("❌ Authentication Failed",
                                "<p>Incorrect password for administrator. Please try again.</p>", "error");
                    }
                } else {
                    response = wrapHTML("❌ Invalid Role",
                            "<p>Invalid role selected. Please choose either Citizen or Administrator.</p>", "error");
                }

                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        server.createContext("/resolve", exchange -> {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String formData = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String complaintId = extractValue(formData, "complaintId");
                String adminId = extractValue(formData, "adminId");

                Admin admin = adminSessions.get(adminId);

                String message;
                String messageType;
                if (admin != null) {
                    Admin.setTempContext(complaintId, admin.getName());
                    boolean resolved = admin.resolveComplaintWeb(); // now returns true or false

                    if (resolved) {
                        message = "<p>Complaint ID <strong>" + complaintId
                                + "</strong> has been resolved successfully.</p>" +
                                "<p>Resolved by: <strong>Admin " + admin.getName() + "</strong></p>" +
                                "<p>Status updated and citizen has been notified.</p>";
                        messageType = "success";
                    } else {
                        message = "<p>Failed to resolve complaint.</p>" +
                                "<p>Complaint ID <strong>" + complaintId + "</strong> was not found in the system.</p>"
                                +
                                "<p>Please verify the complaint ID and try again.</p>";
                        messageType = "error";
                    }
                } else {
                    message = "<p>Failed to resolve complaint.</p>" +
                            "<p>Admin session has expired or is invalid.</p>" +
                            "<p>Please log in again to continue.</p>";
                    messageType = "error";
                }

                String title = messageType.equals("success") ? "✅ Complaint Resolved" : "❌ Resolution Failed";
                String response = wrapHTML(title, message, messageType);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:8081");
    }

    private static String extractValue(String data, String key) {
        for (String pair : data.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv[0].equals(key)) {
                return kv.length > 1 ? java.net.URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            }
        }
        return "";
    }

    private static String wrapHTML(String title, String body, String type) {
        String backgroundColor = switch (type) {
            case "success" -> "#d4edda";
            case "error" -> "#f8d7da";
            case "admin" -> "#e3f2fd";
            default -> "#ffffff";
        };

        String borderColor = switch (type) {
            case "success" -> "#28a745";
            case "error" -> "#dc3545";
            case "admin" -> "#2196f3";
            default -> "#dee2e6";
        };

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>City Complaint System - %s</title>
                    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }

                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            min-height: 100vh;
                            padding: 20px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }

                        .container {
                            background: rgba(255, 255, 255, 0.95);
                            backdrop-filter: blur(10px);
                            border-radius: 20px;
                            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
                            padding: 40px;
                            max-width: 800px;
                            width: 100%%;
                            border-left: 6px solid %s;
                        }

                        .header {
                            text-align: center;
                            margin-bottom: 30px;
                            padding-bottom: 20px;
                            border-bottom: 2px solid #f1f3f4;
                        }

                        .header h1 {
                            color: #2d3748;
                            font-size: 28px;
                            font-weight: 700;
                            margin-bottom: 10px;
                        }

                        .welcome-header {
                            background: linear-gradient(135deg, #667eea, #764ba2);
                            color: white;
                            padding: 20px;
                            border-radius: 12px;
                            margin-bottom: 30px;
                            text-align: center;
                        }

                        .welcome-header h2 {
                            font-size: 24px;
                            font-weight: 600;
                        }

                        .complaints-section {
                            margin-bottom: 30px;
                        }

                        .complaints-section h3 {
                            color: #2d3748;
                            font-size: 20px;
                            margin-bottom: 15px;
                            display: flex;
                            align-items: center;
                            gap: 10px;
                        }

                        .complaints-container {
                            background: #f8f9fa;
                            border-radius: 12px;
                            padding: 20px;
                            max-height: 300px;
                            overflow-y: auto;
                            border: 2px solid #e9ecef;
                        }

                        .complaint-item {
                            background: white;
                            padding: 15px;
                            margin-bottom: 10px;
                            border-radius: 8px;
                            border-left: 4px solid #667eea;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                            font-family: 'Courier New', monospace;
                            font-size: 14px;
                        }

                        .no-complaints {
                            text-align: center;
                            color: #6c757d;
                            font-style: italic;
                            padding: 20px;
                        }

                        .error-message {
                            background: #f8d7da;
                            color: #721c24;
                            padding: 15px;
                            border-radius: 8px;
                            border: 1px solid #f5c6cb;
                        }

                        .resolve-section {
                            background: #f8f9fa;
                            padding: 25px;
                            border-radius: 12px;
                            border: 2px solid #e9ecef;
                        }

                        .resolve-section h3 {
                            color: #2d3748;
                            font-size: 18px;
                            margin-bottom: 20px;
                            display: flex;
                            align-items: center;
                            gap: 10px;
                        }

                        .resolve-form .form-group {
                            margin-bottom: 20px;
                        }

                        .resolve-form label {
                            display: block;
                            margin-bottom: 8px;
                            color: #2d3748;
                            font-weight: 600;
                            font-size: 14px;
                        }

                        .resolve-form input[type="text"] {
                            width: 100%%;
                            padding: 12px 16px;
                            border: 2px solid #e2e8f0;
                            border-radius: 8px;
                            font-size: 16px;
                            transition: all 0.3s ease;
                        }

                        .resolve-form input[type="text"]:focus {
                            border-color: #667eea;
                            outline: none;
                            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
                        }

                        .resolve-btn {
                            background: linear-gradient(135deg, #28a745, #20c997);
                            color: white;
                            border: none;
                            padding: 12px 24px;
                            border-radius: 8px;
                            font-size: 16px;
                            font-weight: 600;
                            cursor: pointer;
                            transition: all 0.3s ease;
                            display: flex;
                            align-items: center;
                            gap: 8px;
                        }

                        .resolve-btn:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 8px 20px rgba(40, 167, 69, 0.3);
                        }

                        .content {
                            line-height: 1.6;
                            color: #2d3748;
                        }

                        .content p {
                            margin-bottom: 15px;
                        }

                        .content strong {
                            color: #1a202c;
                        }

                        .back-btn {
                            margin-top: 30px;
                            text-align: center;
                        }

                        .back-btn a {
                            display: inline-flex;
                            align-items: center;
                            gap: 8px;
                            padding: 12px 24px;
                            background: linear-gradient(135deg, #667eea, #764ba2);
                            color: white;
                            text-decoration: none;
                            border-radius: 8px;
                            font-weight: 600;
                            transition: all 0.3s ease;
                        }

                        .back-btn a:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
                        }

                        @media (max-width: 768px) {
                            .container {
                                padding: 25px;
                                margin: 10px;
                            }

                            .header h1 {
                                font-size: 24px;
                            }

                            .welcome-header h2 {
                                font-size: 20px;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>%s</h1>
                        </div>
                        <div class="content">
                            %s
                        </div>
                        <div class="back-btn">
                            <a href="/"><i class="fas fa-arrow-left"></i> Back to Login</a>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(title, borderColor, title, body);
    }
}