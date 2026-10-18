# Hibernate Employee Management Web Application

A complete, production-ready, beginner-friendly Employee Management Web Application built for MCA coursework using **Java 17**, **Jakarta Servlet 6.0**, **JSP**, **Hibernate ORM 6**, and **MySQL 8.0**, running on **Apache Tomcat 10.1** (or compatible Tomcat 11).

---

## 📌 Architecture & Technology Stack

| Component | Technology / Specification | Role in Application |
|---|---|---|
| **Language** | Java 17 LTS (Compiled with `--release 17`, runs on Java 17 to 25) | Core logic & backend |
| **Servlet Specification** | Jakarta Servlet 6.0 (`jakarta.servlet.*`) | Controller layer, routing, session lifecycle |
| **Presentation Tier** | JSP 3.1 & Jakarta JSTL 3.0 | Dynamic HTML rendering, EL expressions |
| **ORM Framework** | Hibernate ORM 6 (`6.6.15.Final`) | Object-Relational Mapping, HQL, transactions |
| **Relational Database** | MySQL Server 8.0 | Relational database storage |
| **Database Driver** | MySQL Connector/J (`8.3.0`) | JDBC communication |
| **Password Security** | jBCrypt (`0.4`) | Secure salt generation and BCrypt hashing |
| **Application Server** | Apache Tomcat 10.1 / 11.0 | Jakarta EE 10 compliant servlet container |
| **Build & Dependency Tool** | Apache Maven 3.9+ (Maven Wrapper included) | Project lifecycle, automated testing, WAR packaging |

---

## 📁 Project Directory Structure

```text
Hibernate/
├── .mvn/                                # Maven wrapper configuration
├── pom.xml                              # Maven build file with Jakarta EE 10 & Hibernate 6 dependencies
├── mvnw / mvnw.cmd                      # Cross-platform Maven wrapper scripts
├── README.md                            # Comprehensive project guide
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── wipro/
    │   │           └── employeemanagement/
    │   │               ├── controller/  # RegisterServlet, LoginServlet, LogoutServlet, WelcomeServlet, EmployeeServlet, ProfileServlet
    │   │               ├── dao/         # UserDAO, EmployeeDAO (Hibernate 6 transactions & queries)
    │   │               ├── filter/      # AuthenticationFilter (Session security & no-cache headers)
    │   │               ├── model/       # User & Employee entities (MySQL AUTO_INCREMENT)
    │   │               └── util/        # HibernateUtil (SessionFactory) & PasswordUtil (BCrypt)
    │   ├── resources/
    │   │   ├── db.properties            # Configurable database credentials
    │   │   └── hibernate.cfg.xml        # Hibernate ORM 6 configuration & entity mappings
    │   └── webapp/
    │       ├── css/
    │       │   └── style.css            # Clean, modern, responsive stylesheet
    │       ├── js/
    │       │   └── validation.js        # Client-side form validations & live feedback
    │       ├── WEB-INF/
    │       │   ├── views/               # Protected views: welcome, employee-list, employee-form, employee-search, employee-delete-confirm, profile-edit
    │       │   └── web.xml              # Deployment descriptor (Servlet 6.0, session config, error pages)
    │       ├── index.jsp                # Public Landing / Home page
    │       ├── login.jsp                # Public Login page
    │       ├── register.jsp             # Public Registration page
    │       └── error.jsp                # Generic error page (prevents stack trace leaks)
    └── test/
        └── java/
            └── com/
                └── wipro/
                    └── employeemanagement/
                        ├── controller/  # RegisterServletTest, LoginAndSessionSecurityTest, EmployeeManagementTest
                        └── util/        # PasswordUtilTest
```

---

## ⚙️ Step-by-Step Setup Guide

### 1. Database Configuration (MySQL)

1. **Verify MySQL Server**:
   * Open Windows Services (`services.msc`) and verify **MySQL80** is in `Running` status.
2. **Create the Database**:
   * Open MySQL Command Line Client or MySQL Workbench:
     ```sql
     CREATE DATABASE IF NOT EXISTS employee_management_db;
     ```
3. **Configure Your Credentials**:
   * Open [src/main/resources/db.properties](file:///c:/Users/DELL/OneDrive/Desktop/practice%20project/wipro%20project/Hibernate/src/main/resources/db.properties).
   * Update line 16 with your local MySQL password:
     ```properties
     db.url=jdbc:mysql://localhost:3306/employee_management_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true
     db.username=root
     db.password=YOUR_LOCAL_MYSQL_PASSWORD
     ```
   * Hibernate is set to `db.hbm2ddl.auto=update`, which automatically creates the `users` and `employees` tables on first launch.

---

### 2. How to Import and Run in Eclipse IDE

1. **Open Eclipse**:
   * Launch **Eclipse IDE for Enterprise Java and Web Developers**.
2. **Import Project**:
   * Click `File` ➔ `Import...`
   * Select `Maven` ➔ `Existing Maven Projects` ➔ Click `Next`.
   * In **Root Directory**, browse to:
     `c:\Users\DELL\OneDrive\Desktop\practice project\wipro project\Hibernate`
   * Ensure `pom.xml` is checked and click `Finish`.
   * Wait for Eclipse to resolve dependencies.
3. **Configure Apache Tomcat**:
   * Open the `Servers` tab at the bottom of Eclipse.
   * Click `New` ➔ `Server` ➔ Select `Apache` ➔ `Tomcat v10.1 Server` (or `Tomcat v11.0 Server`).
   * Point Tomcat installation directory to your local Tomcat (e.g., `C:\Program Files\Apache Software Foundation\Tomcat 11.0_Tomcat11.0` or Tomcat 10.1).
4. **Run Application**:
   * Right-click the project `hibernate-employee-management` ➔ `Run As` ➔ `Run on Server`.
   * Select Tomcat server ➔ Click `Finish`.
   * Browser will open:
     `http://localhost:8080/HibernateEmployeeManagement/`

---

### 3. How to Build & Test via Command Line

The project includes Maven Wrapper (`mvnw.cmd`), so Maven does not need to be manually configured in your Windows PATH:

```powershell
# Navigate to the workspace directory
cd "c:\Users\DELL\OneDrive\Desktop\practice project\wipro project\Hibernate"

# Run all 27 automated unit tests
.\mvnw.cmd clean test

# Build deployable WAR file
.\mvnw.cmd clean package
```

The compiled WAR file is generated at:
`target/HibernateEmployeeManagement.war`

---

## 🌐 Application URLs & Test Menu

| # | Feature | URL | HTTP Method | Access Level | Description |
|---|---|---|---|---|---|
| 1 | **Home Page** | `/index.jsp` | `GET` | Public | Landing page with Portal overview and links to Login & Register |
| 2 | **User Registration** | `/register.jsp` / `/register` | `GET` / `POST` | Public | Form for Name, Emp No (`^[A-Za-z][0-9]{4}$`), Email, Password (exactly 6 chars). Hashes password with BCrypt |
| 3 | **User Login** | `/login.jsp` / `/login` | `GET` / `POST` | Public | Authenticates credentials, creates session (`userId`, `userName`, etc.), redirects to `/welcome` |
| 4 | **Dashboard** | `/welcome` | `GET` | **Protected** | Personalized welcome banner (*"Welcome Ram"*) and quick-access cards to all 5 CRUD operations |
| 5 | **Add Employee** | `/employee?action=add` | `GET` / `POST` | **Protected** | Form to register employee with auto-generated ID, salary, gender, designation, city, email, mobile |
| 6 | **All Employees** | `/employee?action=list` | `GET` | **Protected** | Tabular directory of all employee records with formatted currency and action buttons |
| 7 | **Search by ID** | `/employee?action=search` | `GET` | **Protected** | Lookup employee details by numeric ID; displays record or not-found alert |
| 8 | **Delete Employee** | `/employee?action=deleteConfirm&id=1` | `GET` / `POST` | **Protected** | Previews full employee record details and deletes **only after explicit confirmation** |
| 9 | **Modify Profile** | `/profile` | `GET` / `POST` | **Protected** | Updates Name, City, Email, Mobile using **session user ID only** (prevents tampering / IDOR) |
| 10 | **Logout** | `/logout` | `GET` / `POST` | Public | Invalidates session, sets `no-cache` headers, and redirects to `login.jsp?logout=true` |

---

## 🧪 Comprehensive Test Scenarios Verified

| Test Case | Inputs / Action | Expected Result | Verified Status |
|---|---|---|---|
| **TC-01: Home Page** | Access `/index.jsp` | Page loads cleanly with navigation links | **PASS** |
| **TC-02: Valid Registration** | Name: `Ram Sharma`, Emp No: `A1234`, Password: `secret`, Email: `ram@example.com` | User saved in MySQL with BCrypt hashed password; shows success alert and login button | **PASS** |
| **TC-03: Registration Empty Fields** | Blank Name / Emp No / Email / Password | Rejects with *"All fields are mandatory"* | **PASS** |
| **TC-04: Invalid Emp No Format** | Emp No: `12345` or `ABC12` | Rejects with *"Must start with 1 letter followed by exactly 4 digits"* | **PASS** |
| **TC-05: Invalid Password Length** | Password: `12345` (5 chars) or `1234567` (7 chars) | Rejects with *"Password must be exactly 6 characters long"* | **PASS** |
| **TC-06: Duplicate Emp No** | Emp No: `A1234` when already registered | Rejects with *"Employee Number 'A1234' is already registered"* | **PASS** |
| **TC-07: Login Missing Fields** | Blank username or blank password | Rejects with *"Please enter both username/employee number and password"* | **PASS** |
| **TC-08: Login Nonexistent User** | Username: `Z9999` | Rejects with *"No account found with employee number/username 'Z9999'"* | **PASS** |
| **TC-09: Login Wrong Password** | Username: `A1234`, Password: `wrongP` | Rejects with *"Incorrect password. Please verify and try again"* | **PASS** |
| **TC-10: Login Success** | Correct credentials | Creates authenticated HTTP session and redirects to `/welcome` displaying *"Welcome Ram"* | **PASS** |
| **TC-11: Direct URL Protection** | Access `/welcome`, `/employee?action=list`, `/profile` in incognito without login | `AuthenticationFilter` intercepts and redirects to `/login.jsp?authError=session_required` | **PASS** |
| **TC-12: Logout Session Invalidation** | Click Logout | Invalidates session, clears cache headers, redirects to `login.jsp?logout=true` | **PASS** |
| **TC-13: Add Employee Auto-Increment** | Name: `Suresh`, Gender: `Male`, Desig: `Developer`, Sal: `50000`, City: `Pune`, Email: `suresh@co.com`, Mobile: `9876543210` | Auto-assigns MySQL auto-increment ID; redirects to directory with success notice | **PASS** |
| **TC-14: Delete Confirmation** | Click Delete on Employee #1 | Shows complete details preview page; deletes record only after clicking *"Yes, Delete"* | **PASS** |
| **TC-15: Modify Personal Details** | Edit Name: `Ram Kumar`, City: `Noida`, Mobile: `9876543211` | Strictly uses `session.getAttribute("userId")` to prevent IDOR; updates DB and active session | **PASS** |
| **TC-16: Search Employee by ID** | Enter ID: `1` | Displays matching employee record card; displays *"No employee found"* if not found | **PASS** |
| **TC-17: Select All Employees** | Access `/employee?action=list` | Displays clean table of all employees with formatted currency and action buttons | **PASS** |
| **TC-18: Generic Error Page** | Force 404 or 500 error | Handled via [error.jsp](file:///c:/Users/DELL/OneDrive/Desktop/practice%20project/wipro%20project/Hibernate/src/main/webapp/error.jsp); no stack traces or DB details leaked | **PASS** |

---

## 🔧 Troubleshooting Guide

* **MySQL Access Denied (`ERROR 1045`)**:
  * Check your password in [src/main/resources/db.properties](file:///c:/Users/DELL/OneDrive/Desktop/practice%20project/wipro%20project/Hibernate/src/main/resources/db.properties). Ensure `db.password` matches your local MySQL `root` user password.
* **Port 8080 Already in Use**:
  * In Eclipse, double-click the configured Tomcat server, change the HTTP port from `8080` to `8081` or `8082`, and save.
* **Database `employee_management_db` Does Not Exist**:
  * Ensure you created the database in MySQL: `CREATE DATABASE IF NOT EXISTS employee_management_db;`.
* **Changes in JSP or Java Not Reflecting**:
  * In Eclipse, right-click the server in the `Servers` tab ➔ click `Clean...` ➔ `Clean Tomcat Work Directory...` ➔ restart server.
