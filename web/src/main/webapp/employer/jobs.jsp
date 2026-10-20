<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/fragments/header.jsp">
    <jsp:param name="pageTitle" value="My Job Postings - CareerBridge" />
</jsp:include>
<jsp:include page="/fragments/messages.jsp" />

<div class="container page-content">
    <div class="d-flex justify-between align-center flex-wrap gap-3 mb-4">
        <div>
            <h1 class="h2 mb-1">My Job Postings</h1>
            <p class="text-muted">Manage all vacancies, track applications, and change job visibility statuses.</p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/employer/job-form" class="btn btn-primary">
                <span>＋</span> Post New Job
            </a>
        </div>
    </div>

    <!-- Status Filter Navigation -->
    <div class="card mb-4">
        <div class="card-body py-2 px-3">
            <div class="d-flex align-center justify-between flex-wrap gap-2">
                <div class="d-flex gap-2 flex-wrap">
                    <c:set var="currentStatus" value="${not empty param.status ? param.status : 'ALL'}" />
                    <a href="${pageContext.request.contextPath}/employer/jobs"
                       class="btn btn-sm ${currentStatus == 'ALL' ? 'btn-primary' : 'btn-outline-secondary'}">
                        All Jobs
                    </a>
                    <a href="${pageContext.request.contextPath}/employer/jobs?status=PUBLISHED"
                       class="btn btn-sm ${currentStatus == 'PUBLISHED' ? 'btn-primary' : 'btn-outline-secondary'}">
                        Published
                    </a>
                    <a href="${pageContext.request.contextPath}/employer/jobs?status=DRAFT"
                       class="btn btn-sm ${currentStatus == 'DRAFT' ? 'btn-primary' : 'btn-outline-secondary'}">
                        Drafts
                    </a>
                    <a href="${pageContext.request.contextPath}/employer/jobs?status=CLOSED"
                       class="btn btn-sm ${currentStatus == 'CLOSED' ? 'btn-primary' : 'btn-outline-secondary'}">
                        Closed
                    </a>
                    <a href="${pageContext.request.contextPath}/employer/jobs?status=DEACTIVATED"
                       class="btn btn-sm ${currentStatus == 'DEACTIVATED' ? 'btn-primary' : 'btn-outline-secondary'}">
                        Deactivated
                    </a>
                </div>

                <c:if test="${not empty jobs}">
                    <span class="text-muted font-size-xs">Showing jobs</span>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Jobs Table / Card List -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty jobs}">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Job Title</th>
                                    <th>Status</th>
                                    <th>Applications</th>
                                    <th>Created Date</th>
                                    <th>Deadline</th>
                                    <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="renderedCount" value="0" />
                                <c:forEach var="job" items="${jobs}">
                                    <c:if test="${currentStatus == 'ALL' || job.status == currentStatus}">
                                        <c:set var="renderedCount" value="${renderedCount + 1}" />
                                        <tr>
                                            <td>
                                                <div class="font-weight-600 font-size-base">
                                                    <a href="${pageContext.request.contextPath}/employer/job-form?id=${job.jobId}" class="text-primary">
                                                        <c:out value="${job.title}" />
                                                    </a>
                                                </div>
                                                <div class="text-muted font-size-xs mt-1">
                                                    <span>📍 <c:out value="${not empty job.location ? job.location : 'Any location'}" /></span>
                                                    <span class="ml-2">💼 ${job.jobType}</span>
                                                    <span class="ml-2">🏠 ${job.workMode}</span>
                                                </div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${job.status == 'PUBLISHED'}">
                                                        <span class="badge badge-success">PUBLISHED</span>
                                                    </c:when>
                                                    <c:when test="${job.status == 'DRAFT'}">
                                                        <span class="badge badge-secondary">DRAFT</span>
                                                    </c:when>
                                                    <c:when test="${job.status == 'CLOSED'}">
                                                        <span class="badge badge-warning">CLOSED</span>
                                                    </c:when>
                                                    <c:when test="${job.status == 'DEACTIVATED'}">
                                                        <span class="badge badge-danger">DEACTIVATED</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-secondary">${job.status}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:set var="appCount" value="${job.applicationCount != null ? job.applicationCount : (applicationCounts[job.jobId] != null ? applicationCounts[job.jobId] : 0)}" />
                                                <a href="${pageContext.request.contextPath}/employer/applicants?jobId=${job.jobId}" class="badge ${appCount > 0 ? 'badge-primary' : 'badge-secondary'}" style="text-decoration: none; font-size: 0.85rem; padding: 0.35rem 0.65rem;">
                                                    👥 ${appCount} applicant${appCount == 1 ? '' : 's'}
                                                </a>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty job.createdAt}">
                                                        ${job.createdAt}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">-</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty job.applicationDeadline}">
                                                        ${job.applicationDeadline}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">No deadline</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-right">
                                                <div class="d-flex justify-end align-center gap-1 flex-wrap">
                                                    <%-- View Applicants --%>
                                                    <a href="${pageContext.request.contextPath}/employer/applicants?jobId=${job.jobId}"
                                                       class="btn btn-sm btn-outline-primary"
                                                       title="View Candidates">
                                                        Applicants
                                                    </a>

                                                    <%-- Edit Job --%>
                                                    <a href="${pageContext.request.contextPath}/employer/job-form?id=${job.jobId}"
                                                       class="btn btn-sm btn-outline-secondary"
                                                       title="Edit Job Details">
                                                        Edit
                                                    </a>

                                                    <%-- Status Transitions --%>
                                                    <c:choose>
                                                        <%-- If DRAFT: Can Publish --%>
                                                        <c:when test="${job.status == 'DRAFT'}">
                                                            <form action="${pageContext.request.contextPath}/employer/jobs" method="post" style="display:inline;">
                                                                <input type="hidden" name="_csrf" value="${csrfToken != null ? csrfToken : sessionScope.csrfToken}" />
                                                                <input type="hidden" name="action" value="updateStatus" />
                                                                <input type="hidden" name="jobId" value="${job.jobId}" />
                                                                <input type="hidden" name="status" value="PUBLISHED" />
                                                                <button type="submit" class="btn btn-sm btn-success" title="Publish Job Opening">
                                                                    Publish
                                                                </button>
                                                            </form>
                                                        </c:when>

                                                        <%-- If PUBLISHED: Can Close or Deactivate --%>
                                                        <c:when test="${job.status == 'PUBLISHED'}">
                                                            <form action="${pageContext.request.contextPath}/employer/jobs" method="post" style="display:inline;">
                                                                <input type="hidden" name="_csrf" value="${csrfToken != null ? csrfToken : sessionScope.csrfToken}" />
                                                                <input type="hidden" name="action" value="updateStatus" />
                                                                <input type="hidden" name="jobId" value="${job.jobId}" />
                                                                <input type="hidden" name="status" value="CLOSED" />
                                                                <button type="submit" class="btn btn-sm btn-warning" onclick="return confirm('Are you sure you want to close applications for this job?');">
                                                                    Close
                                                                </button>
                                                            </form>
                                                            <form action="${pageContext.request.contextPath}/employer/jobs" method="post" style="display:inline;">
                                                                <input type="hidden" name="_csrf" value="${csrfToken != null ? csrfToken : sessionScope.csrfToken}" />
                                                                <input type="hidden" name="action" value="updateStatus" />
                                                                <input type="hidden" name="jobId" value="${job.jobId}" />
                                                                <input type="hidden" name="status" value="DEACTIVATED" />
                                                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Deactivate this job? It will no longer be visible.');">
                                                                    Deactivate
                                                                </button>
                                                            </form>
                                                        </c:when>

                                                        <%-- If CLOSED: Can Re-Publish --%>
                                                        <c:when test="${job.status == 'CLOSED'}">
                                                            <form action="${pageContext.request.contextPath}/employer/jobs" method="post" style="display:inline;">
                                                                <input type="hidden" name="_csrf" value="${csrfToken != null ? csrfToken : sessionScope.csrfToken}" />
                                                                <input type="hidden" name="action" value="updateStatus" />
                                                                <input type="hidden" name="jobId" value="${job.jobId}" />
                                                                <input type="hidden" name="status" value="PUBLISHED" />
                                                                <button type="submit" class="btn btn-sm btn-success">
                                                                    Re-open
                                                                </button>
                                                            </form>
                                                        </c:when>

                                                        <%-- If DEACTIVATED: Can Reactivate / Publish --%>
                                                        <c:when test="${job.status == 'DEACTIVATED'}">
                                                            <form action="${pageContext.request.contextPath}/employer/jobs" method="post" style="display:inline;">
                                                                <input type="hidden" name="_csrf" value="${csrfToken != null ? csrfToken : sessionScope.csrfToken}" />
                                                                <input type="hidden" name="action" value="updateStatus" />
                                                                <input type="hidden" name="jobId" value="${job.jobId}" />
                                                                <input type="hidden" name="status" value="PUBLISHED" />
                                                                <button type="submit" class="btn btn-sm btn-success" onclick="return confirm('Reactivate and publish this job opening?');">
                                                                    Reactivate
                                                                </button>
                                                            </form>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${renderedCount == 0}">
                                    <tr>
                                        <td colspan="6" class="text-center py-5">
                                            <div class="empty-state-title">No jobs matching filter "${currentStatus}"</div>
                                            <a href="${pageContext.request.contextPath}/employer/jobs" class="btn btn-sm btn-outline-primary mt-2">
                                                View All Jobs
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
                        <div class="empty-state-icon">📄</div>
                        <div class="empty-state-title">No job postings created yet</div>
                        <div class="empty-state-desc">
                            Create your first job posting to start finding top talents for your business.
                        </div>
                        <a href="${pageContext.request.contextPath}/employer/job-form" class="btn btn-primary">
                            <span>＋</span> Create First Job
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp" />
