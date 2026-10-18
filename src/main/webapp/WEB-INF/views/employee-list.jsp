<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Directory | Employee Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>
<jsp:include page="_navbar.jsp"/>

<div class="app-shell">
    <jsp:include page="_sidebar.jsp">
        <jsp:param name="activePage" value="list"/>
    </jsp:include>

    <main class="page-content">

        <div class="page-header">
            <div>
                <h1>Employee Directory</h1>
                <p class="subtitle">All registered employee records in the system.</p>
            </div>
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/employee?action=search" class="btn btn-secondary" id="btn-list-search">Search by ID</a>
                <a href="${pageContext.request.contextPath}/employee?action=add"    class="btn btn-primary"   id="btn-list-add">Add Employee</a>
            </div>
        </div>

        <c:if test="${not empty param.successMessage or not empty successMessage}">
            <div class="alert alert-success" role="alert">
                <c:out value="${not empty param.successMessage ? param.successMessage : successMessage}"/>
            </div>
        </c:if>
        <c:if test="${not empty param.errorMessage or not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${not empty param.errorMessage ? param.errorMessage : errorMessage}"/>
            </div>
        </c:if>

        <div class="card" style="padding:0; overflow:hidden;">
            <c:choose>
                <c:when test="${not empty employeeList}">
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th style="text-align:center; width:60px;">ID</th>
                                    <th>Name</th>
                                    <th style="text-align:center;">Gender</th>
                                    <th>Designation</th>
                                    <th style="text-align:right;">Salary</th>
                                    <th>City</th>
                                    <th>Email</th>
                                    <th style="text-align:center;">Mobile</th>
                                    <th style="text-align:center; width:90px;">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="emp" items="${employeeList}">
                                    <tr>
                                        <td style="text-align:center; font-weight:700; color:var(--blue);">${emp.id}</td>
                                        <td style="font-weight:600;"><c:out value="${emp.name}"/></td>
                                        <td style="text-align:center;"><c:out value="${emp.gender}"/></td>
                                        <td><c:out value="${emp.designation}"/></td>
                                        <td style="text-align:right; font-variant-numeric:tabular-nums;">
                                            ₹<fmt:formatNumber value="${emp.salary}" minFractionDigits="2" maxFractionDigits="2"/>
                                        </td>
                                        <td><c:out value="${emp.city}"/></td>
                                        <td style="font-size:.85rem;"><c:out value="${emp.email}"/></td>
                                        <td style="text-align:center; font-size:.85rem; font-variant-numeric:tabular-nums;"><c:out value="${emp.mobile}"/></td>
                                        <td style="text-align:center;">
                                            <a href="${pageContext.request.contextPath}/employee?action=deleteConfirm&id=${emp.id}"
                                               class="btn btn-danger btn-sm" title="Delete employee ${emp.id}">Delete</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <h3>No Employees Found</h3>
                        <p>The employee directory is currently empty. Add the first employee record to get started.</p>
                        <a href="${pageContext.request.contextPath}/employee?action=add" class="btn btn-primary">Add Employee</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <footer class="app-footer"><p>&copy; 2026 Employee Management System.</p></footer>
    </main>
</div>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
