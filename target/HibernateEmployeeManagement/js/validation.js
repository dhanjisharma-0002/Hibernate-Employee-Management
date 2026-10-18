/**
 * Sidebar mobile toggle + validation.js
 */
document.addEventListener("DOMContentLoaded", function () {

    /* ── Sidebar toggle ─────────────────────────── */
    const toggle   = document.getElementById("sidebarToggle");
    const sidebar  = document.getElementById("sidebar");
    const overlay  = document.getElementById("sidebarOverlay");

    if (toggle && sidebar) {
        toggle.addEventListener("click", function () {
            sidebar.classList.toggle("open");
            if (overlay) overlay.classList.toggle("show");
        });
    }
    if (overlay) {
        overlay.addEventListener("click", function () {
            if (sidebar) sidebar.classList.remove("open");
            overlay.classList.remove("show");
        });
    }

    /* ── Registration form validation ──────────── */
    const form           = document.getElementById("registerForm");
    if (!form) return;

    const empNoInput     = document.getElementById("employeeNumber");
    const passwordInput  = document.getElementById("password");
    const emailInput     = document.getElementById("email");
    const nameInput      = document.getElementById("name");
    const empNoHelp      = document.getElementById("empNoHelp");
    const passwordHelp   = document.getElementById("passwordHelp");
    const emailHelp      = document.getElementById("emailHelp");

    const empNoRegex     = /^[A-Za-z][0-9]{4}$/;
    const emailRegex     = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

    if (empNoInput && empNoHelp) {
        empNoInput.addEventListener("input", function () {
            const v = empNoInput.value.trim();
            if (!v) {
                empNoHelp.textContent = "Format: 1 letter followed by 4 digits (e.g. A1234)";
                empNoHelp.className = "help-text";
                empNoInput.classList.remove("is-valid", "is-invalid");
            } else if (empNoRegex.test(v)) {
                empNoHelp.textContent = "Valid format";
                empNoHelp.className = "help-text text-success";
                empNoInput.classList.replace("is-invalid", "is-valid");
                empNoInput.classList.add("is-valid");
            } else {
                empNoHelp.textContent = "Must be 1 letter + exactly 4 digits (e.g. A1234)";
                empNoHelp.className = "help-text text-danger";
                empNoInput.classList.remove("is-valid");
                empNoInput.classList.add("is-invalid");
            }
        });
    }

    if (passwordInput && passwordHelp) {
        passwordInput.addEventListener("input", function () {
            const len = passwordInput.value.length;
            if (len === 0) {
                passwordHelp.textContent = "Password must be exactly 6 characters";
                passwordHelp.className = "help-text";
                passwordInput.classList.remove("is-valid", "is-invalid");
            } else if (len === 6) {
                passwordHelp.textContent = "Valid length";
                passwordHelp.className = "help-text text-success";
                passwordInput.classList.remove("is-invalid");
                passwordInput.classList.add("is-valid");
            } else {
                passwordHelp.textContent = len + " of 6 characters";
                passwordHelp.className = "help-text text-danger";
                passwordInput.classList.remove("is-valid");
                passwordInput.classList.add("is-invalid");
            }
        });
    }

    if (emailInput && emailHelp) {
        emailInput.addEventListener("input", function () {
            const v = emailInput.value.trim();
            if (!v) {
                emailHelp.textContent = "e.g. user@example.com";
                emailHelp.className = "help-text";
                emailInput.classList.remove("is-valid", "is-invalid");
            } else if (emailRegex.test(v)) {
                emailHelp.textContent = "Valid email";
                emailHelp.className = "help-text text-success";
                emailInput.classList.remove("is-invalid");
                emailInput.classList.add("is-valid");
            } else {
                emailHelp.textContent = "Enter a valid email address";
                emailHelp.className = "help-text text-danger";
                emailInput.classList.remove("is-valid");
                emailInput.classList.add("is-invalid");
            }
        });
    }

    form.addEventListener("submit", function (e) {
        let ok = true, first = null;
        const check = (el, condition) => {
            if (!condition) { ok = false; el.classList.add("is-invalid"); if (!first) first = el; }
        };
        if (nameInput)     check(nameInput,    nameInput.value.trim().length >= 2);
        if (empNoInput)    check(empNoInput,   empNoRegex.test(empNoInput.value.trim()));
        if (passwordInput) check(passwordInput, passwordInput.value.length === 6);
        if (emailInput)    check(emailInput,   emailRegex.test(emailInput.value.trim()));
        if (!ok) { e.preventDefault(); if (first) first.focus(); }
    });
});
