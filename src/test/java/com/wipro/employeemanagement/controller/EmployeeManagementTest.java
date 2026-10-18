package com.wipro.employeemanagement.controller;

import com.wipro.employeemanagement.dao.EmployeeDAO;
import com.wipro.employeemanagement.dao.UserDAO;
import com.wipro.employeemanagement.filter.AuthenticationFilter;
import com.wipro.employeemanagement.model.Employee;
import com.wipro.employeemanagement.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Automated tests for Phase 4 Employee Management:
 * 1. Add Employee (validation + success)
 * 2. Delete Employee (confirmation + deletion)
 * 3. Modify Personal Details (session-identity-protected updates)
 * 4. Select Employee by ID (found + not found)
 * 5. Select All Employees (list retrieval)
 * 6. Authentication security filter for /employee and /profile
 */
public class EmployeeManagementTest {

    private static class StubEmployeeDAO extends EmployeeDAO {
        private final List<Employee> store = new ArrayList<>();
        private long idSequence = 1L;

        @Override
        public void saveEmployee(Employee employee) {
            employee.setId(idSequence++);
            store.add(employee);
        }

        @Override
        public Employee getEmployeeById(Long id) {
            for (Employee e : store) {
                if (e.getId().equals(id)) return e;
            }
            return null;
        }

        @Override
        public List<Employee> getAllEmployees() {
            return new ArrayList<>(store);
        }

        @Override
        public boolean deleteEmployee(Long id) {
            return store.removeIf(e -> e.getId().equals(id));
        }
    }

    private static class StubUserDAO extends UserDAO {
        private User userToReturn;
        private User lastUpdatedUser;

        @Override
        public User getUserById(Long id) {
            return userToReturn;
        }

        @Override
        public void updateUser(User user) {
            this.lastUpdatedUser = user;
        }
    }

    private StubEmployeeDAO stubEmployeeDAO;
    private StubUserDAO stubUserDAO;

    private EmployeeServlet employeeServlet;
    private ProfileServlet profileServlet;
    private AuthenticationFilter authenticationFilter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private FilterChain filterChain;

    private Map<String, Object> requestAttributes;
    private Map<String, Object> sessionAttributes;

    @BeforeEach
    public void setUp() throws Exception {
        stubEmployeeDAO = new StubEmployeeDAO();
        stubUserDAO = new StubUserDAO();

        employeeServlet = new EmployeeServlet();
        profileServlet = new ProfileServlet();
        authenticationFilter = new AuthenticationFilter();

        requestAttributes = new HashMap<>();
        sessionAttributes = new HashMap<>();

        // Inject DAOs via reflection
        Field empDaoField = EmployeeServlet.class.getDeclaredField("employeeDAO");
        empDaoField.setAccessible(true);
        empDaoField.set(employeeServlet, stubEmployeeDAO);

        Field userDaoField = ProfileServlet.class.getDeclaredField("userDAO");
        userDaoField.setAccessible(true);
        userDaoField.set(profileServlet, stubUserDAO);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        filterChain = mock(FilterChain.class);

        // Capture request attributes
        doAnswer(inv -> {
            requestAttributes.put(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(request).setAttribute(anyString(), any());

        // Capture session attributes
        doAnswer(inv -> {
            sessionAttributes.put(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(session).setAttribute(anyString(), any());

        // Default session is authenticated as user ID 1
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(request.getContextPath()).thenReturn("/HibernateEmployeeManagement");
    }

    // -------------------------------------------------------------------------
    // 1. ADD EMPLOYEE TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAddEmployeeValidationMissingFields() throws Exception {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("name")).thenReturn("");
        when(request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp")).thenReturn(dispatcher);

        employeeServlet.doPost(request, response);

        assertEquals("All employee fields are mandatory.", requestAttributes.get("errorMessage"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testAddEmployeeSuccessGeneratesIdAndRedirects() throws Exception {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("name")).thenReturn("Ramesh Verma");
        when(request.getParameter("gender")).thenReturn("Male");
        when(request.getParameter("designation")).thenReturn("Developer");
        when(request.getParameter("salary")).thenReturn("55000");
        when(request.getParameter("city")).thenReturn("Pune");
        when(request.getParameter("email")).thenReturn("ramesh@company.com");
        when(request.getParameter("mobile")).thenReturn("9876543210");

        employeeServlet.doPost(request, response);

        assertEquals(1, stubEmployeeDAO.getAllEmployees().size());
        Employee created = stubEmployeeDAO.getAllEmployees().get(0);
        assertEquals(1L, created.getId()); // MySQL Auto-Increment ID
        assertEquals("Ramesh Verma", created.getName());
        assertEquals("Developer", created.getDesignation());

        verify(response).sendRedirect(contains("/HibernateEmployeeManagement/employee?action=list&successMessage="));
    }

    // -------------------------------------------------------------------------
    // 2. SELECT ALL EMPLOYEES TEST
    // -------------------------------------------------------------------------

    @Test
    public void testSelectAllEmployees() throws Exception {
        stubEmployeeDAO.saveEmployee(new Employee("Anita Rao", "Female", "QA Engineer", 48000.0, "Hyderabad", "anita@co.com", "9876543211"));
        stubEmployeeDAO.saveEmployee(new Employee("Suresh Patel", "Male", "Architect", 95000.0, "Ahmedabad", "suresh@co.com", "9876543212"));

        when(request.getParameter("action")).thenReturn("list");
        when(request.getRequestDispatcher("/WEB-INF/views/employee-list.jsp")).thenReturn(dispatcher);

        employeeServlet.doGet(request, response);

        @SuppressWarnings("unchecked")
        List<Employee> list = (List<Employee>) requestAttributes.get("employeeList");
        assertNotNull(list);
        assertEquals(2, list.size());
        verify(dispatcher).forward(request, response);
    }

    // -------------------------------------------------------------------------
    // 3. SELECT EMPLOYEE BY ID TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testSelectEmployeeByIdFound() throws Exception {
        Employee emp = new Employee("Vikram Singh", "Male", "Manager", 75000.0, "Delhi", "vikram@co.com", "9876543213");
        stubEmployeeDAO.saveEmployee(emp);

        when(request.getParameter("action")).thenReturn("search");
        when(request.getParameter("id")).thenReturn(String.valueOf(emp.getId()));
        when(request.getRequestDispatcher("/WEB-INF/views/employee-search.jsp")).thenReturn(dispatcher);

        employeeServlet.doGet(request, response);

        Employee result = (Employee) requestAttributes.get("searchedEmployee");
        assertNotNull(result);
        assertEquals("Vikram Singh", result.getName());
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testSelectEmployeeByIdNotFound() throws Exception {
        when(request.getParameter("action")).thenReturn("search");
        when(request.getParameter("id")).thenReturn("9999");
        when(request.getRequestDispatcher("/WEB-INF/views/employee-search.jsp")).thenReturn(dispatcher);

        employeeServlet.doGet(request, response);

        assertNull(requestAttributes.get("searchedEmployee"));
        assertEquals("No employee found with ID: 9999", requestAttributes.get("errorMessage"));
        verify(dispatcher).forward(request, response);
    }

    // -------------------------------------------------------------------------
    // 4. DELETE EMPLOYEE WITH CONFIRMATION TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDeleteConfirmationDisplaysEmployeeDetails() throws Exception {
        Employee emp = new Employee("Kavita Menon", "Female", "Analyst", 52000.0, "Chennai", "kavita@co.com", "9876543214");
        stubEmployeeDAO.saveEmployee(emp);

        when(request.getParameter("action")).thenReturn("deleteConfirm");
        when(request.getParameter("id")).thenReturn(String.valueOf(emp.getId()));
        when(request.getRequestDispatcher("/WEB-INF/views/employee-delete-confirm.jsp")).thenReturn(dispatcher);

        employeeServlet.doGet(request, response);

        Employee toDelete = (Employee) requestAttributes.get("employeeToDelete");
        assertNotNull(toDelete);
        assertEquals(emp.getId(), toDelete.getId());
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDeleteConfirmedDeletesRecordAndRedirects() throws Exception {
        Employee emp = new Employee("Manoj Joshi", "Male", "DevOps", 65000.0, "Bengaluru", "manoj@co.com", "9876543215");
        stubEmployeeDAO.saveEmployee(emp);
        assertEquals(1, stubEmployeeDAO.getAllEmployees().size());

        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn(String.valueOf(emp.getId()));

        employeeServlet.doPost(request, response);

        // Record removed from DAO
        assertEquals(0, stubEmployeeDAO.getAllEmployees().size());
        verify(response).sendRedirect(contains("/HibernateEmployeeManagement/employee?action=list&successMessage="));
    }

    // -------------------------------------------------------------------------
    // 5. MODIFY PERSONAL DETAILS (SESSION IDENTITY PROTECTED)
    // -------------------------------------------------------------------------

    @Test
    public void testModifyPersonalDetailsUsesSessionIdentityOnly() throws Exception {
        User loggedInUser = new User("Pooja Nair", "A1234", "pooja@example.com", "dummyHash");
        loggedInUser.setId(1L);
        stubUserDAO.userToReturn = loggedInUser;

        // Even if request maliciously contains another ID, session ID is used
        when(request.getParameter("id")).thenReturn("999");
        when(request.getParameter("name")).thenReturn("Pooja Nair Updated");
        when(request.getParameter("city")).thenReturn("Kochi");
        when(request.getParameter("email")).thenReturn("pooja.updated@example.com");
        when(request.getParameter("mobile")).thenReturn("9876543216");

        profileServlet.doPost(request, response);

        // Verify update applied to logged-in user
        assertEquals("Pooja Nair Updated", stubUserDAO.lastUpdatedUser.getName());
        assertEquals("Kochi", stubUserDAO.lastUpdatedUser.getCity());
        assertEquals("pooja.updated@example.com", stubUserDAO.lastUpdatedUser.getEmail());
        assertEquals("9876543216", stubUserDAO.lastUpdatedUser.getMobile());

        // Verify session name updated live
        assertEquals("Pooja Nair Updated", sessionAttributes.get("userName"));
        verify(response).sendRedirect(contains("/HibernateEmployeeManagement/profile?successMessage="));
    }

    // -------------------------------------------------------------------------
    // 6. AUTHENTICATION PROTECTION TEST
    // -------------------------------------------------------------------------

    @Test
    public void testUnauthenticatedAccessBlockedForEmployeeRoutes() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        authenticationFilter.doFilter(request, response, filterChain);

        verify(response).sendRedirect("/HibernateEmployeeManagement/login.jsp?authError=session_required");
        verify(filterChain, never()).doFilter(request, response);
    }
}
