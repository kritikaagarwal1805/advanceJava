<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Page Not Found (404)" />
</jsp:include>

<div class="container" style="max-width: 680px; margin: 4rem auto; text-align: center;">
    <div class="card shadow" style="padding: 3rem 2rem;">
        <div style="font-size: 5rem; font-weight: 800; color: var(--accent-blue); line-height: 1; margin-bottom: 1rem;">
            404
        </div>
        <h1 style="font-size: 1.75rem; margin-bottom: 0.75rem;">Page Not Found</h1>
        <p class="text-muted" style="font-size: 1.05rem; margin-bottom: 2rem;">
            Sorry, the page you are looking for doesn't exist, has been removed, or has had its address changed.
        </p>

        <div style="margin-bottom: 2rem;">
            <form action="${pageContext.request.contextPath}/jobs" method="GET" class="search-bar" style="max-width: 440px; margin: 0 auto;">
                <span class="search-icon">&#128269;</span>
                <input type="text" name="q" class="search-input" placeholder="Search jobs by keyword, title, skill...">
            </form>
        </div>

        <div class="d-flex justify-center gap-2 flex-wrap">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                Return to Homepage
            </a>
            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline-primary">
                Explore Available Jobs
            </a>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp" />
