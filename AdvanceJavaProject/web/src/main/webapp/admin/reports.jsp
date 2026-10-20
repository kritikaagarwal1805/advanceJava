<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Reports & Analytics - Administrator"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <div>
            <h2 style="color: #1a237e; margin: 0;">Recruitment Analytics & Reports</h2>
            <p class="text-muted" style="margin-top: 0.25rem;">Statistical breakdown across candidates, listings, and applications</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-primary">&larr; Back to Dashboard</a>
    </div>

    <!-- Application Funnel Breakdown -->
    <div class="card p-4 mb-4">
        <h4 style="color: #1a237e; margin-bottom: 1.5rem;">Application Funnel Distribution</h4>
        <div class="d-flex flex-column gap-3">
            <c:forEach var="item" items="${stats.applicationsByStatus}">
                <div>
                    <div class="d-flex justify-between mb-1">
                        <strong>${item.key}</strong>
                        <span>${item.value} applications</span>
                    </div>
                    <div style="background: #e0e0e0; border-radius: 4px; height: 12px; overflow: hidden;">
                        <div style="background: #1565C0; height: 100%; width: ${stats.totalApplications > 0 ? (item.value * 100 / stats.totalApplications) : 0}%;"></div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Platform User Demographics -->
    <div class="d-flex flex-wrap gap-4">
        <div class="card p-4" style="flex: 1; min-width: 300px;">
            <h4 style="color: #1a237e; margin-bottom: 1rem;">User Roles</h4>
            <div class="d-flex flex-column gap-3">
                <c:forEach var="item" items="${stats.usersByRole}">
                    <div>
                        <div class="d-flex justify-between mb-1">
                            <span>${item.key}</span>
                            <strong>${item.value}</strong>
                        </div>
                        <div style="background: #e0e0e0; border-radius: 4px; height: 8px; overflow: hidden;">
                            <div style="background: #2e7d32; height: 100%; width: ${stats.totalUsers > 0 ? (item.value * 100 / stats.totalUsers) : 0}%;"></div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="card p-4" style="flex: 1; min-width: 300px;">
            <h4 style="color: #1a237e; margin-bottom: 1rem;">Listing Moderation</h4>
            <div class="d-flex flex-column gap-3">
                <c:forEach var="item" items="${stats.jobsByStatus}">
                    <div>
                        <div class="d-flex justify-between mb-1">
                            <span>${item.key}</span>
                            <strong>${item.value}</strong>
                        </div>
                        <div style="background: #e0e0e0; border-radius: 4px; height: 8px; overflow: hidden;">
                            <div style="background: #f57c00; height: 100%; width: ${stats.totalJobs > 0 ? (item.value * 100 / stats.totalJobs) : 0}%;"></div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
