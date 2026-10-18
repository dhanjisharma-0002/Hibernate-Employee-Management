package com.wipro.employeemanagement.controller;

import com.wipro.employeemanagement.dao.UserDAO;
import com.wipro.employeemanagement.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Controller servlet for Operation 3: Modify Personal Details.
 * 
 * Secure Profile Management:
 * - Retrieves the profile exclusively using the authenticated session's user ID.
 * - Prevents tampering: Ignored/prohibited any manipulated request parameter targeting another user's ID.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[0-9]{10}$");

    private UserDAO userDAO;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
    }

    /**
     * Displays personal details for the authenticated user only.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?authError=session_required");
            return;
        }

        // Strictly use session user ID - never trust request parameters
        Long sessionUserId = (Long) session.getAttribute("userId");
        User user = userDAO.getUserById(sessionUserId);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?authError=session_required");
            return;
        }

        request.setAttribute("profileUser", user);
        request.getRequestDispatcher("/WEB-INF/views/profile-edit.jsp").forward(request, response);
    }

    /**
     * Updates personal details (name, city, email, mobile) for the authenticated user only.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?authError=session_required");
            return;
        }

        // Security enforcement: strictly use session ID to prevent IDOR / tampering
        Long sessionUserId = (Long) session.getAttribute("userId");
        User user = userDAO.getUserById(sessionUserId);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?authError=session_required");
            return;
        }

        String name = request.getParameter("name");
        String city = request.getParameter("city");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        String trimmedName = name != null ? name.trim() : "";
        String trimmedCity = city != null ? city.trim() : "";
        String trimmedEmail = email != null ? email.trim().toLowerCase() : "";
        String trimmedMobile = mobile != null ? mobile.trim() : "";

        // Server-side validation
        if (trimmedName.isEmpty() || trimmedEmail.isEmpty()) {
            request.setAttribute("errorMessage", "Name and Email are mandatory fields.");
            request.setAttribute("profileUser", user);
            request.getRequestDispatcher("/WEB-INF/views/profile-edit.jsp").forward(request, response);
            return;
        }

        if (trimmedName.length() < 2 || trimmedName.length() > 100) {
            request.setAttribute("errorMessage", "Name must be between 2 and 100 characters.");
            request.setAttribute("profileUser", user);
            request.getRequestDispatcher("/WEB-INF/views/profile-edit.jsp").forward(request, response);
            return;
        }

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            request.setAttribute("errorMessage", "Please enter a valid email address.");
            request.setAttribute("profileUser", user);
            request.getRequestDispatcher("/WEB-INF/views/profile-edit.jsp").forward(request, response);
            return;
        }

        if (!trimmedMobile.isEmpty() && !MOBILE_PATTERN.matcher(trimmedMobile).matches()) {
            request.setAttribute("errorMessage", "Mobile number must be a valid 10-digit number.");
            request.setAttribute("profileUser", user);
            request.getRequestDispatcher("/WEB-INF/views/profile-edit.jsp").forward(request, response);
            return;
        }

        try {
            // Apply updates
            user.setName(trimmedName);
            user.setCity(trimmedCity);
            user.setEmail(trimmedEmail);
            user.setMobile(trimmedMobile);

            userDAO.updateUser(user);

            // Update session identity so header updates live
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("loggedInUser", user);

            String successMsg = URLEncoder.encode("Personal details updated successfully!", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/profile?successMessage=" + successMsg);

        } catch (Exception e) {
            System.err.println("[ProfileServlet] Error updating profile: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to update profile: " + e.getMessage());
            request.setAttribute("profileUser", user);
            request.getRequestDispatcher("/WEB-INF/views/profile-edit.jsp").forward(request, response);
        }
    }
}
