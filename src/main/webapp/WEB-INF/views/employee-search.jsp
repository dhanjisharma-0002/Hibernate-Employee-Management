<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Employee | Employee Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>
<jsp:include page="_navbar.jsp"/>

<div class="app-shell">
    <jsp:include page="_sidebar.jsp">
        <jsp:param name="activePage" value="search"/>
    </jsp:include>

    <main class="page-content">

        <div class="page-header">
            <div>
                <h1>Search Employee</h1>
                <p class="subtitle">Enter a numeric Employee ID to retrieve the matching record.</p>
            </div>
        </div>

        <div class="card" style="max-width:680px;">

            <%-- Search form --%>
            <form action="${pageContext.request.contextPath}/employee" method="GET" style="margin-bottom:1.5rem;">
                <input type="hidden" name="action" value="search">
                <div class="form-group">
                    <label for="searchId" class="form-label">Employee ID <span class="required">*</span></label>
                    <div style="display:flex; gap:.75rem;">
                        <input type="number" id="searchId" name="id" class="form-control"
                               placeholder="e.g. 1"
                               value="${param.id}" min="1" required style="flex:1;" autofocus>
                        <button type="submit" class="btn btn-primary" id="btn-submit-search">Search</button>
                    </div>
                </div>
            </form>

            <%-- Error --%>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert"><c:out value="${errorMessage}"/></div>
            </c:if>

            <%-- Found employee --%>
            <c:if test="${not empty searchedEmployee}">
                <div style="border-top:1px solid var(--border); padding-top:1.5rem; margin-top:.5rem;">
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem; flex-wrap:wrap; gap:.75rem;">
                        <h3 style="color:var(--navy); margin:0;">
                            Employee Record #<c:out value="${searchedEmployee.id}"/>
                        </h3>
                        <a href="${pageContext.request.contextPath}/employee?action=deleteConfirm&id=${searchedEmployee.id}"
                           class="btn btn-danger btn-sm">Delete Record</a>
                    </div>

                    <div class="details-grid">
                        <div class="detail-item">
                            <span class="detail-label">Full Name</span>
                            <span class="detail-value"><c:out value="${searchedEmployee.name}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Gender</span>
                            <span class="detail-value"><c:out value="${searchedEmployee.gender}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Designation</span>
                            <span class="detail-value"><c:out value="${searchedEmployee.designation}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Salary</span>
                            <span class="detail-value">
                                ₹<fmt:formatNumber value="${searchedEmployee.salary}" minFractionDigits="2" maxFractionDigits="2"/>
                            </span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">City</span>
                            <span class="detail-value"><c:out value="${searchedEmployee.city}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Email Address</span>
                            <span class="detail-value"><c:out value="${searchedEmployee.email}"/></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Mobile Number</span>
                            <span class="detail-value"><c:out value="${searchedEmployee.mobile}"/></span>
                        </div>
                    </div>
                </div>
            </c:if>

            <div style="margin-top:1.5rem; padding-top:1rem; border-top:1px solid var(--border);">
                <a href="${pageContext.request.contextPath}/employee?action=list" class="btn btn-secondary">
                    View All Employees
                </a>
            </div>
        </div>

        <footer class="app-footer"><p>&copy; 2026 Employee Management System.</p></footer>
    </main>
</div>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
