# Rural Health Management System 🏥

A **Spring Boot-based RESTful backend application** designed to manage healthcare services in rural areas.
The system helps manage **patients, doctors, and appointments** efficiently while ensuring proper scheduling and healthcare delivery.

# 📌 Features

### 👤 User Management

* User Registration
* User Login / Authentication
* Role-based access
* Supported Roles:

  * ADMIN
  * DOCTOR
  * PATIENT
  * NURSE
  * USER

### 🧑‍⚕️ Doctor Module

* Create Doctor Profile
* Update Doctor Details
* Assign Specialization
* View Doctor List
* Doctor License Validation
* Track doctor experience and hospital details

Example fields:

* Name
* License Number
* Specialization
* Experience Years
* Hospital Name
* Village

### 🧑 Patient Module

* Register Patient
* Search Patient by:

  * ID
  * Name
  * Phone Number
* View all patients
* View patients created by logged-in user

Patient Information:

* Name
* Age
* Gender
* Phone
* Email
* Village
* Blood Group
* Disease

### 📅 Appointment Module

Patients can book appointments with doctors.

Features:

* Book Appointment
* Cancel Appointment
* Update Appointment Status
* View appointments by doctor
* Daily appointment limit validation

Appointment Lifecycle:

```
BOOKED → CONFIRMED → COMPLETED
      ↘
       CANCELLED
```

Appointment Fields:

* Patient
* Doctor
* Appointment Date
* Start Time
* Village
* Symptoms
* Status

# 🏗️ Project Architecture

```
Controller Layer
        ↓
Service Layer
        ↓
Repository Layer
        ↓
Database
```

### Layers Explained

**Controller**

* Handles REST API requests

**Service**

* Contains business logic

**Repository**

* Database interaction using Spring Data JPA

**Entity**

* Database table mapping

---

# 🗄️ Database Schema Overview

Main Entities:

```
User
 │
 ├── Patient
 │
 └── Doctor
       │
       └── Specialization (Enum)

Appointment
   │
   ├── Patient
   └── Doctor
```

---

# ⚙️ Technology Stack

| Technology      | Usage                      |
| --------------- | -------------------------- |
| Java            | Programming Language       |
| Spring Boot     | Backend Framework          |
| Spring Data JPA | Database ORM               |
| Hibernate       | ORM Implementation         |
| MySQL           | Database                   |
| Lombok          | Boilerplate code reduction |
| ModelMapper     | DTO Mapping                |
| SLF4J           | Logging                    |
| Maven           | Dependency Management      |

---

# 📦 API Modules

### Auth APIs

```
POST /auth/register
POST /auth/login
POST /auth/logout
```

### Patient APIs

```
POST /patients
GET /patients
GET /patients/{id}
GET /patients/search?name=
GET /patients/phone/{phoneNumber}
GET /patients/user
```

### Doctor APIs

```
POST /doctors
GET /doctors
GET /doctors/{id}
PUT /doctors/{id}
DELETE /doctors/{id}
```

### Appointment APIs

```
POST /appointments
GET /appointments
GET /appointments/doctor/{doctorId}
PATCH /appointments/{id}/status
PATCH /appointments/{id}/cancel
```

# 🔐 Logging & Monitoring

Application uses **SLF4J Logging**.

Example logs include:

* API request logs
* Controller execution tracking
* Exception logging

AOP is used to log controller execution:

```
@Around("execution(* com.ruralHealth..controller..*(..))")
```

# 🚀 Future Enhancements

* Doctor availability scheduling
* Medicine / Prescription module
* Patient medical history
* Notification system (SMS / Email)
* Dashboard analytics
* File upload for reports

---

# ▶️ Running the Application

### 1️⃣ Clone the Repository

```
git clone https://github.com/Patil1729/rural-health-system.git
```

### 2️⃣ Configure Database

Update `application.properties`

```
spring.datasource.url=jdbc:mysql://localhost:3306/rural_health
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3️⃣ Run the Application

```
mvn spring-boot:run
```

# 📖 Learning Objectives

This project demonstrates:

* REST API development
* Spring Boot architecture
* DTO mapping
* Exception handling
* Logging using AOP
* JPA relationships
* Clean code practices

# 👨‍💻 Author

Developed as part of backend practice and healthcare system design.

# ⭐ Contribution

Contributions are welcome!
Feel free to fork the repository and submit pull requests.
