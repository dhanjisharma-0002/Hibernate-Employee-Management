<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="public-body">

    <header class="public-header">
        <span class="pub-brand">Employee Management</span>
        <ul>
            <li><a href="${pageContext.request.contextPath}/login.jsp"    class="nav-link">Login</a></li>
            <li><a href="${pageContext.request.contextPath}/register.jsp" class="btn-nav-action">Create Account</a></li>
        </ul>
    </header>

    <main style="width:100%; max-width:560px; text-align:center;">
        <div class="auth-card" style="text-align:left;">
            <div style="text-align:center; margin-bottom:1.75rem;">
                <div style="display:inline-flex; align-items:center; justify-content:center;
                            width:64px; height:64px; border-radius:16px;
                            background:var(--blue-light); margin-bottom:1rem;">
                    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="var(--blue)" stroke-width="2">
                        <rect x="2" y="7" width="20" height="14" rx="2"/>
                        <path d="M16 7V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v2"/>
                        <line x1="12" y1="12" x2="12" y2="16"/><line x1="10" y1="14" x2="14" y2="14"/>
                    </svg>
                </div>
                <h1 style="font-size:1.6rem; margin-bottom:.4rem;">Employee Management System</h1>
                <p style="font-size:.9rem; color:var(--muted);">
                    Internal portal for employee record management, directory operations and profile administration.
                </p>
            </div>

            <div class="btn-group" style="flex-direction:column; gap:.85rem;">
                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary btn-block" id="btn-home-login">
                    Sign In
                </a>
                <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-secondary btn-block" id="btn-home-register">
                    Create Account
                </a>
            </div>

            <p style="text-align:center; margin-top:1.5rem; font-size:.8rem; color:var(--muted);">
                Hibernate ORM &middot; Jakarta Servlets &middot; MySQL
            </p>
        </div>
    </main>

    <footer style="margin-top:2rem; font-size:.8rem; color:rgba(255,255,255,.45);">
        &copy; 2026 Employee Management System
    </footer>

</body>
</html>
