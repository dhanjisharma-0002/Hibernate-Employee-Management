package com.wipro.employeemanagement.controller;

import com.wipro.employeemanagement.dao.UserDAO;
import com.wipro.employeemanagement.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Automated tests for Registration:
 * 1. Valid registration persists user & returns success message
 * 2. Rejects empty fields
 * 3. Rejects invalid employee numbers
 * 4. Rejects passwords not exactly 6 characters
 * 5. Rejects duplicate employee number
 * 6. Rejects duplicate email address
 */
public class RegisterServletTest {

    private static class StubUserDAO extends UserDAO {
        private final Map<String, User> storeByEmpNo = new HashMap<>();
        private final Map<String, User> storeByEmail = new HashMap<>();
        private User lastSavedUser;

        @Override
        public void saveUser(User user) {
            this.lastSavedUser = user;
            storeByEmpNo.put(user.getEmployeeNumber().toUpperCase(), user);
            storeByEmail.put(user.getEmail().toLowerCase(), user);
        }

        @Override
        public boolean isEmployeeNumberExists(String employeeNumber) {
            return storeByEmpNo.containsKey(employeeNumber.toUpperCase());
        }

        @Override
        public boolean isEmailExists(String email) {
            return storeByEmail.containsKey(email.toLowerCase());
        }
    }

    private StubUserDAO stubUserDAO;
    private RegisterServlet registerServlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private Map<String, Object> requestAttributes;

    @BeforeEach
    public void setUp() throws Exception {
        stubUserDAO = new StubUserDAO();
        registerServlet = new RegisterServlet();
        requestAttributes = new HashMap<>();

        Field daoField = RegisterServlet.class.getDeclaredField("userDAO");
        daoField.setAccessible(true);
        daoField.set(registerServlet, stubUserDAO);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        doAnswer(inv -> {
            requestAttributes.put(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(request).setAttribute(anyString(), any());

        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);
    }

    @Test
    public void testValidRegistrationSucceeds() throws Exception {
        when(request.getParameter("name")).thenReturn("Vikram Roy");
        when(request.getParameter("employeeNumber")).thenReturn("A5678");
        when(request.getParameter("email")).thenReturn("vikram@company.com");
        when(request.getParameter("password")).thenReturn("123456"); // exactly 6 chars

        registerServlet.doPost(request, response);

        assertNotNull(stubUserDAO.lastSavedUser);
        assertEquals("Vikram Roy", stubUserDAO.lastSavedUser.getName());
        assertEquals("A5678", stubUserDAO.lastSavedUser.getEmployeeNumber());
        assertEquals("vikram@company.com", stubUserDAO.lastSavedUser.getEmail());
        assertNotEquals("123456", stubUserDAO.lastSavedUser.getPasswordHash()); // Hashed!

        String success = (String) requestAttributes.get("successMessage");
        assertNotNull(success);
        assertTrue(success.contains("Registration successful for Employee A5678"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testRegistrationRejectsEmptyFields() throws Exception {
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("employeeNumber")).thenReturn("A1234");
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("123456");

        registerServlet.doPost(request, response);

        assertEquals("All fields are mandatory. Please fill in all details.", requestAttributes.get("errorMessage"));
        assertNull(stubUserDAO.lastSavedUser);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testRegistrationRejectsInvalidEmployeeNumber() throws Exception {
        when(request.getParameter("name")).thenReturn("Rahul Kumar");
        when(request.getParameter("employeeNumber")).thenReturn("12345"); // Starts with digit
        when(request.getParameter("email")).thenReturn("rahul@test.com");
        when(request.getParameter("password")).thenReturn("pass67");

        registerServlet.doPost(request, response);

        String error = (String) requestAttributes.get("errorMessage");
        assertNotNull(error);
        assertTrue(error.contains("Employee Number must start with 1 letter followed by exactly 4 digits"));
        assertNull(stubUserDAO.lastSavedUser);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testRegistrationRejectsPasswordNotExactlySixCharacters() throws Exception {
        when(request.getParameter("name")).thenReturn("Sonia Bose");
        when(request.getParameter("employeeNumber")).thenReturn("B4321");
        when(request.getParameter("email")).thenReturn("sonia@test.com");
        when(request.getParameter("password")).thenReturn("12345"); // 5 characters!

        registerServlet.doPost(request, response);

        assertEquals("Password must be exactly 6 characters long.", requestAttributes.get("errorMessage"));
        assertNull(stubUserDAO.lastSavedUser);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testRegistrationRejectsDuplicateEmployeeNumber() throws Exception {
        User existing = new User("Old User", "A1234", "old@test.com", "hash");
        stubUserDAO.saveUser(existing);

        when(request.getParameter("name")).thenReturn("New User");
        when(request.getParameter("employeeNumber")).thenReturn("A1234"); // Duplicate!
        when(request.getParameter("email")).thenReturn("new@test.com");
        when(request.getParameter("password")).thenReturn("abcdef");

        registerServlet.doPost(request, response);

        String error = (String) requestAttributes.get("errorMessage");
        assertNotNull(error);
        assertTrue(error.contains("Employee Number 'A1234' is already registered"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testRegistrationRejectsDuplicateEmail() throws Exception {
        User existing = new User("User 1", "A1111", "shared@test.com", "hash");
        stubUserDAO.saveUser(existing);

        when(request.getParameter("name")).thenReturn("User 2");
        when(request.getParameter("employeeNumber")).thenReturn("A2222");
        when(request.getParameter("email")).thenReturn("shared@test.com"); // Duplicate email
        when(request.getParameter("password")).thenReturn("fedcba");

        registerServlet.doPost(request, response);

        String error = (String) requestAttributes.get("errorMessage");
        assertNotNull(error);
        assertTrue(error.contains("Email address 'shared@test.com' is already registered"));
        verify(dispatcher).forward(request, response);
    }
}
