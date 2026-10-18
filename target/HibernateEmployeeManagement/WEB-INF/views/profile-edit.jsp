<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modify Personal Details | Employee Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>
<jsp:include page="_navbar.jsp"/>

<div class="app-shell">
    <jsp:include page="_sidebar.jsp">
        <jsp:param name="activePage" value="profile"/>
    </jsp:include>

    <main class="page-content">

        <div class="page-header">
            <div>
                <h1>My Profile</h1>
                <p class="subtitle">Update your personal contact and account information.</p>
            </div>
        </div>

        <div class="card" style="max-width:720px;">

            <c:if test="${not empty param.successMessage}">
                <div class="alert alert-success" role="alert"><c:out value="${param.successMessage}"/></div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert"><c:out value="${errorMessage}"/></div>
            </c:if>

            <form action="${pageContext.request.contextPath}/profile" method="POST" id="editProfileForm">

                <%-- Employee Number – read-only --%>
                <div class="form-group">
                    <label for="empNo" class="form-label">Employee Number</label>
                    <input type="text" id="empNo" class="form-control"
                           value="${profileUser.employeeNumber}" readonly disabled>
                    <div class="help-text">Assigned identifier — cannot be modified.</div>
                </div>

                <%-- Full Name --%>
                <div class="form-group">
                    <label for="name" class="form-label">Full Name <span class="required">*</span></label>
                    <input type="text" id="name" name="name" class="form-control"
                           value="${profileUser.name}"
                           required minlength="2" maxlength="100">
                </div>

                <%-- City & Mobile – two columns --%>
                <div class="form-row">
                    <div class="form-group">
                        <label for="city" class="form-label">City</label>
                        <input type="text" id="city" name="city" class="form-control"
                               placeholder="e.g. Bangalore" value="${profileUser.city}">
                    </div>
                    <div class="form-group">
                        <label for="mobile" class="form-label">Mobile Number</label>
                        <input type="tel" id="mobile" name="mobile" class="form-control"
                               placeholder="10-digit number"
                               pattern="[0-9]{10}" maxlength="10"
                               value="${profileUser.mobile}">
                        <div class="help-text">10 digits, no spaces or country code.</div>
                    </div>
                </div>

                <%-- Email --%>
                <div class="form-group">
                    <label for="email" class="form-label">Email Address <span class="required">*</span></label>
                    <input type="email" id="email" name="email" class="form-control"
                           value="${profileUser.email}" required>
                </div>

                <%-- Buttons --%>
                <div class="btn-group" style="margin-top:1.5rem;">
                    <button type="submit" class="btn btn-primary" id="btn-update-profile">Update Details</button>
                    <a href="${pageContext.request.contextPath}/welcome" class="btn btn-secondary" id="btn-cancel-profile">Cancel</a>
                </div>

            </form>
        </div>

        <footer class="app-footer"><p>&copy; 2026 Employee Management System.</p></footer>
    </main>
</div>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
