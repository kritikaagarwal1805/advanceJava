<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/fragments/header.jsp">
    <jsp:param name="pageTitle" value="Applicants for: ${job.title} - CareerBridge" />
</jsp:include>
<jsp:include page="/fragments/messages.jsp" />

<div class="container page-content">
    <!-- Header & Navigation -->
    <div class="d-flex justify-between align-center flex-wrap gap-3 mb-4">
        <div>
            <div class="mb-1">
                <a href="${pageContext.request.contextPath}/employer/jobs" class="text-muted font-size-sm">
                    &larr; Back to My Jobs
                </a>
            </div>
            <h1 class="h2 mb-1">
                Applicants for: <span class="text-primary"><c:out value="${job.title}" /></span>
            </h1>
            <p class="text-muted mb-0">
                <span>📍 <c:out value="${job.location}" /></span> |
                <span>💼 ${job.jobType}</span> |
                <span>Status: <span class="badge ${job.status == 'PUBLISHED' ? 'badge-success' : 'badge-secondary'}">${job.status}</span></span>
            </p>
        </div>

        <div class="d-flex align-center gap-2">
            <a href="${pageContext.request.contextPath}/employer/job-form?id=${job.jobId}" class="btn btn-outline-secondary">
                Edit Job
            </a>
        </div>
    </div>

    <!-- Stats Summary Breakdown -->
    <div class="stat-grid mb-4">
        <div class="stat-card">
            <div class="stat-icon" style="background-color: #e3f2fd; color: #1565C0;">👥</div>
            <div class="stat-info">
                <div class="stat-value">
                    ${not empty totalApplicants ? totalApplicants : (not empty stats.total ? stats.total : (not empty applicants ? applicants.size() : 0))}
                </div>
                <div class="stat-label">Total Applicants</div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-icon" style="background-color: #e0f2fe; color: #0284c7;">📥</div>
            <div class="stat-info">
                <div class="stat-value">
                    ${not empty countSubmitted ? countSubmitted : (not empty stats.submitted ? stats.submitted : 0)}
                </div>
                <div class="stat-label">Submitted</div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-icon" style="background-color: #fff3e0; color: #f57c00;">🔍</div>
            <div class="stat-info">
                <div class="stat-value">
                    ${not empty countUnderReview ? countUnderReview : (not empty stats.underReview ? stats.underReview : 0)}
                </div>
                <div class="stat-label">Under Review</div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-icon" style="background-color: #f3e5f5; color: #7b1fa2;">⭐</div>
            <div class="stat-info">
                <div class="stat-value">
                    ${not empty countShortlisted ? countShortlisted : (not empty stats.shortlisted ? stats.shortlisted : 0)}
                </div>
                <div class="stat-label">Shortlisted</div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-icon" style="background-color: #e0f2f1; color: #00897b;">💬</div>
            <div class="stat-info">
                <div class="stat-value">
                    ${not empty countInterview ? countInterview : (not empty stats.interview ? stats.interview : 0)}
                </div>
                <div class="stat-label">Interview</div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-icon" style="background-color: #e8f5e9; color: #2e7d32;">🎉</div>
            <div class="stat-info">
                <div class="stat-value">
                    ${not empty countSelected ? countSelected : (not empty stats.selected ? stats.selected : 0)}
                </div>
                <div class="stat-label">Selected</div>
            </div>
        </div>
    </div>

    <!-- Filter Bar -->
    <div class="card mb-4">
        <div class="card-body py-3 px-4">
            <div class="d-flex justify-between align-center flex-wrap gap-3">
                <div class="d-flex align-center gap-2">
                    <label for="statusFilter" class="form-label mb-0 font-weight-600">Filter by Status:</label>
                    <c:set var="filterStatus" value="${not empty param.status ? param.status : 'ALL'}" />
                    <select id="statusFilter" class="form-control form-control-sm" style="width: 220px;"
                            onchange="window.location.href='${pageContext.request.contextPath}/employer/applicants?jobId=${job.jobId}&status=' + encodeURIComponent(this.value)">
                        <option value="ALL" ${filterStatus == 'ALL' ? 'selected' : ''}>All Applicants</option>
                        <option value="SUBMITTED" ${filterStatus == 'SUBMITTED' ? 'selected' : ''}>Submitted</option>
                        <option value="UNDER_REVIEW" ${filterStatus == 'UNDER_REVIEW' ? 'selected' : ''}>Under Review</option>
                        <option value="SHORTLISTED" ${filterStatus == 'SHORTLISTED' ? 'selected' : ''}>Shortlisted</option>
                        <option value="INTERVIEW" ${filterStatus == 'INTERVIEW' ? 'selected' : ''}>Interview</option>
                        <option value="SELECTED" ${filterStatus == 'SELECTED' ? 'selected' : ''}>Selected</option>
                        <option value="REJECTED" ${filterStatus == 'REJECTED' ? 'selected' : ''}>Rejected</option>
                        <option value="WITHDRAWN" ${filterStatus == 'WITHDRAWN' ? 'selected' : ''}>Withdrawn</option>
                    </select>
                </div>

                <c:if test="${filterStatus != 'ALL'}">
                    <a href="${pageContext.request.contextPath}/employer/applicants?jobId=${job.jobId}" class="btn btn-sm btn-outline-secondary">
                        Clear Filter
                    </a>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Applicants Table -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty applicants}">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Candidate Name</th>
                                    <th>Email</th>
                                    <th>Applied Date</th>
                                    <th>Status</th>
                                    <th>Resume</th>
                                    <th>Update Status</th>
                                    <th class="text-right">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="matchedCount" value="0" />
                                <c:forEach var="app" items="${applicants}">
                                    <c:if test="${filterStatus == 'ALL' || app.status == filterStatus}">
                                        <c:set var="matchedCount" value="${matchedCount + 1}" />
                                        <tr>
                                            <td>
                                                <div class="font-weight-600">
                                                    <a href="${pageContext.request.contextPath}/employer/candidate?id=${app.candidateUserId}&applicationId=${app.applicationId}" class="text-primary">
                                                        <c:out value="${not empty app.candidateName ? app.candidateName : 'Candidate #'.concat(app.candidateUserId)}" />
                                                    </a>
                                                </div>
                                            </td>
                                            <td>
                                                <a href="mailto:${app.candidateEmail}" class="text-muted">
                                                    <c:out value="${app.candidateEmail}" />
                                                </a>
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
                                                <c:set var="stKey" value="${app.status != null ? app.status.toString().toLowerCase().replace('_', '-') : 'submitted'}" />
                                                <span class="status-badge status-${stKey}">
                                                    ${app.status}
                                                </span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty app.resumeId}">
                                                        <a href="${pageContext.request.contextPath}/resume/download?id=${app.resumeId}"
                                                           class="btn btn-sm btn-outline-secondary"
                                                           target="_blank"
                                                           title="Download Candidate Resume">
                                                            📄 Download
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted font-size-xs">No resume</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <%-- Terminal statuses --%>
                                                    <c:when test="${app.status == 'SELECTED' || app.status == 'REJECTED' || app.status == 'WITHDRAWN'}">
                                                        <span class="text-muted font-size-xs italic">Final status</span>
                                                    </c:when>

                                                    <%-- Active statuses with allowed transitions --%>
                                                    <c:otherwise>
                                                        <form action="${pageContext.request.contextPath}/employer/application-status" method="post" class="d-flex align-center gap-1">
                                                            <input type="hidden" name="_csrf" value="${csrfToken != null ? csrfToken : sessionScope.csrfToken}" />
                                                            <input type="hidden" name="applicationId" value="${app.applicationId}" />
                                                            <input type="hidden" name="jobId" value="${job.jobId}" />

                                                            <select name="status" class="form-control form-control-sm" style="width: auto; min-width: 130px;" required>
                                                                <option value="" disabled selected>Change to...</option>

                                                                <c:if test="${app.status == 'SUBMITTED'}">
                                                                    <option value="UNDER_REVIEW">Under Review</option>
                                                                    <option value="REJECTED">Rejected</option>
                                                                </c:if>

                                                                <c:if test="${app.status == 'UNDER_REVIEW'}">
                                                                    <option value="SHORTLISTED">Shortlisted</option>
                                                                    <option value="REJECTED">Rejected</option>
                                                                </c:if>

                                                                <c:if test="${app.status == 'SHORTLISTED'}">
                                                                    <option value="INTERVIEW">Interview</option>
                                                                    <option value="REJECTED">Rejected</option>
                                                                </c:if>

                                                                <c:if test="${app.status == 'INTERVIEW'}">
                                                                    <option value="SELECTED">Selected</option>
                                                                    <option value="REJECTED">Rejected</option>
                                                                </c:if>
                                                            </select>

                                                            <input type="text"
                                                                   name="notes"
                                                                   placeholder="Notes"
                                                                   class="form-control form-control-sm"
                                                                   style="width: 120px;"
                                                                   title="Optional reviewer notes" />

                                                            <button type="submit" class="btn btn-sm btn-primary" title="Update Application Status">
                                                                Save
                                                            </button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-right">
                                                <a href="${pageContext.request.contextPath}/employer/candidate?id=${app.candidateUserId}&applicationId=${app.applicationId}"
                                                   class="btn btn-sm btn-primary">
                                                    View Profile
                                                </a>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${matchedCount == 0}">
                                    <tr>
                                        <td colspan="7" class="text-center py-5">
                                            <div class="empty-state-title">No candidates match status "${filterStatus}"</div>
                                            <a href="${pageContext.request.contextPath}/employer/applicants?jobId=${job.jobId}" class="btn btn-sm btn-outline-primary mt-2">
                                                Reset Filter
                                            </a>
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <div class="empty-state-icon">👥</div>
                        <div class="empty-state-title">No applicants yet for this job</div>
                        <div class="empty-state-desc">
                            When job seekers submit their resumes for <c:out value="${job.title}" />, they will be listed here for screening and interviewing.
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp" />
