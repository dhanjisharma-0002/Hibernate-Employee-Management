<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In | Employee Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="public-body">

    <header class="public-header">
        <span class="pub-brand">Employee Management</span>
        <ul>
            <li><a href="${pageContext.request.contextPath}/index.jsp"    class="nav-link">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/register.jsp" class="btn-nav-action">Create Account</a></li>
        </ul>
    </header>

    <main style="width:100%; max-width:460px;">
        <div class="auth-card">
            <h2>Account Login</h2>
            <p class="form-subtitle">Enter your employee credentials to access the portal.</p>

            <%-- Server error --%>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert" id="alert-login-error">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <%-- Session required --%>
            <c:if test="${param.authError eq 'session_required'}">
                <div class="alert alert-danger" role="alert" id="alert-session-required">
                    Authentication required. Please log in to continue.
                </div>
            </c:if>

            <%-- Logout success --%>
            <c:if test="${param.logout eq 'true'}">
                <div class="alert alert-success" role="alert" id="alert-logout-success">
                    You have been logged out successfully.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="POST" id="loginForm">

                <div class="form-group">
                    <label for="username" class="form-label">
                        Employee Number or Email <span class="required">*</span>
                    </label>
                    <input type="text" id="username" name="username" class="form-control"
                           placeholder="e.g. A1234 or user@example.com"
                           value="${enteredUsername}"
                           required autocomplete="username" autofocus>
                    <div class="help-text">5-character employee number or registered email</div>
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">
                        Password <span class="required">*</span>
                    </label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="Enter your 6-character password"
                           minlength="6" maxlength="6"
                           required autocomplete="current-password">
                    <div class="help-text">Your 6-character password</div>
                </div>

                <button type="submit" class="btn btn-primary btn-block" id="btn-submit-login">Sign In</button>

                <div class="form-footer">
                    Need an account?
                    <a href="${pageContext.request.contextPath}/register.jsp">Create an account</a>
                </div>
            </form>
        </div>
    </main>

    <footer style="margin-top:2rem; font-size:.8rem; color:rgba(255,255,255,.45);">
        &copy; 2026 Employee Management System
    </footer>

</body>
</html>
