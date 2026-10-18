package com.wipro.employeemanagement.controller;

import com.wipro.employeemanagement.dao.UserDAO;
import com.wipro.employeemanagement.filter.AuthenticationFilter;
import com.wipro.employeemanagement.model.User;
import com.wipro.employeemanagement.util.PasswordUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Automated tests for Phase 3:
 * 1. Missing credentials (both, username only, password only)
 * 2. Nonexistent user
 * 3. Wrong password
 * 4. Successful login, session creation, and redirect
 * 5. Direct URL access security filter
 * 6. Logout and session invalidation
 */
public class LoginAndSessionSecurityTest {

    // Subclass stub avoids Java 25 bytecode instrumentation issues with mocking concrete classes
    private static class StubUserDAO extends UserDAO {
        private User userToReturn;

        public void setUserToReturn(User user) {
            this.userToReturn = user;
        }

        @Override
        public User getUserByUsernameOrEmployeeNumber(String identifier) {
            return userToReturn;
        }
    }

    private StubUserDAO stubUserDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private FilterChain filterChain;

    private LoginServlet loginServlet;
    private LogoutServlet logoutServlet;
    private AuthenticationFilter authenticationFilter;
    private Map<String, Object> requestAttributes;
    private Map<String, Object> sessionAttributes;

    @BeforeEach
    public void setUp() throws Exception {
        stubUserDAO = new StubUserDAO();
        loginServlet = new LoginServlet();
        logoutServlet = new LogoutServlet();
        authenticationFilter = new AuthenticationFilter();

        requestAttributes = new HashMap<>();
        sessionAttributes = new HashMap<>();

        // Inject StubUserDAO using reflection
        Field daoField = LoginServlet.class.getDeclaredField("userDAO");
        daoField.setAccessible(true);
        daoField.set(loginServlet, stubUserDAO);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        filterChain = mock(FilterChain.class);

        // Capture request attributes
        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            requestAttributes.put(key, value);
            return null;
        }).when(request).setAttribute(anyString(), any());

        // Capture session attributes
        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            sessionAttributes.put(key, value);
            return null;
        }).when(session).setAttribute(anyString(), any());
    }

    @Test
    public void testMissingCredentialsBoth() throws Exception {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        assertEquals("Please enter both username/employee number and password.", requestAttributes.get("errorMessage"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testMissingUsername() throws Exception {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        assertEquals("Please enter your employee number or username.", requestAttributes.get("errorMessage"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testMissingPassword() throws Exception {
        when(request.getParameter("username")).thenReturn("A1234");
        when(request.getParameter("password")).thenReturn("");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        assertEquals("Please enter your password.", requestAttributes.get("errorMessage"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testNonexistentUser() throws Exception {
        when(request.getParameter("username")).thenReturn("Z9999");
        when(request.getParameter("password")).thenReturn("123456");
        stubUserDAO.setUserToReturn(null);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        String error = (String) requestAttributes.get("errorMessage");
        assertNotNull(error);
        assertTrue(error.contains("No account found with employee number/username 'Z9999'"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testWrongPassword() throws Exception {
        String realPasswordHash = PasswordUtil.hashPassword("secret");
        User testUser = new User("Ram Sharma", "A1234", "ram@example.com", realPasswordHash);
        testUser.setId(101L);

        when(request.getParameter("username")).thenReturn("A1234");
        when(request.getParameter("password")).thenReturn("wrongP"); // wrong 6-char password
        stubUserDAO.setUserToReturn(testUser);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        loginServlet.doPost(request, response);

        assertEquals("Incorrect password. Please verify and try again.", requestAttributes.get("errorMessage"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testSuccessfulLoginCreatesSessionAndRedirects() throws Exception {
        String correctPassword = "mypass";
        String realPasswordHash = PasswordUtil.hashPassword(correctPassword);
        User testUser = new User("Ram", "A1234", "ram@example.com", realPasswordHash);
        testUser.setId(1L);

        when(request.getParameter("username")).thenReturn("A1234");
        when(request.getParameter("password")).thenReturn(correctPassword);
        stubUserDAO.setUserToReturn(testUser);
        when(request.getContextPath()).thenReturn("/HibernateEmployeeManagement");
        when(request.getSession(false)).thenReturn(null);
        when(request.getSession(true)).thenReturn(session);

        loginServlet.doPost(request, response);

        // Verify session attributes were stored
        assertEquals(1L, sessionAttributes.get("userId"));
        assertEquals("Ram", sessionAttributes.get("userName"));
        assertEquals("A1234", sessionAttributes.get("employeeNumber"));
        assertEquals("ram@example.com", sessionAttributes.get("userEmail"));
        assertEquals(testUser, sessionAttributes.get("loggedInUser"));

        // Verify redirect to welcome page
        verify(response).sendRedirect("/HibernateEmployeeManagement/welcome");
    }

    @Test
    public void testDirectUrlAccessBlockedWithoutSession() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/HibernateEmployeeManagement");

        authenticationFilter.doFilter(request, response, filterChain);

        // Verify redirect to login page with authError flag
        verify(response).sendRedirect("/HibernateEmployeeManagement/login.jsp?authError=session_required");
        verify(response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void testDirectUrlAccessAllowedWithSession() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(1L);

        authenticationFilter.doFilter(request, response, filterChain);

        // Verify access granted
        verify(filterChain).doFilter(request, response);
        verify(response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    }

    @Test
    public void testLogoutInvalidatesSessionAndRedirects() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/HibernateEmployeeManagement");

        logoutServlet.doGet(request, response);

        // Verify session was invalidated
        verify(session).invalidate();

        // Verify no-cache headers set
        verify(response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        // Verify redirect to login page with logout flag
        verify(response).sendRedirect("/HibernateEmployeeManagement/login.jsp?logout=true");
    }
}
