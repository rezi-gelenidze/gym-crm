# **Gym CRM System**
**A Spring-based Gym CRM system that handles trainees, trainers, and training sessions using a layered architecture.**

## **📌 Overview**
This project is a **Spring Core-based CRM system** designed to manage **trainees, trainers, and training sessions**. It follows a **layered architecture** with **in-memory storage** to mimic a relational database structure.

The system implements the following functionalities:
- **Trainee Management**: Create, update, delete, and fetch trainees.
- **Trainer Management**: Create, update, and fetch trainers.
- **Training Management**: create and retrieve training sessions.
- **User Credential Management**: Generates **unique usernames**, **randomized passwords**, and **sequential user IDs**.

---

## **📜 Requirements**
This project was implemented based on the following requirements:
1. **Spring-based module** for a Gym CRM system.
2. **Service classes** for handling:
    - **Trainee Service**: CRUD operations for trainees.
    - **Trainer Service**: CRU operations for trainers.
    - **Training Service**: CR operations for training sessions.
3. **In-memory storage (`MemoryStorage`)** using `Map` for storing entities.
4. **DAO Layer**:
    - Separate DAOs for `Trainee`, `Trainer`, and `Training`.
5. **Username and Password Management**:
    - Username format: `"FirstName.LastName"`, with incremental suffix (`John.Doe`, `John.Doe1`).
    - Password: **Random 10-character**.
6. **Spring Context Configuration** using `@ComponentScan` (No Spring Boot).
7. **Comprehensive testing**:
    - **Unit tests for DAO & Services**.
    - **Integration tests for `GymFacade`**.

---

## **🛠 Project Structure**
This project is structured using **layered architecture**, ensuring **separation of concerns**.

```
src/main/java/io/github/rezi_gelenidze/gym_crm/
 ├── config/             # Spring context configuration (AppConfig)
 ├── dao/                # Data Access Objects (TraineeDao, TrainerDao, TrainingDao, UserDao)
 ├── entity/             # Core domain models (Trainee, Trainer, Training, TrainingType, User)
 ├── facade/             # Facade layer to expose a high-level API for Gym CRM
 ├── service/            # Business logic layer (TraineeService, TrainerService, TrainingService)
 ├── storage/            # In-memory storage (MemoryStorage)
 ├── Runner.java        # Application entry point
```

---

## **📌 Key Design Decisions**

### **1️⃣ Vertical Partitioning with `User` DTO**
Instead of duplicating fields like `userId`, `username`, and `password` inside `Trainee` and `Trainer`, we use a **shared `User` object**.  
This **mimics SQL vertical partitioning**, where a **User table** stores shared fields, while `Trainee` and `Trainer` extend it.

✔ **Reduces code duplication**.  
✔ **Separates business logic from user credential generation**.  
✔ **Mimics relational database structure while using in-memory storage**.

```java
public class Trainee {
    private User user;   // Shared user properties
    private String dateOfBirth;
    private String address;
}
```

---

### **2️⃣ Centralized User Management with `UserDao`**
✔ **Ensures all user-related logic is in one place**.  
✔ Handles:
- **Unique username generation** across trainees and trainers.
- **Secure random password generation**.
- **Auto-incrementing user ID generation**.

```java
import lombok.Setter;

public class UserDao {
    public User createUser(String firstName, String lastName) { ...}
    protected String generateUsername(String firstName, String lastName) { ...}
    protected Long generateUserId() { ... }
    protected String generatePassword() { ...}  // Generates a 10-char secure random password
}
```

---

### **3️⃣ `GymFacade` as a Single Entry Point**
✔ **Combines multiple services into a single, easy-to-use API**.  
✔ **Simplifies service calls** (e.g., register a trainee, trainer, and schedule training in one method).

```java
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    ...AutoWire via Constructor...
    
    public Trainee createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
        return traineeService.createTrainee(firstName, lastName, dateOfBirth, address);
    }

    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        return trainerService.createTrainer(firstName, lastName, specialization);
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName, TrainingType trainingType, String trainingDate, Duration duration) {
        return trainingService.createTraining(traineeId, trainerId, trainingName, trainingType, trainingDate, duration);
    }
    
    ...other methods wrapped...
}
```

---

## **🧪 Testing**
The project includes **comprehensive tests** covering **CRUD operations, user field generation, and facade-level integration**.

✔ **`testCRUDTrainee`** (Ensures trainee CRUD operations work).  
✔ **`testCRUTrainer`** (Ensures trainer CRU operations work).  
✔ **`testCRTraining`** (Ensures training CR operations work).
✔ **`testUserFieldsGeneration`** (Ensures **username uniqueness**, **password randomness**, **ID generation** correctness).


## **📌 Summary**
✅ **Spring Core-based Gym CRM system** using **layered architecture**.  
✅ **Separates `User` DTO to mimic vertical partitioning & separate credential logic**.  
✅ **DAO, Service, and Facade layers ensure a clean, maintainable structure**.  
✅ **Tests validate correctness & reliability**.
