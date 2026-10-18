<%-- Shared top navbar fragment – include on every authenticated page --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="top-navbar">
    <div class="top-navbar-brand">
        <button class="sidebar-toggle" id="sidebarToggle" aria-label="Toggle sidebar">
            <span></span><span></span><span></span>
        </button>
        <a href="${pageContext.request.contextPath}/welcome" class="brand-link">
            Employee Management
        </a>
    </div>
    <div class="top-navbar-user">
        <span class="navbar-username">
            <svg class="icon-user" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/>
            </svg>
            <c:out value="${sessionScope.userName}" default="User"/>
        </span>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout" id="btn-navbar-logout">
            Logout
        </a>
    </div>
</header>
