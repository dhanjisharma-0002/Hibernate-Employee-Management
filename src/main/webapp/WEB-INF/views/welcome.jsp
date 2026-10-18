<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | Employee Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%-- Mobile sidebar overlay --%>
<div class="sidebar-overlay" id="sidebarOverlay"></div>

<%-- Shared top navbar --%>
<jsp:include page="_navbar.jsp"/>

<div class="app-shell">
    <%-- Shared sidebar – mark Dashboard as active --%>
    <jsp:include page="_sidebar.jsp">
        <jsp:param name="activePage" value="dashboard"/>
    </jsp:include>

    <main class="page-content">

        <%-- Page header --%>
        <div class="page-header">
            <div>
                <h1>Welcome, <c:out value="${sessionScope.userName}"/></h1>
                <p class="subtitle">Select an operation from the cards below or use the sidebar navigation.</p>
            </div>
        </div>

        <%-- 5 Operation Cards --%>
        <div class="dashboard-grid">

            <%-- Add Employee --%>
            <div class="op-card">
                <div class="op-card-icon icon-blue">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="8" r="4"/>
                        <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/>
                        <line x1="19" y1="8" x2="19" y2="14"/><line x1="16" y1="11" x2="22" y2="11"/>
                    </svg>
                </div>
                <h3>Add Employee</h3>
                <p>Register a new employee with designation, salary, city and contact details.</p>
                <div class="card-action">
                    <a href="${pageContext.request.contextPath}/employee?action=add" class="btn btn-primary btn-block" id="op-add-employee">
                        Add Employee
                    </a>
                </div>
            </div>

            <%-- View All Employees --%>
            <div class="op-card">
                <div class="op-card-icon icon-green">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                        <circle cx="9" cy="7" r="4"/>
                        <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
                        <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                    </svg>
                </div>
                <h3>View All Employees</h3>
                <p>Browse the complete list of all registered employee records in a structured table.</p>
                <div class="card-action">
                    <a href="${pageContext.request.contextPath}/employee?action=list" class="btn btn-primary btn-block" id="op-view-all">
                        View All Employees
                    </a>
                </div>
            </div>

            <%-- Search Employee --%>
            <div class="op-card">
                <div class="op-card-icon icon-purple">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
                    </svg>
                </div>
                <h3>Search Employee</h3>
                <p>Look up an individual employee record by their auto-generated numeric Employee ID.</p>
                <div class="card-action">
                    <a href="${pageContext.request.contextPath}/employee?action=search" class="btn btn-secondary btn-block" id="op-search-id">
                        Search by ID
                    </a>
                </div>
            </div>

            <%-- Delete Employee --%>
            <div class="op-card">
                <div class="op-card-icon icon-amber">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="3 6 5 6 21 6"/>
                        <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/>
                        <path d="M10 11v6"/><path d="M14 11v6"/>
                        <path d="M9 6V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
                    </svg>
                </div>
                <h3>Delete Employee</h3>
                <p>Locate an employee by ID, review the record, and permanently remove it after confirmation.</p>
                <div class="card-action">
                    <a href="${pageContext.request.contextPath}/employee?action=deleteConfirm" class="btn btn-secondary btn-block" id="op-delete-employee">
                        Delete Employee
                    </a>
                </div>
            </div>

            <%-- Modify Personal Details --%>
            <div class="op-card">
                <div class="op-card-icon icon-slate">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                        <circle cx="12" cy="7" r="4"/>
                    </svg>
                </div>
                <h3>Modify Personal Details</h3>
                <p>Update your own profile: name, city, email and mobile number.</p>
                <div class="card-action">
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary btn-block" id="op-modify-profile">
                        Modify Details
                    </a>
                </div>
            </div>

        </div>

        <footer class="app-footer">
            <p>&copy; 2026 Employee Management System. Developed with Jakarta Servlets &amp; Hibernate ORM.</p>
        </footer>
    </main>
</div>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
