<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Administrator Dashboard - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <div>
            <h2 style="color: #1a237e; margin: 0;">Platform Administration</h2>
            <p class="text-muted" style="margin-top: 0.25rem;">System overview, moderation, and operational statistics</p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-primary">Manage Users</a>
            <a href="${pageContext.request.contextPath}/admin/jobs" class="btn btn-outline-primary">Moderate Jobs</a>
            <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-primary">Reports</a>
        </div>
    </div>

    <!-- Top KPI Cards -->
    <div class="d-flex flex-wrap gap-3 mb-4">
        <div class="stat-card" style="flex: 1; min-width: 200px;">
            <div style="font-size: 2.2rem; font-weight: bold; color: #1565C0;">${stats.totalUsers}</div>
            <div class="text-muted">Total Accounts</div>
        </div>
        <div class="stat-card" style="flex: 1; min-width: 200px;">
            <div style="font-size: 2.2rem; font-weight: bold; color: #2e7d32;">${stats.totalJobs}</div>
            <div class="text-muted">Total Postings</div>
        </div>
        <div class="stat-card" style="flex: 1; min-width: 200px;">
            <div style="font-size: 2.2rem; font-weight: bold; color: #e65100;">${stats.totalApplications}</div>
            <div class="text-muted">Applications Filed</div>
        </div>
        <div class="stat-card" style="flex: 1; min-width: 200px;">
            <div style="font-size: 2.2rem; font-weight: bold; color: #6a1b9a;">${stats.totalCompanies}</div>
            <div class="text-muted">Enterprises</div>
        </div>
    </div>

    <!-- Breakdown Tables -->
    <div class="d-flex flex-wrap gap-4">
        <!-- Users by Role -->
        <div class="card p-4" style="flex: 1; min-width: 300px;">
            <h4 style="color: #1a237e; margin-bottom: 1rem;">Users by Role</h4>
            <table class="table">
                <thead><tr><th>Role</th><th>Count</th></tr></thead>
                <tbody>
                    <c:forEach var="item" items="${stats.usersByRole}">
                        <tr>
                            <td><strong>${item.key}</strong></td>
                            <td>${item.value}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Jobs by Status -->
        <div class="card p-4" style="flex: 1; min-width: 300px;">
            <h4 style="color: #1a237e; margin-bottom: 1rem;">Jobs by Status</h4>
            <table class="table">
                <thead><tr><th>Status</th><th>Count</th></tr></thead>
                <tbody>
                    <c:forEach var="item" items="${stats.jobsByStatus}">
                        <tr>
                            <td><strong>${item.key}</strong></td>
                            <td>${item.value}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
