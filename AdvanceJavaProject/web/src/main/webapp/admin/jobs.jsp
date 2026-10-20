<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Moderate Jobs - Administrator"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <div>
            <h2 style="color: #1a237e; margin: 0;">Job Moderation</h2>
            <p class="text-muted" style="margin-top: 0.25rem;">Review and moderate public recruitment postings</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-primary">&larr; Back to Dashboard</a>
    </div>

    <!-- Filters -->
    <div class="card p-3 mb-4" style="background: #f8f9fa;">
        <form action="${pageContext.request.contextPath}/admin/jobs" method="GET" class="d-flex flex-wrap gap-2 align-center">
            <input type="text" name="keyword" class="form-control" placeholder="Search title..." value="${criteria.keyword}" style="flex: 2; min-width: 200px;">
            <select name="status" class="form-control" style="flex: 1; min-width: 150px;">
                <option value="">All Statuses</option>
                <option value="PUBLISHED" ${criteria.status == 'PUBLISHED' ? 'selected' : ''}>Published</option>
                <option value="DRAFT" ${criteria.status == 'DRAFT' ? 'selected' : ''}>Draft</option>
                <option value="CLOSED" ${criteria.status == 'CLOSED' ? 'selected' : ''}>Closed</option>
                <option value="DEACTIVATED" ${criteria.status == 'DEACTIVATED' ? 'selected' : ''}>Deactivated</option>
            </select>
            <button type="submit" class="btn btn-primary">Filter</button>
            <a href="${pageContext.request.contextPath}/admin/jobs" class="btn btn-secondary">Reset</a>
        </form>
    </div>

    <!-- Jobs Table -->
    <div class="card p-4">
        <div style="overflow-x: auto;">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Job Title</th>
                        <th>Company</th>
                        <th>Location</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="j" items="${jobs}">
                        <tr>
                            <td>#${j.jobId}</td>
                            <td><strong>${j.title}</strong></td>
                            <td>${j.companyName}</td>
                            <td>${j.location}</td>
                            <td><span class="badge badge-primary">${j.status}</span></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/jobs" method="POST" style="display: inline-block;">
                                    <input type="hidden" name="_csrf" value="${csrfToken}"/>
                                    <input type="hidden" name="jobId" value="${j.jobId}"/>
                                    <c:choose>
                                        <c:when test="${j.status == 'PUBLISHED'}">
                                            <input type="hidden" name="action" value="deactivate"/>
                                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Deactivate this job posting?');">Deactivate</button>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="action" value="activate"/>
                                            <button type="submit" class="btn btn-sm btn-success">Publish</button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <c:if test="${totalPages > 1}">
            <div class="d-flex justify-center mt-3">
                <my:pagination currentPage="${currentPage}" totalPages="${totalPages}" baseUrl="${pageContext.request.contextPath}/admin/jobs?keyword=${criteria.keyword}&status=${criteria.status}"/>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
