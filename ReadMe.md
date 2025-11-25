# DKKP Inventory & Management System - Comprehensive Technical Documentation

## 1. Project Overview & Architectural Vision

**DKKP** Is A Robust Desktop-Based Inventory Management & Point-Of-Sale Application Designed To Streamline Business Operations. Built On The **Java** Platform Using **JavaFX**, It Delivers A Modern, Responsive User Interface Powered By **MaterialFX**. The System Is Engineered To Manage Complex Product Hierarchies, User Authentication, & Transactional Data Through A Secure & Scalable Architecture.

The Application Follows A Strict **Model-View-Controller (MVC)** Design Pattern, Supplemented By A **Service-DAO** Layer To Ensure Separation Of Concerns. It Leverages **Hibernate (JPA)** For Object-Relational Mapping, Connecting Seamlessly To A **PostgreSQL** Database. This Architecture Prioritizes Data Integrity, Security, & Maintainability, Making It Suitable For Enterprise-Level Resource Planning.

---

## 2. Technology Stack & Infrastructure Decisions

### 2.1 Core Infrastructure

The Application Runs On The Java Virtual Machine (JVM), Utilizing Bleeding-Edge Features Of The Java Ecosystem.

* **Programming Language**: **Java 23**
    * **Strategic Rationale**: The Project Adopts Java 23, Enabling The Use Of The Latest Language Enhancements For Cleaner & More Efficient Code. The Compiler Configuration Targets Release 23 Explicitly.
* **Build Automation**: **Apache Maven**
    * **Implementation**: Maven Orchestrates The Build Lifecycle, Dependency Management, & Application Packaging. It Includes Plugins Like `maven-compiler-plugin` & `javafx-maven-plugin` To Streamline Development.
* **Database System**: **PostgreSQL**
    * **Deployment Strategy**: A Powerful Open-Source Object-Relational Database System. The Application Connects via Port `5432` To A Database Named `DKKP`, Ensuring Robust Data Persistence & Concurrency Support.
* **ORM Framework**: **Hibernate 6.6**
    * **Functionality**: Implements The Jakarta Persistence API (JPA). It Handles The Mapping Between Java Objects & Database Tables, Utilizing **HikariCP** For High-Performance Connection Pooling.

### 2.2 User Interface & Libraries

The Frontend Is A Rich Desktop Client Built With JavaFX.

* **GUI Framework**: **JavaFX 23**
    * **Architecture**: Provides A Hardware-Accelerated Graphics Layer. The UI Structure Is Defined In `.fxml` Files, Decoupling The Visual Layout From The Java Business Logic.
* **Component Library**: **MaterialFX**
    * **Design System**: Integrates Material Design Components Into JavaFX, Offering A Modern Look & Enhanced User Experience Over Standard Swing Or JavaFX Controls.
* **Security & Utilities**:
    * **JJWT (JSON Web Token)**: Included For Secure Token-Based Operations.
    * **Apache POI**: Integrated For Generating Excel Reports.

---

## 3. Detailed System Architecture & Design Patterns

### 3.1 Backend Layered Architecture

The Application Is Structured To Promote Modularity & Reusability.

* **Entity Layer**:
    * **Role**: Defines The Data Models Annotated With `@Entity` & `@Table`.
    * **Example**: `User_Entity` Maps To The `uzer` Table, Storing Credentials, Roles, & Personal Information.
* **DAO (Data Access Object) Layer**:
    * **Role**: Encapsulates Direct Database Interaction Using The `EntityManager`.
    * **Function**: `UserDao` Executes JPQL Queries To Find Users By Email, Validate Logins, & Update Records. It Abstracts SQL Complexity From The Business Logic.
* **Service Layer**:
    * **Role**: Contains Business Logic & Validation Rules.
    * **Function**: `UserService` Orchestrates Operations Like Login Verification, While `Validator` Ensures Input Integrity (E.G., Email Format Checks).

### 3.2 Frontend Controller Architecture

User Interaction Is Managed Via FXML Controllers.

* **Initialization**:
    * **Logic**: The `Main` Class Bootstraps The JavaFX Application, Sets Up The MaterialFX Themes, & Loads The Initial `LoginView`.
* **Event Handling**:
    * **Implementation**: Controllers Like `LoginController` Bind UI Events (Button Clicks) To Java Methods. They Handle Input Validation, Call Services, & Manage Scene Transitions via `ViewUtil`.

---

## 4. Comprehensive Database Schema Definition

The Database Schema Is Designed Using TypeORM Entities Within PostgreSQL.

### 4.1 User Management Table (`uzer`)
* **`ID_USER`**: Primary Key (String).
* **`EMAIL_ACC`**: Unique User Email Used For Login.
* **`PASSWORD_ACC`**: Hashed Password String.
* **`SALT`**: Cryptographic Salt For Password Security.
* **`ROLE_ACC`**: Defines User Permissions (E.G., Admin, Staff).
* **`DATE_JOIN`**: Timestamp Of User Registration.

### 4.2 Product & Inventory Tables (Inferred)
* **`Product_Base_Entity`**: Represents The Core Product Definition.
* **`Product_Final_Entity`**: Represents Specific Product Instances.
* **`Category_Entity`**: Organizes Products Into Hierarchies.
* **`Brand_Entity`**: Manages Product Brand Associations.
* **`Import_Entity` / `Import_Detail_Entity`**: Tracks Stock Inflow Transactions.
* **`Bill_Entity` / `Bill_Detail_Entity`**: Records Sales & Customer Transactions.

---

## 5. Core Functionalities & Security

### 5.1 Authentication & Security
* **Login System**: Validates Credentials Against The Database Using `UserDao`. It Checks For Valid Email Formats Before Attempting Authentication.
* **Cryptography**: Includes A `SecurityFunction` Service To Handle Password Hashing & Salting, Ensuring Sensitive Data Is Never Stored In Plain Text. The `User_Entity` Explicitly Stores A `SALT` Column.

### 5.2 Validation & Error Handling
* **Input Validation**: The `Validator` Utility Checks Inputs (Like Email Regex) Before Processing.
* **User Feedback**: The UI Uses `Alert` Dialogs To Inform Users Of Input Errors, Invalid Credentials, Or System Failures.

---

## 6. Installation & Development Setup

### 6.1 Prerequisites
* **Java JDK**: Version 23 Required.
* **PostgreSQL**: Local Instance Running On Port 5432.
* **Maven**: For Project Build & Dependency Resolution.

### 6.2 Configuration
1.  **Database Setup**: Create A PostgreSQL Database Named `DKKP`.
2.  **Persistence Config**: Update `src/main/resources/META-INF/persistence.xml` With Valid Database Credentials (`user`, `password`). The Default Is Set To User `postgres` & Password `0`.

### 6.3 Build & Run
1.  **Compile**:
    ```bash
    mvn clean compile
    ```
2.  **Run Application**:
    ```bash
    mvn javafx:run
    ```

---

## 7. Project Directory Structure

* **`src/main/java/com/example/dkkp/`**:
    * **`Main.java`**: Application Entry Point.
    * **`controller/`**: UI Logic For Views (Login, Products, Bills, Imports).
    * **`dao/`**: Database Access Layer (UserDao, ProductDao).
    * **`model/`**: JPA Entities Representing Database Tables.
    * **`service/`**: Business Logic (Security, Validation).
    * **`util/`**: Helper Classes (ViewUtil).
    * **`view/`**: View Loading Classes.
* **`src/main/resources/`**:
    * **`com/example/dkkp/*.fxml`**: UI Layout Definitions.
    * **`META-INF/persistence.xml`**: Database Connection Configuration.
    * **`css/`**: Stylesheets (DKKP.css).
    * **`images/`**: Assets (DKKP.png).

---

## 8. License

This Project Is Proprietary Software. Unauthorized Distribution Or Modification Is Prohibited.
