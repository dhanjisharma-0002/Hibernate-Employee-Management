package com.wipro.employeemanagement.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Jakarta Filter guarding protected application routes from unauthenticated access.
 * Enforces session checks and prevents browser caching of sensitive pages.
 */
@WebFilter(
    filterName = "AuthenticationFilter",
    urlPatterns = {
        "/welcome",
        "/welcome.jsp",
        "/employee",
        "/employee/*",
        "/profile",
        "/profile/*"
    }
)
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Prevent browser caching on all protected pages
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
        httpResponse.setDateHeader("Expires", 0); // Proxies

        HttpSession session = httpRequest.getSession(false);
        boolean isAuthenticated = (session != null && session.getAttribute("userId") != null);

        if (isAuthenticated) {
            // User is authenticated, proceed to the requested resource
            chain.doFilter(request, response);
        } else {
            // Unauthenticated direct access attempt: redirect to login page
            System.out.println("[AuthenticationFilter] Unauthorized access attempt blocked to: " + httpRequest.getRequestURI());
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?authError=session_required");
        }
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
