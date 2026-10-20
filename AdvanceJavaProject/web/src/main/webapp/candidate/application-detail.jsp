<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Application Status - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <a href="${pageContext.request.contextPath}/candidate/applications" class="text-muted" style="text-decoration: none; display: inline-block; margin-bottom: 1rem;">&larr; Back to My Applications</a>

    <div class="d-flex flex-wrap gap-4 align-items-start">
        <!-- Application Overview -->
        <div style="flex: 2; min-width: 320px;">
            <div class="card p-4 mb-4">
                <div class="d-flex justify-between align-center mb-2">
                    <h2 style="color: #1a237e; margin: 0;">${application.jobTitle}</h2>
                    <my:statusBadge status="${application.status.name()}"/>
                </div>
                <div class="text-muted font-weight-500 mb-3" style="font-size: 1.1rem;">${application.companyName}</div>

                <div class="p-3 mb-4" style="background: #f8f9fa; border-radius: 6px;">
                    <div><strong>Submitted On:</strong> ${application.appliedAt}</div>
                    <c:if test="${resume != null}">
                        <div class="mt-1">
                            <strong>Attached Resume:</strong>
                            <a href="${pageContext.request.contextPath}/candidate/resume?action=download&id=${resume.resumeId}" style="color: #1565C0;">
                                ${resume.originalFilename}
                            </a>
                        </div>
                    </c:if>
                </div>

                <h4 style="color: #1a237e;">Cover Note</h4>
                <div style="line-height: 1.6; color: #444; background: #fff; padding: 1rem; border: 1px solid #eee; border-radius: 6px; white-space: pre-line;">
                    <c:choose>
                        <c:when test="${not empty application.coverLetter}">${application.coverLetter}</c:when>
                        <c:otherwise><em>No cover note attached.</em></c:otherwise>
                    </c:choose>
                </div>

                <!-- Status Transition History Timeline -->
                <h4 style="color: #1a237e; margin-top: 2rem; margin-bottom: 1rem;">Audit & Transition Timeline</h4>
                <div class="d-flex flex-column gap-2">
                    <c:forEach var="item" items="${statusHistory}">
                        <div class="p-3" style="border-left: 3px solid #1565C0; background: #fdfdfd; box-shadow: 0 1px 3px rgba(0,0,0,0.05);">
                            <div class="d-flex justify-between align-center">
                                <strong>Status: ${item.newStatus}</strong>
                                <span class="text-muted" style="font-size: 0.8rem;">${item.changedAt}</span>
                            </div>
                            <c:if test="${not empty item.notes}">
                                <div style="font-size: 0.85rem; color: #555; margin-top: 0.25rem;">Note: ${item.notes}</div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- Actions Sidebar -->
        <div style="flex: 1; min-width: 250px;">
            <div class="card p-4">
                <h4 style="color: #1a237e; margin-bottom: 1rem;">Manage Application</h4>
                <c:choose>
                    <c:when test="${canWithdraw}">
                        <p class="text-muted" style="font-size: 0.9rem;">You can withdraw this application if you no longer wish to be considered.</p>
                        <form action="${pageContext.request.contextPath}/candidate/application-detail" method="POST" onsubmit="return confirm('Are you sure you want to withdraw this application? This action cannot be undone.');">
                            <input type="hidden" name="_csrf" value="${csrfToken}"/>
                            <input type="hidden" name="action" value="withdraw"/>
                            <input type="hidden" name="applicationId" value="${application.applicationId}"/>
                            <button type="submit" class="btn btn-danger w-100">Withdraw Application</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted" style="font-size: 0.9rem;">This application is in <strong>${application.status}</strong> status and cannot be withdrawn.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
