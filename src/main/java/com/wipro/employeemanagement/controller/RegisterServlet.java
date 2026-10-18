package com.wipro.employeemanagement.controller;

import com.wipro.employeemanagement.dao.UserDAO;
import com.wipro.employeemanagement.model.User;
import com.wipro.employeemanagement.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Controller servlet handling user registration.
 * Compatible with Jakarta EE 10 & Apache Tomcat 10.1 / 11.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Pattern: 1 alphabet followed by exactly 4 digits (e.g. A1234)
    private static final Pattern EMP_NO_PATTERN = Pattern.compile("^[A-Za-z][0-9]{4}$");

    // Standard email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private UserDAO userDAO;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
    }

    /**
     * Handles GET requests by forwarding to the registration page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    /**
     * Handles POST requests for submitting registration form data.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String employeeNumber = request.getParameter("employeeNumber");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Trim values for clean handling (preserving password as-is)
        String trimmedName = name != null ? name.trim() : "";
        String trimmedEmpNo = employeeNumber != null ? employeeNumber.trim().toUpperCase() : "";
        String trimmedEmail = email != null ? email.trim().toLowerCase() : "";

        // Retain entered values to repopulate the form if validation fails
        request.setAttribute("enteredName", trimmedName);
        request.setAttribute("enteredEmpNo", trimmedEmpNo);
        request.setAttribute("enteredEmail", trimmedEmail);

        // =====================================================================
        // Server-Side Validations
        // =====================================================================

        // 1. Mandatory Fields Check
        if (trimmedName.isEmpty() || trimmedEmpNo.isEmpty() || trimmedEmail.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "All fields are mandatory. Please fill in all details.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 2. Name Length Check
        if (trimmedName.length() < 2 || trimmedName.length() > 100) {
            request.setAttribute("errorMessage", "Name must be between 2 and 100 characters.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 3. Employee Number Pattern Check (^ [A-Za-z][0-9]{4} $)
        if (!EMP_NO_PATTERN.matcher(trimmedEmpNo).matches()) {
            request.setAttribute("errorMessage", "Employee Number must start with 1 letter followed by exactly 4 digits (e.g., A1234).");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 4. Password Exactly 6 Characters Check
        if (!PasswordUtil.isValidAssignmentPassword(password)) {
            request.setAttribute("errorMessage", "Password must be exactly 6 characters long.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 5. Email Format Check
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            request.setAttribute("errorMessage", "Please provide a valid email address (e.g., user@example.com).");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 6. Database Uniqueness Check - Employee Number
        try {
            if (userDAO.isEmployeeNumberExists(trimmedEmpNo)) {
                request.setAttribute("errorMessage", "Employee Number '" + trimmedEmpNo + "' is already registered. Please proceed to Login or use a different number.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // 7. Database Uniqueness Check - Email
            if (userDAO.isEmailExists(trimmedEmail)) {
                request.setAttribute("errorMessage", "Email address '" + trimmedEmail + "' is already registered. Please proceed to Login or use a different email.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // =================================================================
            // Secure Password Hashing & Persistence
            // =================================================================
            String hashedPassword = PasswordUtil.hashPassword(password);

            User newUser = new User(trimmedName, trimmedEmpNo, trimmedEmail, hashedPassword);
            userDAO.saveUser(newUser);

            // Clear retained values on success
            request.removeAttribute("enteredName");
            request.removeAttribute("enteredEmpNo");
            request.removeAttribute("enteredEmail");

            request.setAttribute("successMessage", "Registration successful for Employee " + trimmedEmpNo + "! You can now log in with your credentials.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[RegisterServlet] Error saving user: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database registration failed: " + e.getMessage() + ". Please check database connection in db.properties.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
