package com.wipro.employeemanagement.controller;

import com.wipro.employeemanagement.dao.UserDAO;
import com.wipro.employeemanagement.model.User;
import com.wipro.employeemanagement.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller servlet for handling user authentication and session creation.
 * Compatible with Jakarta EE 10 & Apache Tomcat 10.1 / 11.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
    }

    /**
     * Handles GET requests: displays the login page or redirects to welcome page if already authenticated.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect(request.getContextPath() + "/welcome");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Handles POST requests: authenticates user credentials, sets session, and redirects to welcome page.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String trimmedUsername = username != null ? username.trim() : "";
        request.setAttribute("enteredUsername", trimmedUsername);

        // 1. Validate missing credentials
        if (trimmedUsername.isEmpty() && (password == null || password.isEmpty())) {
            request.setAttribute("errorMessage", "Please enter both username/employee number and password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (trimmedUsername.isEmpty()) {
            request.setAttribute("errorMessage", "Please enter your employee number or username.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Please enter your password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            // 2. Query user from database via Hibernate DAO
            User user = userDAO.getUserByUsernameOrEmployeeNumber(trimmedUsername);

            // 3. Check for nonexistent user
            if (user == null) {
                request.setAttribute("errorMessage", "No account found with employee number/username '" + trimmedUsername + "'. Please check your credentials or register.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            // 4. Verify password against BCrypt hash
            boolean isPasswordCorrect = PasswordUtil.checkPassword(password, user.getPasswordHash());

            if (!isPasswordCorrect) {
                request.setAttribute("errorMessage", "Incorrect password. Please verify and try again.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            // 5. Successful Authentication: Create fresh HTTP session
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("employeeNumber", user.getEmployeeNumber());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("loggedInUser", user);

            // 6. Redirect to personalized welcome page (Post/Redirect/Get pattern)
            response.sendRedirect(request.getContextPath() + "/welcome");

        } catch (Exception e) {
            System.err.println("[LoginServlet] Unexpected error during authentication: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred during authentication. Please verify database connectivity in db.properties.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
