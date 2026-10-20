<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="${job.title} - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <a href="${pageContext.request.contextPath}/jobs" class="text-muted" style="text-decoration: none; display: inline-block; margin-bottom: 1rem;">&larr; Back to all listings</a>

    <div class="d-flex flex-wrap gap-4 align-items-start">
        <!-- Main Job Body -->
        <div style="flex: 2; min-width: 320px;">
            <div class="card p-4 mb-4">
                <div class="d-flex justify-between align-center mb-2">
                    <span class="badge badge-primary">${job.jobType}</span>
                    <span class="text-muted">${job.workMode} &bull; ${job.experienceLevel}</span>
                </div>
                <h2 style="color: #1a237e; margin-bottom: 0.5rem;">${job.title}</h2>
                <div class="text-muted font-weight-500 mb-3" style="font-size: 1.1rem;">
                    <strong>${company.name}</strong> &bull; ${job.location}
                </div>

                <div class="d-flex flex-wrap gap-3 p-3 mb-4" style="background: #f8f9fa; border-radius: 6px;">
                    <div><strong>Compensation:</strong> <c:choose><c:when test="${job.salaryMin != null}">₹${job.salaryMin} - ₹${job.salaryMax} / yr</c:when><c:otherwise>Undisclosed</c:otherwise></c:choose></div>
                    <div><strong>Deadline:</strong> <c:choose><c:when test="${job.applicationDeadline != null}">${job.applicationDeadline}</c:when><c:otherwise>Open until filled</c:otherwise></c:choose></div>
                </div>

                <h4 style="color: #1a237e; margin-top: 1rem;">Job Description</h4>
                <div style="line-height: 1.6; color: #333; margin-bottom: 1.5rem; white-space: pre-line;">${job.description}</div>

                <c:if test="${not empty job.requirements}">
                    <h4 style="color: #1a237e;">Requirements & Skills</h4>
                    <div style="line-height: 1.6; color: #333; margin-bottom: 1.5rem; white-space: pre-line;">${job.requirements}</div>
                </c:if>

                <c:if test="${not empty job.responsibilities}">
                    <h4 style="color: #1a237e;">Core Responsibilities</h4>
                    <div style="line-height: 1.6; color: #333; margin-bottom: 1.5rem; white-space: pre-line;">${job.responsibilities}</div>
                </c:if>
            </div>
        </div>

        <!-- Sidebar / Application Panel -->
        <div style="flex: 1; min-width: 280px;">
            <div class="card p-4 mb-4">
                <h4 style="color: #1a237e; margin-bottom: 1rem;">Apply for this Position</h4>

                <c:choose>
                    <c:when test="${hasApplied}">
                        <div class="alert alert-info">
                            <strong>Status:</strong> You have already submitted an application for this position.
                        </div>
                        <a href="${pageContext.request.contextPath}/candidate/applications" class="btn btn-outline-primary w-100">View Application Status</a>
                    </c:when>

                    <c:when test="${canApply}">
                        <form action="${pageContext.request.contextPath}/candidate/apply" method="POST">
                            <input type="hidden" name="_csrf" value="${csrfToken}"/>
                            <input type="hidden" name="jobId" value="${job.jobId}"/>

                            <div class="form-group mb-3">
                                <label class="form-label" for="resumeId">Select Saved Resume</label>
                                <c:choose>
                                    <c:when test="${not empty resumes}">
                                        <select name="resumeId" id="resumeId" class="form-control" required>
                                            <c:forEach var="res" items="${resumes}">
                                                <option value="${res.resumeId}">${res.originalFilename} (${res.fileSizeFormatted})</option>
                                            </c:forEach>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted" style="font-size: 0.85rem;">No resumes on file yet.</p>
                                        <a href="${pageContext.request.contextPath}/candidate/resume" class="btn btn-sm btn-secondary mb-2">Upload Resume First</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="form-group mb-3">
                                <label class="form-label" for="coverLetter">Cover Letter / Note</label>
                                <textarea name="coverLetter" id="coverLetter" class="form-control" rows="4" placeholder="Briefly describe why you are a great fit..."></textarea>
                            </div>

                            <button type="submit" class="btn btn-success w-100" ${empty resumes ? 'disabled' : ''}>Submit Application</button>
                        </form>
                    </c:when>

                    <c:when test="${sessionScope.user == null}">
                        <p class="text-muted">Sign in with a candidate account to apply directly for this job.</p>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary w-100">Log in to Apply</a>
                    </c:when>

                    <c:otherwise>
                        <div class="alert alert-warning">
                            Recruiters and Admins cannot submit candidate applications.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Company Card -->
            <c:if test="${company != null}">
                <div class="card p-3">
                    <h5 style="color: #1a237e;">About ${company.name}</h5>
                    <p class="text-muted" style="font-size: 0.9rem;">${company.industry} &bull; ${company.location}</p>
                    <p style="font-size: 0.85rem; color: #555;">${company.description}</p>
                    <c:if test="${not empty company.website}">
                        <a href="${company.website}" target="_blank" rel="noopener noreferrer" style="font-size: 0.85rem; color: #1565C0;">Visit Company Website &rarr;</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
