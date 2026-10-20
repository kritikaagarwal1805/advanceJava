<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="CareerBridge - Connect to Your Next Opportunity"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4">
    <!-- Hero Section -->
    <div class="card mb-4" style="background: linear-gradient(135deg, #1a237e 0%, #0d47a1 100%); color: white; padding: 2.5rem; border-radius: 8px;">
        <h1 style="font-weight: 700; margin-bottom: 0.5rem;">Find Your Dream Career & Internship</h1>
        <p style="font-size: 1.15rem; opacity: 0.9; margin-bottom: 2rem;">Connecting passionate candidates with verified industry employers across India.</p>

        <form action="${pageContext.request.contextPath}/jobs" method="GET" class="search-bar d-flex flex-wrap gap-2" style="background: rgba(255,255,255,0.15); padding: 1rem; border-radius: 6px;">
            <input type="text" name="keyword" class="form-control" placeholder="Job title, keywords, or skills..." style="flex: 2; min-width: 200px;">
            <input type="text" name="location" class="form-control" placeholder="Location or Remote..." style="flex: 1; min-width: 150px;">
            <button type="submit" class="btn btn-primary" style="min-width: 120px;">Search Jobs</button>
        </form>
    </div>

    <!-- Quick Stats -->
    <div class="d-flex flex-wrap gap-3 mb-4 justify-between">
        <div class="stat-card" style="flex: 1; min-width: 200px; text-align: center;">
            <div style="font-size: 2rem; font-weight: bold; color: #1565C0;">${totalJobs != null ? totalJobs : '10+'}</div>
            <div class="text-muted">Active Job Postings</div>
        </div>
        <div class="stat-card" style="flex: 1; min-width: 200px; text-align: center;">
            <div style="font-size: 2rem; font-weight: bold; color: #2e7d32;">${totalCompanies != null ? totalCompanies : '5+'}</div>
            <div class="text-muted">Registered Companies</div>
        </div>
        <div class="stat-card" style="flex: 1; min-width: 200px; text-align: center;">
            <div style="font-size: 2rem; font-weight: bold; color: #e65100;">100%</div>
            <div class="text-muted">Verified Recruiters</div>
        </div>
    </div>

    <!-- Featured Postings -->
    <div class="d-flex justify-between align-center mb-3">
        <h2>Latest Opportunities</h2>
        <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline-primary">View All Jobs &rarr;</a>
    </div>

    <div class="d-flex flex-wrap gap-3 mb-5">
        <c:choose>
            <c:when test="${not empty recentJobs}">
                <c:forEach var="job" items="${recentJobs}">
                    <div class="job-card" style="flex: 1; min-width: 320px; max-width: 380px;">
                        <div class="d-flex justify-between align-center mb-2">
                            <span class="badge badge-primary">${job.jobType}</span>
                            <span class="text-muted" style="font-size: 0.85rem;">${job.workMode}</span>
                        </div>
                        <h4 style="margin: 0.5rem 0;"><a href="${pageContext.request.contextPath}/job-details?id=${job.jobId}" style="text-decoration: none; color: #1a237e;">${job.title}</a></h4>
                        <div class="text-muted mb-2">${job.companyName != null ? job.companyName : 'Tech Enterprise'} &bull; ${job.location}</div>
                        <p style="font-size: 0.9rem; color: #555; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                            ${job.description}
                        </p>
                        <div class="d-flex justify-between align-center mt-3 pt-2" style="border-top: 1px solid #eee;">
                            <span style="font-weight: 600; color: #2e7d32;">
                                <c:if test="${job.salaryMin != null}">₹${job.salaryMin} - ₹${job.salaryMax}</c:if>
                            </span>
                            <a href="${pageContext.request.contextPath}/job-details?id=${job.jobId}" class="btn btn-sm btn-primary">Details</a>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="card w-100 p-4 text-center text-muted">
                    <p>No job listings active at the moment. Please check back shortly!</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
