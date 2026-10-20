<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Manage Skills - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <div>
            <h2 style="color: #1a237e; margin: 0;">Skill Endorsements</h2>
            <p class="text-muted" style="margin-top: 0.25rem;">Highlight your technical and professional proficiencies</p>
        </div>
        <a href="${pageContext.request.contextPath}/candidate/profile" class="btn btn-outline-primary">&larr; Back to Profile</a>
    </div>

    <!-- Current Skills Display -->
    <div class="card p-4 mb-4">
        <h4 style="color: #1a237e; margin-bottom: 1rem;">My Skills</h4>
        <c:choose>
            <c:when test="${not empty candidateSkills}">
                <div class="d-flex flex-wrap gap-2">
                    <c:forEach var="cs" items="${candidateSkills}">
                        <div class="d-flex align-center gap-2 p-2" style="background: #e3f2fd; border: 1px solid #90caf9; border-radius: 20px; font-size: 0.9rem;">
                            <strong>${cs.skillName}</strong>
                            <span class="badge badge-primary" style="font-size: 0.75rem;">${cs.proficiencyLevel}</span>
                            <form action="${pageContext.request.contextPath}/candidate/skills" method="POST" style="margin: 0; display: inline;">
                                <input type="hidden" name="_csrf" value="${csrfToken}"/>
                                <input type="hidden" name="action" value="remove"/>
                                <input type="hidden" name="skillId" value="${cs.skillId}"/>
                                <button type="submit" style="background: none; border: none; cursor: pointer; color: #c62828; font-weight: bold; padding: 0 4px;" title="Remove skill">&times;</button>
                            </form>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p class="text-muted">You haven't listed any skills yet. Add your proficiencies below.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Add Skill Form -->
    <div class="card p-4">
        <h4 style="color: #1a237e; margin-bottom: 1rem;">Add a Skill</h4>
        <form action="${pageContext.request.contextPath}/candidate/skills" method="POST" class="d-flex flex-wrap gap-3 align-center">
            <input type="hidden" name="_csrf" value="${csrfToken}"/>
            <input type="hidden" name="action" value="add"/>

            <div class="form-group" style="flex: 2; min-width: 200px;">
                <label class="form-label" for="skillId">Select Skill</label>
                <select id="skillId" name="skillId" class="form-control" required>
                    <c:forEach var="s" items="${allSkills}">
                        <option value="${s.skillId}">${s.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group" style="flex: 1; min-width: 180px;">
                <label class="form-label" for="proficiencyLevel">Proficiency Level</label>
                <select id="proficiencyLevel" name="proficiencyLevel" class="form-control" required>
                    <option value="BEGINNER">Beginner</option>
                    <option value="INTERMEDIATE" selected>Intermediate</option>
                    <option value="ADVANCED">Advanced</option>
                    <option value="EXPERT">Expert</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary" style="margin-top: 1.8rem; min-width: 120px;">Add Skill</button>
        </form>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
