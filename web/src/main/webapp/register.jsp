<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Create Account - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container d-flex justify-center align-center mt-4 mb-5">
    <div class="card" style="width: 100%; max-width: 500px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
        <div class="card-header text-center" style="background: #f8f9fa; border-bottom: 1px solid #e9ecef; padding: 1.5rem;">
            <h3 style="margin: 0; color: #1a237e;">Join CareerBridge</h3>
            <p class="text-muted" style="margin-top: 0.5rem; font-size: 0.9rem;">Register as a Job Seeker or Recruiter</p>
        </div>
        <div class="card-body p-4">
            <form action="${pageContext.request.contextPath}/register" method="POST">
                <input type="hidden" name="_csrf" value="${csrfToken}"/>

                <div class="form-group mb-3">
                    <label class="form-label">I want to register as:</label>
                    <div class="d-flex gap-3">
                        <label style="cursor: pointer; display: flex; align-items: center; gap: 0.5rem;">
                            <input type="radio" name="role" value="JOB_SEEKER" ${selectedRole != 'EMPLOYER' ? 'checked' : ''}/>
                            <strong>Candidate / Student</strong>
                        </label>
                        <label style="cursor: pointer; display: flex; align-items: center; gap: 0.5rem;">
                            <input type="radio" name="role" value="EMPLOYER" ${selectedRole == 'EMPLOYER' ? 'checked' : ''}/>
                            <strong>Employer / Recruiter</strong>
                        </label>
                    </div>
                </div>

                <div class="form-group mb-3">
                    <label class="form-label" for="fullName">Full Name</label>
                    <input type="text" id="fullName" name="fullName" class="form-control" placeholder="Aarav Sharma" required value="${fullName}"/>
                </div>

                <div class="form-group mb-3">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="form-control" placeholder="name@domain.com" required value="${email}"/>
                </div>

                <div class="form-group mb-3">
                    <label class="form-label" for="password">Password (min 8 chars)</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required/>
                </div>

                <div class="form-group mb-3">
                    <label class="form-label" for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="••••••••" required/>
                </div>

                <button type="submit" class="btn btn-primary w-100" style="padding: 0.65rem; font-weight: 600;">Create Account</button>
            </form>
        </div>
        <div class="card-footer text-center" style="background: #fdfdfd; border-top: 1px solid #eee; padding: 1rem;">
            <span class="text-muted">Already registered? </span>
            <a href="${pageContext.request.contextPath}/login" style="color: #1565C0; font-weight: 600; text-decoration: none;">Log in</a>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
