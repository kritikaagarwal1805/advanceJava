<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Success Alert --%>
<c:if test="${not empty requestScope.success}">
    <div class="alert alert-success" role="alert">
        <div class="alert-content">
            <span style="font-weight: 700; margin-right: 6px;">&#10003;</span>
            <span><c:out value="${requestScope.success}" /></span>
        </div>
        <button type="button" class="alert-close" aria-label="Close message">&times;</button>
    </div>
</c:if>

<%-- Error Alert --%>
<c:if test="${not empty requestScope.error}">
    <div class="alert alert-danger" role="alert">
        <div class="alert-content">
            <span style="font-weight: 700; margin-right: 6px;">&#9888;</span>
            <span><c:out value="${requestScope.error}" /></span>
        </div>
        <button type="button" class="alert-close" aria-label="Close message">&times;</button>
    </div>
</c:if>

<%-- Info Alert --%>
<c:if test="${not empty requestScope.info}">
    <div class="alert alert-info" role="alert">
        <div class="alert-content">
            <span style="font-weight: 700; margin-right: 6px;">&#8505;</span>
            <span><c:out value="${requestScope.info}" /></span>
        </div>
        <button type="button" class="alert-close" aria-label="Close message">&times;</button>
    </div>
</c:if>

<%-- Warning Alert --%>
<c:if test="${not empty requestScope.warning}">
    <div class="alert alert-warning" role="alert">
        <div class="alert-content">
            <span style="font-weight: 700; margin-right: 6px;">&#9888;</span>
            <span><c:out value="${requestScope.warning}" /></span>
        </div>
        <button type="button" class="alert-close" aria-label="Close message">&times;</button>
    </div>
</c:if>
