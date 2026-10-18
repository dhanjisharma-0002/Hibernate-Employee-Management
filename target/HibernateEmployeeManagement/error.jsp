<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notice | Employee Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="public-body">

    <!-- Header Navigation -->
    <header class="public-header">
        <span class="pub-brand">Employee Management</span>
        <ul>
            <li><a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/login.jsp" class="btn-nav-action">Login</a></li>
        </ul>
    </header>

    <!-- Error Notice Container -->
    <main style="width:100%; max-width:500px;">
        <div class="auth-card" style="text-align: center;">
            <h2>System Notice</h2>
            <p class="form-subtitle">
                An unexpected condition was encountered while processing your request. Internal details and stack traces are suppressed for security reasons.
            </p>

            <div class="btn-group" style="justify-content: center; margin-top: 1.5rem;">
                <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary">
                    Return to Home
                </a>
                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-secondary">
                    Back to Login
                </a>
            </div>
        </div>
    </main>

    <!-- Footer -->
    <footer style="margin-top:2rem; font-size:.8rem; color:rgba(255,255,255,.45);">
        &copy; 2026 Employee Management System
    </footer>

</body>
</html>
