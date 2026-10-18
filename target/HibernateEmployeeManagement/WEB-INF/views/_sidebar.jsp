<%-- Shared left sidebar fragment – include on every authenticated page.
     Pass pageParam="dashboard|add|list|search|delete|profile" to highlight active link. --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="sidebar" id="sidebar">
    <div class="sidebar-header">
        <span class="sidebar-logo-text">EMS</span>
    </div>
    <ul class="sidebar-nav">
        <li class="sidebar-item ${param.activePage eq 'dashboard' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/welcome" class="sidebar-link">
                <svg class="sidebar-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/>
                    <rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/>
                </svg>
                <span>Dashboard</span>
            </a>
        </li>
        <li class="sidebar-item ${param.activePage eq 'add' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/employee?action=add" class="sidebar-link">
                <svg class="sidebar-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/>
                    <line x1="19" y1="8" x2="19" y2="14"/><line x1="16" y1="11" x2="22" y2="11"/>
                </svg>
                <span>Add Employee</span>
            </a>
        </li>
        <li class="sidebar-item ${param.activePage eq 'list' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/employee?action=list" class="sidebar-link">
                <svg class="sidebar-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                    <circle cx="9" cy="7" r="4"/>
                    <path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                </svg>
                <span>All Employees</span>
            </a>
        </li>
        <li class="sidebar-item ${param.activePage eq 'search' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/employee?action=search" class="sidebar-link">
                <svg class="sidebar-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
                </svg>
                <span>Search Employee</span>
            </a>
        </li>
        <li class="sidebar-item ${param.activePage eq 'delete' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/employee?action=deleteConfirm" class="sidebar-link">
                <svg class="sidebar-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/>
                    <path d="M10 11v6"/><path d="M14 11v6"/>
                    <path d="M9 6V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
                </svg>
                <span>Delete Employee</span>
            </a>
        </li>
        <li class="sidebar-item ${param.activePage eq 'profile' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/profile" class="sidebar-link">
                <svg class="sidebar-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                    <circle cx="12" cy="7" r="4"/>
                </svg>
                <span>My Profile</span>
            </a>
        </li>
    </ul>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/logout" class="sidebar-logout-link">
            <svg class="sidebar-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
                <polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
            <span>Logout</span>
        </a>
    </div>
</nav>
