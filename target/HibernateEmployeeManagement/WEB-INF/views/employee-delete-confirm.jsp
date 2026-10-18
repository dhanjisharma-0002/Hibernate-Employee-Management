<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delete Employee | Employee Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>
<jsp:include page="_navbar.jsp"/>

<div class="app-shell">
    <jsp:include page="_sidebar.jsp">
        <jsp:param name="activePage" value="delete"/>
    </jsp:include>

    <main class="page-content">

        <div class="page-header">
            <div>
                <h1>Delete Employee</h1>
                <p class="subtitle">Search for an employee by ID and confirm deletion after reviewing the record.</p>
            </div>
        </div>

        <div class="card" style="max-width:680px;">

            <%-- ID Lookup form --%>
            <form action="${pageContext.request.contextPath}/employee" method="GET" style="margin-bottom:1.5rem;">
                <input type="hidden" name="action" value="deleteConfirm">
                <div class="form-group">
                    <label for="deleteLookupId" class="form-label">
                        Enter Employee ID <span class="required">*</span>
                    </label>
                    <div style="display:flex; gap:.75rem;">
                        <input type="number" id="deleteLookupId" name="id" class="form-control"
                               placeholder="e.g. 1"
                               value="${param.id}" min="1" required style="flex:1;" autofocus>
                        <button type="submit" class="btn btn-secondary" id="btn-find-delete">Find Record</button>
                    </div>
                </div>
            </form>

            <%-- Success --%>
            <c:if test="${not empty param.successMessage or not empty successMessage}">
                <div class="alert alert-success" role="alert">
                    <c:out value="${not empty param.successMessage ? param.successMessage : successMessage}"/>
                </div>
            </c:if>

            <%-- Error --%>
            <c:if test="${not empty param.errorMessage or not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    <c:out value="${not empty param.errorMessage ? param.errorMessage : errorMessage}"/>
                </div>
            </c:if>

            <%-- Employee details + confirm --%>
            <c:if test="${not empty employeeToDelete}">

                <div class="alert alert-warning" role="alert">
                    <strong>Warning:</strong> You are about to permanently delete this employee record. Please review carefully.
                </div>

                <div style="margin:1.25rem 0;">
                    <h3 style="color:var(--navy); margin-bottom:.85rem;">
                        Record #<c:out value="${employeeToDelete.id}"/>: <c:out value="${employeeToDelete.name}"/>
                    </h3>

                    <div class="details-grid">
                        <div class="detail-item">
                            <span class="detail-label">Employee ID</span>
                            <span class="detail-value">#<c:out value="${employeeToDelete.id}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Full Name</span>
                            <span class="detail-value"><c:out value="${employeeToDelete.name}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Gender</span>
                            <span class="detail-value"><c:out value="${employeeToDelete.gender}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Designation</span>
                            <span class="detail-value"><c:out value="${employeeToDelete.designation}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Salary</span>
                            <span class="detail-value">
                                ₹<fmt:formatNumber value="${employeeToDelete.salary}" minFractionDigits="2" maxFractionDigits="2"/>
                            </span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">City</span>
                            <span class="detail-value"><c:out value="${employeeToDelete.city}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Email Address</span>
                            <span class="detail-value"><c:out value="${employeeToDelete.email}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Mobile Number</span>
                            <span class="detail-value"><c:out value="${employeeToDelete.mobile}"/></span>
                        </div>
                    </div>
                </div>

                <%-- Confirmation POST form --%>
                <form action="${pageContext.request.contextPath}/employee" method="POST" style="margin-top:1.5rem;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${employeeToDelete.id}">
                    <div class="btn-group">
                        <button type="submit" class="btn btn-danger" id="btn-confirm-delete">
                            Confirm &amp; Delete
                        </button>
                        <a href="${pageContext.request.contextPath}/employee?action=list"
                           class="btn btn-secondary" id="btn-cancel-delete">Cancel</a>
                    </div>
                </form>
            </c:if>

            <c:if test="${empty employeeToDelete and empty errorMessage}">
                <div style="margin-top:1.5rem; padding-top:1rem; border-top:1px solid var(--border);">
                    <a href="${pageContext.request.contextPath}/employee?action=list" class="btn btn-secondary">
                        View All Employees
                    </a>
                </div>
            </c:if>
        </div>

        <footer class="app-footer"><p>&copy; 2026 Employee Management System.</p></footer>
    </main>
</div>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
