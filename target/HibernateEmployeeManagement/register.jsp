<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | Employee Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="public-body">

    <header class="public-header">
        <span class="pub-brand">Employee Management</span>
        <ul>
            <li><a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/login.jsp" class="btn-nav-action">Login</a></li>
        </ul>
    </header>

    <main style="width:100%; max-width:480px;">
        <div class="auth-card">
            <h2>Create Account</h2>
            <p class="form-subtitle">Register with your employee details to access the portal.</p>

            <%-- Error --%>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert" id="alert-error">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <%-- Success --%>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success" role="alert" id="alert-success">
                    <c:out value="${successMessage}"/>
                    <div style="margin-top:.75rem;">
                        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary btn-sm" id="btn-success-login">
                            Proceed to Login
                        </a>
                    </div>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="POST" id="registerForm" novalidate>

                <div class="form-group">
                    <label for="name" class="form-label">Full Name <span class="required">*</span></label>
                    <input type="text" id="name" name="name" class="form-control"
                           placeholder="Enter your full name"
                           value="${enteredName}" required autocomplete="name">
                    <div class="help-text" id="nameHelp">Official name (2 to 100 characters)</div>
                </div>

                <div class="form-group">
                    <label for="employeeNumber" class="form-label">Employee Number <span class="required">*</span></label>
                    <input type="text" id="employeeNumber" name="employeeNumber" class="form-control"
                           placeholder="e.g. A1234"
                           value="${enteredEmpNo}"
                           pattern="^[A-Za-z][0-9]{4}$"
                           required maxlength="5"
                           title="1 letter followed by exactly 4 digits (e.g. A1234)"
                           autocomplete="off">
                    <div class="help-text" id="empNoHelp">Format: 1 letter followed by 4 digits (e.g. A1234)</div>
                </div>

                <div class="form-group">
                    <label for="email" class="form-label">Email Address <span class="required">*</span></label>
                    <input type="email" id="email" name="email" class="form-control"
                           placeholder="e.g. user@example.com"
                           value="${enteredEmail}" required autocomplete="email">
                    <div class="help-text" id="emailHelp">Official email address for correspondence</div>
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">Password <span class="required">*</span></label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="Enter exactly 6 characters"
                           minlength="6" maxlength="6"
                           required
                           title="Password must be exactly 6 characters"
                           autocomplete="new-password">
                    <div class="help-text" id="passwordHelp">Password must be exactly 6 characters</div>
                </div>

                <button type="submit" class="btn btn-primary btn-block" id="btn-submit-register">Create Account</button>

                <div class="form-footer">
                    Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Log in</a>
                </div>
            </form>
        </div>
    </main>

    <footer style="margin-top:2rem; font-size:.8rem; color:rgba(255,255,255,.45);">
        &copy; 2026 Employee Management System
    </footer>

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
