<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Manage Resumes - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <div>
            <h2 style="color: #1a237e; margin: 0;">Resume Documents</h2>
            <p class="text-muted" style="margin-top: 0.25rem;">Upload and manage resume files (PDF, DOCX up to 10MB)</p>
        </div>
        <a href="${pageContext.request.contextPath}/candidate/profile" class="btn btn-outline-primary">&larr; Back to Profile</a>
    </div>

    <!-- Upload Resume Card -->
    <div class="card p-4 mb-4">
        <h4 style="color: #1a237e; margin-bottom: 1rem;">Upload New Resume</h4>
        <form action="${pageContext.request.contextPath}/candidate/resume" method="POST" enctype="multipart/form-data" class="d-flex flex-wrap gap-3 align-center">
            <input type="hidden" name="_csrf" value="${csrfToken}"/>

            <div class="form-group" style="flex: 2; min-width: 250px;">
                <label class="form-label" for="resumeFile">Select Document</label>
                <input type="file" id="resumeFile" name="resumeFile" class="form-control" accept=".pdf,.docx" required/>
            </div>

            <button type="submit" class="btn btn-success" style="margin-top: 1.8rem; min-width: 140px;">Upload File</button>
        </form>
    </div>

    <!-- Resumes List -->
    <div class="card p-4">
        <h4 style="color: #1a237e; margin-bottom: 1rem;">Uploaded Files</h4>
        <c:choose>
            <c:when test="${not empty resumes}">
                <div style="overflow-x: auto;">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Filename</th>
                                <th>Size</th>
                                <th>Uploaded On</th>
                                <th>Primary</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="res" items="${resumes}">
                                <tr>
                                    <td><strong>${res.originalFilename}</strong></td>
                                    <td>${res.fileSizeFormatted}</td>
                                    <td>${res.uploadedAt}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${res.primary}">
                                                <span class="badge badge-success">Primary</span>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/candidate/resume" method="POST" style="margin: 0;">
                                                    <input type="hidden" name="_csrf" value="${csrfToken}"/>
                                                    <input type="hidden" name="action" value="setPrimary"/>
                                                    <input type="hidden" name="resumeId" value="${res.resumeId}"/>
                                                    <button type="submit" class="btn btn-sm btn-secondary">Make Primary</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="d-flex gap-2">
                                        <a href="${pageContext.request.contextPath}/candidate/resume?action=download&id=${res.resumeId}" class="btn btn-sm btn-primary">Download</a>
                                        <form action="${pageContext.request.contextPath}/candidate/resume" method="POST" onsubmit="return confirm('Delete this resume?');" style="margin: 0;">
                                            <input type="hidden" name="_csrf" value="${csrfToken}"/>
                                            <input type="hidden" name="action" value="delete"/>
                                            <input type="hidden" name="resumeId" value="${res.resumeId}"/>
                                            <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <p class="text-muted text-center p-3">No resumes uploaded yet. Upload a document to start applying for jobs.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
