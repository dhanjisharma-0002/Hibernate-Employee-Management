package com.wipro.employeemanagement.controller;

import com.wipro.employeemanagement.dao.EmployeeDAO;
import com.wipro.employeemanagement.model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Controller servlet managing all Employee CRUD operations:
 * 1. Add Employee (GET/POST)
 * 2. Delete Employee with confirmation (GET/POST)
 * 3. Select Employee by ID (GET)
 * 4. Select All Employees (GET)
 */
@WebServlet(name = "EmployeeServlet", urlPatterns = {"/employee"})
public class EmployeeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[0-9]{10}$");

    private EmployeeDAO employeeDAO;

    @Override
    public void init() {
        this.employeeDAO = new EmployeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?authError=session_required");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        switch (action) {
            case "add":
                showAddForm(request, response);
                break;
            case "search":
                searchEmployeeById(request, response);
                break;
            case "delete":
            case "deleteConfirm":
                showDeleteConfirmation(request, response);
                break;
            case "list":
            default:
                listEmployees(request, response);
                break;
        }
    }

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

        String action = request.getParameter("action");
        if ("add".equalsIgnoreCase(action)) {
            processAddEmployee(request, response);
        } else if ("delete".equalsIgnoreCase(action)) {
            processDeleteEmployee(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/employee?action=list");
        }
    }

    /**
     * Operation 5: Select All Employees
     */
    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Employee> employeeList = employeeDAO.getAllEmployees();
        request.setAttribute("employeeList", employeeList);
        request.getRequestDispatcher("/WEB-INF/views/employee-list.jsp").forward(request, response);
    }

    /**
     * Operation 1: Show Add Employee Form
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
    }

    /**
     * Operation 1: Process Add Employee Form Submission
     */
    private void processAddEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        String designation = request.getParameter("designation");
        String salaryStr = request.getParameter("salary");
        String city = request.getParameter("city");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        String trimmedName = name != null ? name.trim() : "";
        String trimmedGender = gender != null ? gender.trim() : "";
        String trimmedDesignation = designation != null ? designation.trim() : "";
        String trimmedCity = city != null ? city.trim() : "";
        String trimmedEmail = email != null ? email.trim() : "";
        String trimmedMobile = mobile != null ? mobile.trim() : "";

        // Retain values on error
        request.setAttribute("enteredName", trimmedName);
        request.setAttribute("enteredGender", trimmedGender);
        request.setAttribute("enteredDesignation", trimmedDesignation);
        request.setAttribute("enteredSalary", salaryStr != null ? salaryStr.trim() : "");
        request.setAttribute("enteredCity", trimmedCity);
        request.setAttribute("enteredEmail", trimmedEmail);
        request.setAttribute("enteredMobile", trimmedMobile);

        // Validations
        if (trimmedName.isEmpty() || trimmedGender.isEmpty() || trimmedDesignation.isEmpty() ||
                salaryStr == null || salaryStr.trim().isEmpty() ||
                trimmedCity.isEmpty() || trimmedEmail.isEmpty() || trimmedMobile.isEmpty()) {
            request.setAttribute("errorMessage", "All employee fields are mandatory.");
            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
            return;
        }

        Double salary = null;
        try {
            salary = Double.parseDouble(salaryStr.trim());
            if (salary < 0) {
                request.setAttribute("errorMessage", "Salary must be a positive number.");
                request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid salary format. Please enter a valid numeric value.");
            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
            return;
        }

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            request.setAttribute("errorMessage", "Invalid email address format.");
            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
            return;
        }

        if (!MOBILE_PATTERN.matcher(trimmedMobile).matches()) {
            request.setAttribute("errorMessage", "Mobile number must be exactly 10 digits.");
            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
            return;
        }

        try {
            Employee newEmp = new Employee(trimmedName, trimmedGender, trimmedDesignation, salary, trimmedCity, trimmedEmail, trimmedMobile);
            employeeDAO.saveEmployee(newEmp);

            String msg = URLEncoder.encode("Employee '" + trimmedName + "' added successfully with generated ID: " + newEmp.getId(), StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/employee?action=list&successMessage=" + msg);

        } catch (Exception e) {
            System.err.println("[EmployeeServlet] Error adding employee: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to save employee: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
        }
    }

    /**
     * Operation 4: Select Employee by ID
     */
    private void searchEmployeeById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                Long id = Long.parseLong(idStr.trim());
                Employee emp = employeeDAO.getEmployeeById(id);
                if (emp != null) {
                    request.setAttribute("searchedEmployee", emp);
                } else {
                    request.setAttribute("errorMessage", "No employee found with ID: " + id);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid Employee ID format. Please enter a valid number.");
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/employee-search.jsp").forward(request, response);
    }

    /**
     * Operation 2: Show confirmation page with employee details before deleting
     */
    private void showDeleteConfirmation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                Long id = Long.parseLong(idStr.trim());
                Employee emp = employeeDAO.getEmployeeById(id);
                if (emp != null) {
                    request.setAttribute("employeeToDelete", emp);
                } else {
                    request.setAttribute("errorMessage", "Employee not found with ID: " + id);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid Employee ID format. Please enter a valid numeric value.");
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/employee-delete-confirm.jsp").forward(request, response);
    }

    /**
     * Operation 2: Process confirmed deletion of an Employee
     */
    private void processDeleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/employee?action=list");
            return;
        }

        try {
            Long id = Long.parseLong(idStr.trim());
            boolean deleted = employeeDAO.deleteEmployee(id);
            if (deleted) {
                String msg = URLEncoder.encode("Employee ID " + id + " has been successfully deleted.", StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/employee?action=list&successMessage=" + msg);
            } else {
                String error = URLEncoder.encode("Cannot delete: No employee found with ID " + id, StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath() + "/employee?action=list&errorMessage=" + error);
            }
        } catch (Exception e) {
            System.err.println("[EmployeeServlet] Error deleting employee: " + e.getMessage());
            e.printStackTrace();
            String error = URLEncoder.encode("Deletion failed: " + e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/employee?action=list&errorMessage=" + error);
        }
    }
}
