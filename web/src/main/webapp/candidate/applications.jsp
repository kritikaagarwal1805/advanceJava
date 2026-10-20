<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="My Applications - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <div>
            <h2 style="color: #1a237e; margin: 0;">My Job Applications</h2>
            <p class="text-muted" style="margin-top: 0.25rem;">Track real-time hiring decisions and recruitment stages</p>
        </div>
        <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary">Find More Jobs</a>
    </div>

    <div class="card p-4">
        <c:choose>
            <c:when test="${not empty applications}">
                <div style="overflow-x: auto;">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Role</th>
                                <th>Company</th>
                                <th>Submission Date</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="app" items="${applications}">
                                <tr>
                                    <td><strong>${app.jobTitle}</strong></td>
                                    <td>${app.companyName}</td>
                                    <td>${app.appliedAt}</td>
                                    <td><my:statusBadge status="${app.status.name()}"/></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/candidate/application-detail?id=${app.applicationId}" class="btn btn-sm btn-primary">View Status Details</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center p-5 text-muted">
                    <h4>No active applications</h4>
                    <p>You haven't submitted applications yet. Browse open listings and apply today!</p>
                    <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary mt-2">Browse Job Openings</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
