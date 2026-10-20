<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Manage Education - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <div>
            <h2 style="color: #1a237e; margin: 0;">Education History</h2>
            <p class="text-muted" style="margin-top: 0.25rem;">Add your academic degrees, certifications, and grades</p>
        </div>
        <a href="${pageContext.request.contextPath}/candidate/profile" class="btn btn-outline-primary">&larr; Back to Profile</a>
    </div>

    <!-- Education List -->
    <div class="d-flex flex-column gap-3 mb-4">
        <c:choose>
            <c:when test="${not empty educationList}">
                <c:forEach var="edu" items="${educationList}">
                    <div class="card p-3 d-flex justify-between align-center" style="flex-direction: row;">
                        <div>
                            <h4 style="margin: 0; color: #1a237e;">${edu.qualification} in ${edu.fieldOfStudy}</h4>
                            <div class="text-muted" style="font-size: 0.95rem; margin: 0.25rem 0;">${edu.institution}</div>
                            <div style="font-size: 0.85rem; color: #555;">
                                ${edu.startDate} &mdash; <c:choose><c:when test="${edu.endDate != null}">${edu.endDate}</c:when><c:otherwise>Present</c:otherwise></c:choose>
                                <c:if test="${not empty edu.grade}"> &bull; <strong>Score:</strong> ${edu.grade}</c:if>
                            </div>
                        </div>
                        <form action="${pageContext.request.contextPath}/candidate/education" method="POST" onsubmit="return confirm('Delete this education entry?');">
                            <input type="hidden" name="_csrf" value="${csrfToken}"/>
                            <input type="hidden" name="action" value="delete"/>
                            <input type="hidden" name="educationId" value="${edu.educationId}"/>
                            <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                        </form>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="card p-4 text-center text-muted">No education records added yet.</div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Add Education Form -->
    <div class="card p-4">
        <h4 style="color: #1a237e; margin-bottom: 1.5rem;">Add Qualification</h4>
        <form action="${pageContext.request.contextPath}/candidate/education" method="POST">
            <input type="hidden" name="_csrf" value="${csrfToken}"/>
            <input type="hidden" name="action" value="add"/>

            <div class="d-flex flex-wrap gap-3 mb-3">
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="institution">Institution / University</label>
                    <input type="text" id="institution" name="institution" class="form-control" placeholder="e.g. Rajasthan Technical University" required/>
                </div>
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="qualification">Degree / Qualification</label>
                    <input type="text" id="qualification" name="qualification" class="form-control" placeholder="e.g. B.Tech / MCA / Diploma" required/>
                </div>
            </div>

            <div class="d-flex flex-wrap gap-3 mb-3">
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="fieldOfStudy">Field of Study</label>
                    <input type="text" id="fieldOfStudy" name="fieldOfStudy" class="form-control" placeholder="e.g. Computer Science & Engineering"/>
                </div>
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="grade">Grade / CGPA</label>
                    <input type="text" id="grade" name="grade" class="form-control" placeholder="e.g. 8.5 CGPA or 82%"/>
                </div>
            </div>

            <div class="d-flex flex-wrap gap-3 mb-4">
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="startDate">Start Date</label>
                    <input type="date" id="startDate" name="startDate" class="form-control"/>
                </div>
                <div class="form-group" style="flex: 1; min-width: 200px;">
                    <label class="form-label" for="endDate">End Date (leave blank if ongoing)</label>
                    <input type="date" id="endDate" name="endDate" class="form-control"/>
                </div>
            </div>

            <button type="submit" class="btn btn-primary" style="min-width: 150px;">Add Education</button>
        </form>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
