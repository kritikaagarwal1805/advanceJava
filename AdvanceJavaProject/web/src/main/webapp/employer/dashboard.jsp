<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/fragments/header.jsp">
    <jsp:param name="pageTitle" value="Employer Dashboard - CareerBridge" />
</jsp:include>
<jsp:include page="/fragments/messages.jsp" />

<div class="container page-content">
    <c:choose>
        <%-- Case 1: Employer has not created a company profile yet --%>
        <c:when test="${empty company}">
            <div class="card my-4">
                <div class="card-body text-center py-5">
                    <div class="empty-state-icon mb-3">🏢</div>
                    <h2 class="mb-2">Welcome to CareerBridge!</h2>
                    <p class="text-muted mb-4" style="max-width: 600px; margin-left: auto; margin-right: auto;">
                        To start posting job openings and managing candidates, you must first register your company profile.
                        It takes just a minute to set up your business identity.
                    </p>
                    <a href="${pageContext.request.contextPath}/employer/company" class="btn btn-primary btn-lg">
                        <span class="btn-icon">＋</span> Create Company Profile
                    </a>
                </div>
            </div>
        </c:when>

        <%-- Case 2: Employer has a company profile --%>
        <c:otherwise>
            <!-- Company Overview Banner -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="d-flex justify-between align-center flex-wrap gap-3">
                        <div class="d-flex align-center gap-3">
                            <c:choose>
                                <c:when test="${not empty company.logoUrl}">
                                    <img src="${company.logoUrl}" alt="${company.name} Logo" style="width: 64px; height: 64px; object-fit: contain; border-radius: var(--border-radius); border: 1px solid var(--color-gray-200); padding: 4px;" />
                                </c:when>
                                <c:otherwise>
                                    <div class="profile-avatar" style="width: 64px; height: 64px; font-size: 1.5rem;">
                                        ${company.name != null && company.name.length() > 0 ? company.name.substring(0, 1).toUpperCase() : 'C'}
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div>
                                <div class="d-flex align-center gap-2">
                                    <h1 class="h2 mb-0">${company.name}</h1>
                                    <span class="badge ${company.status == 'ACTIVE' ? 'badge-success' : (company.status == 'PENDING' ? 'badge-warning' : 'badge-secondary')}">
                                        ${company.status}
                                    </span>
                                </div>
                                <div class="text-muted mt-1 font-size-sm">
                                    <c:if test="${not empty company.industry}">
                                        <span>🏷️ ${company.industry}</span>
                                    </c:if>
                                    <c:if test="${not empty company.location}">
                                        <span class="ml-2">📍 ${company.location}</span>
                                    </c:if>
                                    <c:if test="${not empty company.website}">
                                        <span class="ml-2">🌐 <a href="${company.website}" target="_blank" rel="noopener noreferrer">${company.website}</a></span>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <!-- Quick Actions -->
                        <div class="d-flex align-center gap-2 flex-wrap">
                            <a href="${pageContext.request.contextPath}/employer/job-form" class="btn btn-primary">
                                <span>＋</span> Post New Job
                            </a>
                            <a href="${pageContext.request.contextPath}/employer/jobs" class="btn btn-outline-primary">
                                View All Jobs
                            </a>
                            <a href="${pageContext.request.contextPath}/employer/company" class="btn btn-outline-secondary">
                                Company Profile
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Stats Metric Cards -->
            <div class="stat-grid mb-5">
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #e3f2fd; color: #1565C0;">📋</div>
                    <div class="stat-info">
                        <div class="stat-value">
                            ${not empty totalJobs ? totalJobs : (not empty stats.totalJobs ? stats.totalJobs : 0)}
                        </div>
                        <div class="stat-label">Total Jobs Posted</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #dcfce7; color: #16a34a;">🚀</div>
                    <div class="stat-info">
                        <div class="stat-value">
                            ${not empty activeJobs ? activeJobs : (not empty stats.activeJobs ? stats.activeJobs : 0)}
                        </div>
                        <div class="stat-label">Active Jobs</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #f3e5f5; color: #7b1fa2;">👥</div>
                    <div class="stat-info">
                        <div class="stat-value">
                            ${not empty totalApplicants ? totalApplicants : (not empty stats.totalApplicants ? stats.totalApplicants : 0)}
                        </div>
                        <div class="stat-label">Total Applicants</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #fff3e0; color: #ea580c;">⏳</div>
                    <div class="stat-info">
                        <div class="stat-value">
                            ${not empty pendingReviews ? pendingReviews : (not empty stats.pendingReviews ? stats.pendingReviews : 0)}
                        </div>
                        <div class="stat-label">Pending Reviews</div>
                    </div>
                </div>
            </div>

            <!-- Recent Applicants Section -->
            <div class="card mb-4">
                <div class="card-header">
                    <div class="d-flex justify-between align-center w-100">
                        <h2 class="h4 mb-0">Recent Applicants</h2>
                        <a href="${pageContext.request.contextPath}/employer/jobs" class="btn btn-sm btn-outline-primary">
                            Browse by Job
                        </a>
                    </div>
                </div>
                <div class="card-body p-0">
                    <c:choose>
                        <c:when test="${not empty recentApplicants}">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead>
                                        <tr>
                                            <th>Candidate Name</th>
                                            <th>Applied For</th>
                                            <th>Applied Date</th>
                                            <th>Status</th>
                                            <th>Resume</th>
                                            <th class="text-right">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="app" items="${recentApplicants}">
                                            <tr>
                                                <td>
                                                    <strong>${not empty app.candidateName ? app.candidateName : 'Candidate #'.concat(app.candidateUserId)}</strong>
                                                    <c:if test="${not empty app.candidateEmail}">
                                                        <br/><span class="text-muted font-size-xs">${app.candidateEmail}</span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <span class="font-weight-500">${not empty app.jobTitle ? app.jobTitle : 'Job #'.concat(app.jobId)}</span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty app.appliedAt}">
                                                            ${app.appliedAt}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">-</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:set var="statusKey" value="${app.status != null ? app.status.toString().toLowerCase().replace('_', '-') : 'submitted'}" />
                                                    <span class="status-badge status-${statusKey}">
                                                        ${app.status}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty app.resumeId}">
                                                            <a href="${pageContext.request.contextPath}/resume/download?id=${app.resumeId}" class="btn btn-sm btn-outline-secondary" target="_blank" title="Download Resume">
                                                                📄 Resume
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted font-size-xs">No resume</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-right">
                                                    <a href="${pageContext.request.contextPath}/employer/candidate?id=${app.candidateUserId}&applicationId=${app.applicationId}" class="btn btn-sm btn-primary">
                                                        View Profile
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <div class="empty-state-icon">📥</div>
                                <div class="empty-state-title">No applications received yet</div>
                                <div class="empty-state-desc">
                                    When candidates apply for your posted jobs, their application records and resumes will appear here.
                                </div>
                                <a href="${pageContext.request.contextPath}/employer/job-form" class="btn btn-primary btn-sm">
                                    Post a Job Opening
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/fragments/footer.jsp" />
