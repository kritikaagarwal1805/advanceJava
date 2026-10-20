<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="My Profile - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <h2 style="color: #1a237e; margin: 0;">Candidate Profile</h2>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/candidate/education" class="btn btn-outline-primary">Manage Education</a>
            <a href="${pageContext.request.contextPath}/candidate/skills" class="btn btn-outline-primary">Manage Skills</a>
            <a href="${pageContext.request.contextPath}/candidate/resume" class="btn btn-outline-primary">Resumes</a>
        </div>
    </div>

    <div class="card p-4">
        <form action="${pageContext.request.contextPath}/candidate/profile" method="POST">
            <input type="hidden" name="_csrf" value="${csrfToken}"/>

            <div class="form-group mb-3">
                <label class="form-label" for="headline">Professional Headline</label>
                <input type="text" id="headline" name="headline" class="form-control" placeholder="e.g. Full-Stack Java Engineer | Final Year B.Tech" value="${profile.headline}"/>
            </div>

            <div class="form-group mb-3">
                <label class="form-label" for="summary">About Me / Summary</label>
                <textarea id="summary" name="summary" class="form-control" rows="4" placeholder="Highlight your expertise, passion, and career aspirations...">${profile.summary}</textarea>
            </div>

            <div class="d-flex flex-wrap gap-3 mb-3">
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="phone">Phone Number</label>
                    <input type="text" id="phone" name="phone" class="form-control" placeholder="+91 9876543210" value="${profile.phone}"/>
                </div>
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="location">Current Location</label>
                    <input type="text" id="location" name="location" class="form-control" placeholder="Jaipur, Rajasthan" value="${profile.location}"/>
                </div>
            </div>

            <div class="d-flex flex-wrap gap-3 mb-4">
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="portfolioUrl">Portfolio / GitHub Website</label>
                    <input type="url" id="portfolioUrl" name="portfolioUrl" class="form-control" placeholder="https://github.com/username" value="${profile.portfolioUrl}"/>
                </div>
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="linkedinUrl">LinkedIn URL</label>
                    <input type="url" id="linkedinUrl" name="linkedinUrl" class="form-control" placeholder="https://linkedin.com/in/username" value="${profile.linkedinUrl}"/>
                </div>
            </div>

            <button type="submit" class="btn btn-primary" style="min-width: 150px;">Save Profile</button>
        </form>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
