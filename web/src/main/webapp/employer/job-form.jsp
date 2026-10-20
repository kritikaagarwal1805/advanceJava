<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="isEditing" value="${not empty job && not empty job.jobId}" />

<jsp:include page="/fragments/header.jsp">
    <jsp:param name="pageTitle" value="${isEditing ? 'Edit Job Opening' : 'Post New Job'} - CareerBridge" />
</jsp:include>
<jsp:include page="/fragments/messages.jsp" />

<div class="container page-content">
    <div style="max-width: 900px; margin: 0 auto; width: 100%;">
        <div class="d-flex justify-between align-center mb-4">
            <div>
                <h1 class="h2 mb-1">
                    <c:choose>
                        <c:when test="${isEditing}">Edit Job Posting</c:when>
                        <c:otherwise>Post a New Job</c:otherwise>
                    </c:choose>
                </h1>
                <p class="text-muted">
                    <c:choose>
                        <c:when test="${isEditing}">Update details, requirements, and status for this job vacancy.</c:when>
                        <c:otherwise>Create an exciting vacancy description to attract top candidates.</c:otherwise>
                    </c:choose>
                </p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/employer/jobs" class="btn btn-outline-secondary">
                    &larr; Back to Jobs
                </a>
            </div>
        </div>

        <div class="card shadow-sm">
            <div class="card-body p-4">
                <form action="${pageContext.request.contextPath}/employer/job-form" method="post" id="jobPostingForm">
                    <%-- CSRF Protection --%>
                    <input type="hidden" name="_csrf" value="${csrfToken != null ? csrfToken : sessionScope.csrfToken}" />

                    <%-- Hidden ID when editing --%>
                    <c:if test="${isEditing}">
                        <input type="hidden" name="jobId" value="${job.jobId}" />
                    </c:if>

                    <!-- Section 1: Basic Information -->
                    <h3 class="h4 mb-3 pb-2 border-bottom text-primary">1. Basic Details</h3>

                    <!-- Job Title -->
                    <div class="form-group mb-4">
                        <label for="jobTitle" class="form-label">Job Title <span class="required">*</span></label>
                        <input type="text"
                               id="jobTitle"
                               name="title"
                               class="form-control"
                               value="<c:out value='${job.title}' />"
                               placeholder="e.g. Senior Backend Java Engineer"
                               required
                               maxlength="200" />
                    </div>

                    <!-- Location & Work Mode -->
                    <div class="d-flex gap-4 flex-wrap mb-4">
                        <div class="form-group" style="flex: 2; min-width: 250px;">
                            <label for="location" class="form-label">Job Location <span class="required">*</span></label>
                            <input type="text"
                                   id="location"
                                   name="location"
                                   class="form-control"
                                   value="<c:out value='${job.location}' />"
                                   placeholder="e.g. Bengaluru, Karnataka, India"
                                   required
                                   maxlength="200" />
                        </div>

                        <div class="form-group" style="flex: 1; min-width: 200px;">
                            <label for="workMode" class="form-label">Work Mode <span class="required">*</span></label>
                            <select id="workMode" name="workMode" class="form-control" required>
                                <option value="ONSITE" ${job.workMode == 'ONSITE' ? 'selected' : ''}>Onsite</option>
                                <option value="REMOTE" ${job.workMode == 'REMOTE' ? 'selected' : ''}>Remote</option>
                                <option value="HYBRID" ${job.workMode == 'HYBRID' ? 'selected' : ''}>Hybrid</option>
                            </select>
                        </div>
                    </div>

                    <!-- Job Type & Experience Level -->
                    <div class="d-flex gap-4 flex-wrap mb-4">
                        <div class="form-group" style="flex: 1; min-width: 220px;">
                            <label for="jobType" class="form-label">Employment Type <span class="required">*</span></label>
                            <select id="jobType" name="jobType" class="form-control" required>
                                <option value="FULL_TIME" ${job.jobType == 'FULL_TIME' ? 'selected' : ''}>Full Time</option>
                                <option value="PART_TIME" ${job.jobType == 'PART_TIME' ? 'selected' : ''}>Part Time</option>
                                <option value="INTERNSHIP" ${job.jobType == 'INTERNSHIP' ? 'selected' : ''}>Internship</option>
                                <option value="CONTRACT" ${job.jobType == 'CONTRACT' ? 'selected' : ''}>Contract</option>
                            </select>
                        </div>

                        <div class="form-group" style="flex: 1; min-width: 220px;">
                            <label for="experienceLevel" class="form-label">Experience Level <span class="required">*</span></label>
                            <select id="experienceLevel" name="experienceLevel" class="form-control" required>
                                <option value="ENTRY" ${job.experienceLevel == 'ENTRY' ? 'selected' : ''}>Entry Level</option>
                                <option value="MID" ${job.experienceLevel == 'MID' ? 'selected' : ''}>Mid Level</option>
                                <option value="SENIOR" ${job.experienceLevel == 'SENIOR' ? 'selected' : ''}>Senior Level</option>
                                <option value="EXECUTIVE" ${job.experienceLevel == 'EXECUTIVE' ? 'selected' : ''}>Executive Level</option>
                            </select>
                        </div>
                    </div>

                    <!-- Compensation & Currency -->
                    <div class="d-flex gap-3 flex-wrap mb-4">
                        <div class="form-group" style="flex: 1; min-width: 140px;">
                            <label for="currency" class="form-label">Currency</label>
                            <select id="currency" name="currency" class="form-control">
                                <option value="INR" ${empty job.currency || job.currency == 'INR' ? 'selected' : ''}>INR (₹)</option>
                                <option value="USD" ${job.currency == 'USD' ? 'selected' : ''}>USD ($)</option>
                                <option value="EUR" ${job.currency == 'EUR' ? 'selected' : ''}>EUR (€)</option>
                                <option value="GBP" ${job.currency == 'GBP' ? 'selected' : ''}>GBP (£)</option>
                            </select>
                        </div>

                        <div class="form-group" style="flex: 2; min-width: 160px;">
                            <label for="salaryMin" class="form-label">Minimum Salary</label>
                            <input type="number"
                                   id="salaryMin"
                                   name="salaryMin"
                                   class="form-control"
                                   value="${job.salaryMin}"
                                   placeholder="e.g. 500000"
                                   min="0"
                                   step="1000" />
                        </div>

                        <div class="form-group" style="flex: 2; min-width: 160px;">
                            <label for="salaryMax" class="form-label">Maximum Salary</label>
                            <input type="number"
                                   id="salaryMax"
                                   name="salaryMax"
                                   class="form-control"
                                   value="${job.salaryMax}"
                                   placeholder="e.g. 900000"
                                   min="0"
                                   step="1000" />
                        </div>
                    </div>

                    <!-- Deadline & Status -->
                    <div class="d-flex gap-4 flex-wrap mb-5">
                        <div class="form-group" style="flex: 1; min-width: 220px;">
                            <label for="applicationDeadline" class="form-label">Application Deadline</label>
                            <input type="date"
                                   id="applicationDeadline"
                                   name="applicationDeadline"
                                   class="form-control"
                                   value="${job.applicationDeadline}" />
                            <span class="form-hint">Leave blank if applications are accepted continuously.</span>
                        </div>

                        <div class="form-group" style="flex: 1; min-width: 220px;">
                            <label for="status" class="form-label">Job Status <span class="required">*</span></label>
                            <select id="status" name="status" class="form-control" required>
                                <c:choose>
                                    <c:when test="${isEditing}">
                                        <option value="DRAFT" ${job.status == 'DRAFT' ? 'selected' : ''}>Draft (Hidden from candidates)</option>
                                        <option value="PUBLISHED" ${job.status == 'PUBLISHED' ? 'selected' : ''}>Published (Active for applications)</option>
                                        <option value="CLOSED" ${job.status == 'CLOSED' ? 'selected' : ''}>Closed (Applications paused)</option>
                                        <option value="DEACTIVATED" ${job.status == 'DEACTIVATED' ? 'selected' : ''}>Deactivated</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="DRAFT" ${empty job.status || job.status == 'DRAFT' ? 'selected' : ''}>Draft (Save without publishing)</option>
                                        <option value="PUBLISHED" ${job.status == 'PUBLISHED' ? 'selected' : ''}>Published (Publish immediately)</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                    </div>

                    <!-- Section 2: Job Content & Description -->
                    <h3 class="h4 mb-3 pb-2 border-bottom text-primary">2. Role Description & Criteria</h3>

                    <!-- Job Description -->
                    <div class="form-group mb-4">
                        <label for="description" class="form-label">Job Description <span class="required">*</span></label>
                        <textarea id="description"
                                  name="description"
                                  class="form-control"
                                  rows="7"
                                  placeholder="Provide an overview of the role, team context, objectives, and impact..."
                                  required><c:out value="${job.description}" /></textarea>
                        <span class="form-hint">Describe the mission of this role and why candidates should be interested.</span>
                    </div>

                    <!-- Responsibilities -->
                    <div class="form-group mb-4">
                        <label for="responsibilities" class="form-label">Key Responsibilities</label>
                        <textarea id="responsibilities"
                                  name="responsibilities"
                                  class="form-control"
                                  rows="6"
                                  placeholder="- Design, develop, and maintain high-performance software modules
- Collaborate with product managers and cross-functional teams
- Conduct code reviews and ensure quality standards..."><c:out value="${job.responsibilities}" /></textarea>
                        <span class="form-hint">List specific day-to-day duties and core tasks.</span>
                    </div>

                    <!-- Requirements -->
                    <div class="form-group mb-5">
                        <label for="requirements" class="form-label">Requirements & Qualifications</label>
                        <textarea id="requirements"
                                  name="requirements"
                                  class="form-control"
                                  rows="6"
                                  placeholder="- Bachelor's degree in Computer Science or related field
- 3+ years experience in Core Java, Spring/Jakarta EE, and SQL
- Strong understanding of RESTful API architecture..."><c:out value="${job.requirements}" /></textarea>
                        <span class="form-hint">List required education, technical skills, years of experience, and nice-to-have capabilities.</span>
                    </div>

                    <!-- Form Controls -->
                    <div class="d-flex justify-between align-center pt-3 border-top">
                        <a href="${pageContext.request.contextPath}/employer/jobs" class="btn btn-outline-secondary">
                            Cancel
                        </a>
                        <button type="submit" class="btn btn-primary btn-lg">
                            <c:choose>
                                <c:when test="${isEditing}">
                                    💾 Update Job Posting
                                </c:when>
                                <c:otherwise>
                                    🚀 Save & Continue
                                </c:otherwise>
                            </c:choose>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp" />
