<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Employee | Employee Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>
<jsp:include page="_navbar.jsp"/>

<div class="app-shell">
    <jsp:include page="_sidebar.jsp">
        <jsp:param name="activePage" value="add"/>
    </jsp:include>

    <main class="page-content">

        <div class="page-header">
            <div>
                <h1>Add New Employee</h1>
                <p class="subtitle">Fill in the details below. The Employee ID is generated automatically.</p>
            </div>
        </div>

        <div class="card" style="max-width:760px;">

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert"><c:out value="${errorMessage}"/></div>
            </c:if>

            <form action="${pageContext.request.contextPath}/employee" method="POST" id="addEmployeeForm">
                <input type="hidden" name="action" value="add">

                <%-- Full Name --%>
                <div class="form-group">
                    <label for="name" class="form-label">Full Name <span class="required">*</span></label>
                    <input type="text" id="name" name="name" class="form-control"
                           placeholder="Enter employee's full name"
                           value="${enteredName}" required>
                </div>

                <div class="form-row">
                    <%-- Gender --%>
                    <div class="form-group">
                        <label for="gender" class="form-label">Gender <span class="required">*</span></label>
                        <select id="gender" name="gender" class="form-control" required>
                            <option value="">-- Select Gender --</option>
                            <option value="Male"   ${enteredGender eq 'Male'   ? 'selected' : ''}>Male</option>
                            <option value="Female" ${enteredGender eq 'Female' ? 'selected' : ''}>Female</option>
                            <option value="Other"  ${enteredGender eq 'Other'  ? 'selected' : ''}>Other</option>
                        </select>
                    </div>

                    <%-- Designation --%>
                    <div class="form-group">
                        <label for="designation" class="form-label">Designation <span class="required">*</span></label>
                        <input type="text" id="designation" name="designation" class="form-control"
                               placeholder="e.g. Software Engineer"
                               value="${enteredDesignation}" required>
                    </div>
                </div>

                <div class="form-row">
                    <%-- Salary --%>
                    <div class="form-group">
                        <label for="salary" class="form-label">Salary (₹) <span class="required">*</span></label>
                        <input type="number" step="0.01" min="0" id="salary" name="salary" class="form-control"
                               placeholder="e.g. 50000"
                               value="${enteredSalary}" required>
                    </div>

                    <%-- City --%>
                    <div class="form-group">
                        <label for="city" class="form-label">City <span class="required">*</span></label>
                        <input type="text" id="city" name="city" class="form-control"
                               placeholder="e.g. Bangalore"
                               value="${enteredCity}" required>
                    </div>
                </div>

                <div class="form-row">
                    <%-- Email --%>
                    <div class="form-group">
                        <label for="email" class="form-label">Email Address <span class="required">*</span></label>
                        <input type="email" id="email" name="email" class="form-control"
                               placeholder="emp@company.com"
                               value="${enteredEmail}" required>
                    </div>

                    <%-- Mobile --%>
                    <div class="form-group">
                        <label for="mobile" class="form-label">Mobile Number <span class="required">*</span></label>
                        <input type="tel" id="mobile" name="mobile" class="form-control"
                               placeholder="10-digit mobile number"
                               pattern="[0-9]{10}" maxlength="10"
                               value="${enteredMobile}" required>
                        <div class="help-text">10-digit numeric format, no spaces.</div>
                    </div>
                </div>

                <div class="btn-group" style="margin-top:1.5rem;">
                    <button type="submit" class="btn btn-primary" id="btn-submit-employee">Save Employee</button>
                    <a href="${pageContext.request.contextPath}/welcome" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>

        <footer class="app-footer"><p>&copy; 2026 Employee Management System.</p></footer>
    </main>
</div>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
