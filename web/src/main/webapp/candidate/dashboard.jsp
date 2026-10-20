<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Candidate Dashboard - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <div>
            <h2 style="color: #1a237e; margin: 0;">Welcome, ${sessionScope.user.fullName}!</h2>
            <p class="text-muted" style="margin-top: 0.25rem;">Candidate Workspace &bull; ${sessionScope.user.email}</p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/candidate/profile" class="btn btn-outline-primary">Edit Profile</a>
            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary">Find Jobs</a>
        </div>
    </div>

    <!-- Metrics -->
    <div class="d-flex flex-wrap gap-3 mb-4">
        <div class="stat-card" style="flex: 1; min-width: 180px;">
            <div style="font-size: 2rem; font-weight: bold; color: #1565C0;">${totalApplications}</div>
            <div class="text-muted">Total Applications</div>
        </div>
        <div class="stat-card" style="flex: 1; min-width: 180px;">
            <div style="font-size: 2rem; font-weight: bold; color: #e65100;">${underReviewCount}</div>
            <div class="text-muted">Under Review</div>
        </div>
        <div class="stat-card" style="flex: 1; min-width: 180px;">
            <div style="font-size: 2rem; font-weight: bold; color: #6a1b9a;">${shortlistedCount}</div>
            <div class="text-muted">Shortlisted / Interview</div>
        </div>
        <div class="stat-card" style="flex: 1; min-width: 180px;">
            <div style="font-size: 2rem; font-weight: bold; color: #2e7d32;">${selectedCount}</div>
            <div class="text-muted">Offers Received</div>
        </div>
    </div>

    <!-- Recent Applications Table -->
    <div class="card p-4">
        <div class="d-flex justify-between align-center mb-3">
            <h4 style="color: #1a237e; margin: 0;">Recent Applications</h4>
            <a href="${pageContext.request.contextPath}/candidate/applications" class="btn btn-sm btn-outline-primary">View All</a>
        </div>

        <c:choose>
            <c:when test="${not empty applications}">
                <div style="overflow-x: auto;">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Position</th>
                                <th>Company</th>
                                <th>Applied Date</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="app" items="${applications}" begin="0" end="4">
                                <tr>
                                    <td><strong>${app.jobTitle}</strong></td>
                                    <td>${app.companyName}</td>
                                    <td>${app.appliedAt}</td>
                                    <td><my:statusBadge status="${app.status.name()}"/></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/candidate/application-detail?id=${app.applicationId}" class="btn btn-sm btn-secondary">Details</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center p-4 text-muted">
                    <p>You haven't submitted any applications yet.</p>
                    <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary btn-sm">Explore Open Positions</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
