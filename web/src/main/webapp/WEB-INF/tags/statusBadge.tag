<%@ tag description="Renders an application status badge" pageEncoding="UTF-8" %>
<%@ attribute name="status" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="cleanStatus" value="${not empty status ? status.trim().toUpperCase() : ''}" />

<c:choose>
    <c:when test="${cleanStatus == 'SUBMITTED'}">
        <span class="status-badge status-submitted">Submitted</span>
    </c:when>
    <c:when test="${cleanStatus == 'UNDER_REVIEW'}">
        <span class="status-badge status-under-review">Under Review</span>
    </c:when>
    <c:when test="${cleanStatus == 'SHORTLISTED'}">
        <span class="status-badge status-shortlisted">Shortlisted</span>
    </c:when>
    <c:when test="${cleanStatus == 'INTERVIEW'}">
        <span class="status-badge status-interview">Interview</span>
    </c:when>
    <c:when test="${cleanStatus == 'SELECTED'}">
        <span class="status-badge status-selected">Selected</span>
    </c:when>
    <c:when test="${cleanStatus == 'REJECTED'}">
        <span class="status-badge status-rejected">Rejected</span>
    </c:when>
    <c:when test="${cleanStatus == 'WITHDRAWN'}">
        <span class="status-badge status-withdrawn">Withdrawn</span>
    </c:when>
    <c:otherwise>
        <span class="badge badge-secondary"><c:out value="${status}" /></span>
    </c:otherwise>
</c:choose>
