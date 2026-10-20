<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Server Error (500)" />
</jsp:include>

<div class="container" style="max-width: 680px; margin: 4rem auto; text-align: center;">
    <div class="card shadow" style="padding: 3rem 2rem;">
        <div style="font-size: 5rem; font-weight: 800; color: var(--color-danger); line-height: 1; margin-bottom: 1rem;">
            500
        </div>
        <h1 style="font-size: 1.75rem; margin-bottom: 0.75rem;">Internal Server Error</h1>
        <p class="text-muted" style="font-size: 1.05rem; margin-bottom: 2rem;">
            Something unexpected occurred on our servers while processing your request. Please rest assured that our team has been alerted.
        </p>

        <c:if test="${not empty pageContext.exception or not empty requestScope['jakarta.servlet.error.message']}">
            <div class="alert alert-danger text-left" style="font-size: 0.85rem; margin-bottom: 2rem; overflow-x: auto;">
                <strong>Diagnostic Information:</strong>
                <div>
                    <c:choose>
                        <c:when test="${not empty pageContext.exception.message}">
                            <c:out value="${pageContext.exception.message}" />
                        </c:when>
                        <c:when test="${not empty requestScope['jakarta.servlet.error.message']}">
                            <c:out value="${requestScope['jakarta.servlet.error.message']}" />
                        </c:when>
                        <c:otherwise>
                            An unexpected exception was encountered.
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:if>

        <div class="d-flex justify-center gap-2 flex-wrap">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                Return to Homepage
            </a>
            <button type="button" class="btn btn-outline-primary" onclick="window.location.reload();">
                Try Again
            </button>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp" />
