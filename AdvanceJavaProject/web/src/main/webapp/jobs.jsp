<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Explore Jobs & Internships - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <!-- Top Filter Header -->
    <div class="card p-3 mb-4" style="background: #f8f9fa;">
        <form action="${pageContext.request.contextPath}/jobs" method="GET" class="d-flex flex-wrap gap-2 align-center">
            <input type="text" name="keyword" class="form-control" placeholder="Search title or keywords..." value="${criteria.keyword}" style="flex: 2; min-width: 180px;">
            <input type="text" name="location" class="form-control" placeholder="Location..." value="${criteria.location}" style="flex: 1; min-width: 140px;">
            <select name="jobType" class="form-control" style="flex: 1; min-width: 130px;">
                <option value="">All Types</option>
                <option value="FULL_TIME" ${criteria.jobType == 'FULL_TIME' ? 'selected' : ''}>Full Time</option>
                <option value="PART_TIME" ${criteria.jobType == 'PART_TIME' ? 'selected' : ''}>Part Time</option>
                <option value="INTERNSHIP" ${criteria.jobType == 'INTERNSHIP' ? 'selected' : ''}>Internship</option>
                <option value="CONTRACT" ${criteria.jobType == 'CONTRACT' ? 'selected' : ''}>Contract</option>
            </select>
            <select name="workMode" class="form-control" style="flex: 1; min-width: 130px;">
                <option value="">All Modes</option>
                <option value="ONSITE" ${criteria.workMode == 'ONSITE' ? 'selected' : ''}>Onsite</option>
                <option value="REMOTE" ${criteria.workMode == 'REMOTE' ? 'selected' : ''}>Remote</option>
                <option value="HYBRID" ${criteria.workMode == 'HYBRID' ? 'selected' : ''}>Hybrid</option>
            </select>
            <select name="experienceLevel" class="form-control" style="flex: 1; min-width: 130px;">
                <option value="">All Experience</option>
                <option value="ENTRY" ${criteria.experienceLevel == 'ENTRY' ? 'selected' : ''}>Entry Level</option>
                <option value="MID" ${criteria.experienceLevel == 'MID' ? 'selected' : ''}>Mid Level</option>
                <option value="SENIOR" ${criteria.experienceLevel == 'SENIOR' ? 'selected' : ''}>Senior Level</option>
                <option value="EXECUTIVE" ${criteria.experienceLevel == 'EXECUTIVE' ? 'selected' : ''}>Executive</option>
            </select>
            <button type="submit" class="btn btn-primary" style="min-width: 100px;">Filter</button>
            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-secondary" style="min-width: 80px;">Reset</a>
        </form>
    </div>

    <div class="d-flex justify-between align-center mb-3">
        <h3 style="margin: 0; color: #1a237e;">Available Opportunities</h3>
        <span class="text-muted">Showing <strong>${jobs.size()}</strong> of <strong>${totalResults}</strong> postings</span>
    </div>

    <!-- Job Cards -->
    <div class="d-flex flex-wrap gap-3 mb-4">
        <c:choose>
            <c:when test="${not empty jobs}">
                <c:forEach var="job" items="${jobs}">
                    <div class="job-card" style="flex: 1; min-width: 320px; max-width: 380px;">
                        <div class="d-flex justify-between align-center mb-2">
                            <span class="badge badge-primary">${job.jobType}</span>
                            <span class="text-muted" style="font-size: 0.85rem;">${job.workMode} &bull; ${job.experienceLevel}</span>
                        </div>
                        <h4 style="margin: 0.5rem 0;">
                            <a href="${pageContext.request.contextPath}/job-details?id=${job.jobId}" style="text-decoration: none; color: #1a237e;">${job.title}</a>
                        </h4>
                        <div class="text-muted mb-2 font-weight-500">${job.companyName} &bull; ${job.location}</div>
                        <p style="font-size: 0.9rem; color: #555; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; margin-bottom: 1rem;">
                            ${job.description}
                        </p>
                        <div class="d-flex justify-between align-center pt-2" style="border-top: 1px solid #eee;">
                            <span style="font-weight: 600; color: #2e7d32;">
                                <c:choose>
                                    <c:when test="${job.salaryMin != null}">₹${job.salaryMin} - ₹${job.salaryMax}</c:when>
                                    <c:otherwise>Competitive</c:otherwise>
                                </c:choose>
                            </span>
                            <a href="${pageContext.request.contextPath}/job-details?id=${job.jobId}" class="btn btn-sm btn-primary">View Details</a>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="card w-100 p-5 text-center text-muted">
                    <h4>No job postings match your criteria</h4>
                    <p>Try clearing some search filters or searching for different keywords.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Pagination -->
    <c:if test="${totalPages > 1}">
        <div class="d-flex justify-center mt-4">
            <my:pagination currentPage="${currentPage}" totalPages="${totalPages}" baseUrl="${pageContext.request.contextPath}/jobs?keyword=${criteria.keyword}&location=${criteria.location}&jobType=${criteria.jobType}&workMode=${criteria.workMode}&experienceLevel=${criteria.experienceLevel}"/>
        </div>
    </c:if>
</div>

<jsp:include page="/fragments/footer.jsp"/>
