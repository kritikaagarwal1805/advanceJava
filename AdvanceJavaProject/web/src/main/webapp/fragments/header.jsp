<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title><c:out value="${not empty param.title ? param.title : 'CareerBridge'}" /> | CareerBridge Job Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <span class="brand-icon">CB</span>
            <span>CareerBridge</span>
        </a>
        <button class="navbar-toggler" type="button" aria-label="Toggle navigation" aria-expanded="false">
            &#9776;
        </button>
        <div class="navbar-collapse">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link ${param.activeNav == 'home' ? 'active' : ''}" href="${pageContext.request.contextPath}/">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${param.activeNav == 'jobs' ? 'active' : ''}" href="${pageContext.request.contextPath}/jobs">Jobs</a>
                </li>

                <c:if test="${not empty sessionScope.user}">
                    <c:choose>
                        <%-- Job Seeker Navigation --%>
                        <c:when test="${sessionScope.user.role == 'JOB_SEEKER'}">
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'dashboard' ? 'active' : ''}" href="${pageContext.request.contextPath}/candidate/dashboard">Dashboard</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'applications' ? 'active' : ''}" href="${pageContext.request.contextPath}/candidate/applications">My Applications</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'profile' ? 'active' : ''}" href="${pageContext.request.contextPath}/candidate/profile">Profile</a>
                            </li>
                        </c:when>

                        <%-- Employer Navigation --%>
                        <c:when test="${sessionScope.user.role == 'EMPLOYER'}">
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'dashboard' ? 'active' : ''}" href="${pageContext.request.contextPath}/employer/dashboard">Dashboard</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'my-jobs' ? 'active' : ''}" href="${pageContext.request.contextPath}/employer/jobs">My Jobs</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'company' ? 'active' : ''}" href="${pageContext.request.contextPath}/employer/company">Company</a>
                            </li>
                        </c:when>

                        <%-- Admin Navigation --%>
                        <c:when test="${sessionScope.user.role == 'ADMIN'}">
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'dashboard' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'users' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/users">Users</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'admin-jobs' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/jobs">Jobs</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${param.activeNav == 'reports' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/reports">Reports</a>
                            </li>
                        </c:when>
                    </c:choose>
                </c:if>
            </ul>

            <ul class="navbar-nav navbar-nav-right">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <li class="nav-item">
                            <a class="nav-link ${param.activeNav == 'login' ? 'active' : ''}" href="${pageContext.request.contextPath}/login">Login</a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/register">Register</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <span class="nav-user-info">
                                <span class="nav-user-avatar">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.user.fullName}">
                                            <c:out value="${fn:substring(sessionScope.user.fullName, 0, 1)}" />
                                        </c:when>
                                        <c:otherwise>U</c:otherwise>
                                    </c:choose>
                                </span>
                                <span class="nav-user-name"><c:out value="${sessionScope.user.fullName}" /></span>
                                <span class="badge badge-secondary"><c:out value="${sessionScope.user.role}" /></span>
                            </span>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-danger btn-sm" href="${pageContext.request.contextPath}/logout" data-confirm="Are you sure you want to log out?">Logout</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
<div class="page-content">
