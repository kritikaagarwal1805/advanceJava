<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Candidate Profile Review - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <a href="${pageContext.request.contextPath}/employer/applicants?jobId=${application.jobId}" class="text-muted" style="text-decoration: none; display: inline-block; margin-bottom: 1rem;">&larr; Back to Job Applicants</a>

    <div class="d-flex flex-wrap gap-4 align-items-start">
        <!-- Candidate Profile Details -->
        <div style="flex: 2; min-width: 320px;">
            <div class="card p-4 mb-4">
                <div class="d-flex justify-between align-center mb-2">
                    <h2 style="color: #1a237e; margin: 0;">${candidateUser.fullName}</h2>
                    <span class="badge badge-primary">${application.status}</span>
                </div>
                <div class="text-muted mb-3">${profile.headline != null ? profile.headline : 'Job Seeker Candidate'} &bull; ${profile.location}</div>

                <div class="d-flex flex-wrap gap-3 p-3 mb-4" style="background: #f8f9fa; border-radius: 6px;">
                    <div><strong>Email:</strong> ${candidateUser.email}</div>
                    <c:if test="${not empty profile.phone}"><div><strong>Phone:</strong> ${profile.phone}</div></c:if>
                    <c:if test="${not empty profile.portfolioUrl}">
                        <div><a href="${profile.portfolioUrl}" target="_blank" rel="noopener noreferrer">Portfolio</a></div>
                    </c:if>
                    <c:if test="${not empty profile.linkedinUrl}">
                        <div><a href="${profile.linkedinUrl}" target="_blank" rel="noopener noreferrer">LinkedIn</a></div>
                    </c:if>
                </div>

                <c:if test="${not empty profile.summary}">
                    <h4 style="color: #1a237e;">About</h4>
                    <p style="color: #444; line-height: 1.6;">${profile.summary}</p>
                </c:if>

                <h4 style="color: #1a237e; margin-top: 1.5rem;">Cover Note</h4>
                <div style="line-height: 1.6; color: #444; background: #fff; padding: 1rem; border: 1px solid #eee; border-radius: 6px; white-space: pre-line;">
                    <c:choose>
                        <c:when test="${not empty application.coverLetter}">${application.coverLetter}</c:when>
                        <c:otherwise><em>No cover note attached.</em></c:otherwise>
                    </c:choose>
                </div>

                <!-- Resume -->
                <c:if test="${resume != null}">
                    <h4 style="color: #1a237e; margin-top: 1.5rem;">Attached Resume</h4>
                    <div class="p-3 d-flex justify-between align-center" style="background: #e8eaf6; border-radius: 6px;">
                        <span>${resume.originalFilename} (${resume.fileSizeFormatted})</span>
                        <a href="${pageContext.request.contextPath}/candidate/resume?action=download&id=${resume.resumeId}" class="btn btn-sm btn-primary">Download Document</a>
                    </div>
                </c:if>

                <!-- Education -->
                <h4 style="color: #1a237e; margin-top: 1.5rem;">Academic Credentials</h4>
                <c:choose>
                    <c:when test="${not empty educationList}">
                        <div class="d-flex flex-column gap-2">
                            <c:forEach var="edu" items="${educationList}">
                                <div class="p-2" style="border-bottom: 1px solid #eee;">
                                    <strong>${edu.qualification} in ${edu.fieldOfStudy}</strong> &bull; ${edu.institution}
                                    <div class="text-muted" style="font-size: 0.85rem;">${edu.startDate} &mdash; ${edu.endDate != null ? edu.endDate : 'Present'} (${edu.grade})</div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise><p class="text-muted">No education records provided.</p></c:otherwise>
                </c:choose>

                <!-- Skills -->
                <h4 style="color: #1a237e; margin-top: 1.5rem;">Endorsed Skills</h4>
                <div class="d-flex flex-wrap gap-2">
                    <c:forEach var="sk" items="${candidateSkills}">
                        <span class="badge badge-secondary p-2" style="font-size: 0.85rem;">
                            ${sk.skillName} (${sk.proficiencyLevel})
                        </span>
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- Hiring Decision Workflow Form -->
        <div style="flex: 1; min-width: 260px;">
            <div class="card p-4">
                <h4 style="color: #1a237e; margin-bottom: 1rem;">Update Status</h4>
                <c:choose>
                    <c:when test="${not empty allowedTransitions}">
                        <form action="${pageContext.request.contextPath}/employer/application-status" method="POST">
                            <input type="hidden" name="_csrf" value="${csrfToken}"/>
                            <input type="hidden" name="applicationId" value="${application.applicationId}"/>
                            <input type="hidden" name="jobId" value="${application.jobId}"/>

                            <div class="form-group mb-3">
                                <label class="form-label" for="newStatus">Move Candidate To:</label>
                                <select id="newStatus" name="newStatus" class="form-control" required>
                                    <c:forEach var="st" items="${allowedTransitions}">
                                        <option value="${st.name()}">${st.name()}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group mb-3">
                                <label class="form-label" for="notes">Internal Recruiter Notes</label>
                                <textarea id="notes" name="notes" class="form-control" rows="3" placeholder="Reason or interview notes..."></textarea>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">Update Application</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">This application has reached a terminal status (<strong>${application.status}</strong>).</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
