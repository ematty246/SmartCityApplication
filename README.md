"""
# 🌆 Smart City Complaint Management System

A **Java-based desktop + web-integrated application** to manage and resolve public complaints efficiently — built as part of a *6-day Java Programming Course* at **Ethnotech Lab**.

---

## 📌 Project Highlights

- 🔐 **Admin & Citizen Authentication**
- 📝 **Complaint Filing (Citizen)**
- 📄 **Complaint Viewing & Resolving (Admin)**
- 🌐 **Mini Web Interface** using `HttpServer`
- 📁 File-based storage (`complaints.txt`, `resolved.txt`)

---

## 💻 Technologies Used

| Tool/Tech          | Purpose                          |
|--------------------|----------------------------------|
| Java (Core + IO)   | Application Logic & File Handling |
| Java HttpServer    | Lightweight Web Interface        |
| File I/O           | Read/Write Complaints            |
| Git & GitHub       | Version Control                  |

---

## 🗂️ Project Structure

SmartCityApplication/
├── src/ # Java source files
│ ├── com.example/
│ │ ├── Admin.java
│ │ ├── Citizen.java
│ │ ├── Person.java
│ │ ├── ReportManager.java
│ │ └── Main.java
├── complaints.txt # Stores pending complaints
├── resolved.txt # Stores resolved complaints
├── screenshots/ # Demo screenshots
├── .gitignore
└── README.md

yaml
Copy
Edit

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/ematty246/SmartCityApplication.git
cd SmartCityApplication
2. Compile & Run
bash
Copy
Edit
cd src
javac com/example/*.java
java com.example.Main
🧪 Sample Usage
Citizen:
Fills in name, age, ID

Files complaint with category and description

Complaint saved in complaints.txt

Admin:
Logs in with Admin ID & password

Views list of complaints via web interface

Enters complaint ID to resolve it

Complaint moved to resolved.txt

📸 Screenshots
Add your screenshots inside the screenshots/ folder

pgsql
Copy
Edit
screenshots/
├── login.png
├── complaint-view.png
├── resolved.png
🏁 Course Details
👨‍🏫 Course: Programming in Java

🏢 Conducted at: Ethnotech Lab

📅 Duration: 6 Days

🎓 Outcome: Hands-on project completion with GitHub deployment
