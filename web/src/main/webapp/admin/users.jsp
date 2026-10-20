<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Manage Users - Administrator"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container mt-4 mb-5">
    <div class="d-flex justify-between align-center mb-4">
        <div>
            <h2 style="color: #1a237e; margin: 0;">User Management</h2>
            <p class="text-muted" style="margin-top: 0.25rem;">Activate, deactivate, or suspend platform accounts</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-primary">&larr; Back to Dashboard</a>
    </div>

    <!-- Filters -->
    <div class="card p-3 mb-4" style="background: #f8f9fa;">
        <form action="${pageContext.request.contextPath}/admin/users" method="GET" class="d-flex flex-wrap gap-2 align-center">
            <select name="role" class="form-control" style="flex: 1; min-width: 150px;">
                <option value="">All Roles</option>
                <option value="JOB_SEEKER" ${roleFilter == 'JOB_SEEKER' ? 'selected' : ''}>Job Seeker</option>
                <option value="EMPLOYER" ${roleFilter == 'EMPLOYER' ? 'selected' : ''}>Employer</option>
                <option value="ADMIN" ${roleFilter == 'ADMIN' ? 'selected' : ''}>Administrator</option>
            </select>
            <select name="status" class="form-control" style="flex: 1; min-width: 150px;">
                <option value="">All Statuses</option>
                <option value="ACTIVE" ${statusFilter == 'ACTIVE' ? 'selected' : ''}>Active</option>
                <option value="INACTIVE" ${statusFilter == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                <option value="SUSPENDED" ${statusFilter == 'SUSPENDED' ? 'selected' : ''}>Suspended</option>
            </select>
            <button type="submit" class="btn btn-primary">Filter</button>
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">Reset</a>
        </form>
    </div>

    <!-- Users Table -->
    <div class="card p-4">
        <div style="overflow-x: auto;">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${users}">
                        <tr>
                            <td>#${u.userId}</td>
                            <td><strong>${u.fullName}</strong></td>
                            <td>${u.email}</td>
                            <td><span class="badge badge-primary">${u.role}</span></td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.status == 'ACTIVE'}"><span class="badge badge-success">ACTIVE</span></c:when>
                                    <c:when test="${u.status == 'INACTIVE'}"><span class="badge badge-secondary">INACTIVE</span></c:when>
                                    <c:otherwise><span class="badge badge-danger">SUSPENDED</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${u.role != 'ADMIN'}">
                                    <form action="${pageContext.request.contextPath}/admin/users" method="POST" style="display: inline-block;">
                                        <input type="hidden" name="_csrf" value="${csrfToken}"/>
                                        <input type="hidden" name="userId" value="${u.userId}"/>
                                        <c:choose>
                                            <c:when test="${u.status == 'ACTIVE'}">
                                                <input type="hidden" name="newStatus" value="SUSPENDED"/>
                                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Suspend user account?');">Suspend</button>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="hidden" name="newStatus" value="ACTIVE"/>
                                                <button type="submit" class="btn btn-sm btn-success">Activate</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <c:if test="${totalPages > 1}">
            <div class="d-flex justify-center mt-3">
                <my:pagination currentPage="${currentPage}" totalPages="${totalPages}" baseUrl="${pageContext.request.contextPath}/admin/users?role=${roleFilter}&status=${statusFilter}"/>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
