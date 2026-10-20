<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Login - CareerBridge"/>
</jsp:include>
<jsp:include page="/fragments/messages.jsp"/>

<div class="container d-flex justify-center align-center mt-5 mb-5" style="min-height: 60vh;">
    <div class="card" style="width: 100%; max-width: 440px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
        <div class="card-header text-center" style="background: #f8f9fa; border-bottom: 1px solid #e9ecef; padding: 1.5rem;">
            <h3 style="margin: 0; color: #1a237e;">Sign In to CareerBridge</h3>
            <p class="text-muted" style="margin-top: 0.5rem; font-size: 0.9rem;">Access your dashboard and manage jobs</p>
        </div>
        <div class="card-body p-4">
            <form action="${pageContext.request.contextPath}/login" method="POST">
                <input type="hidden" name="_csrf" value="${csrfToken}"/>

                <div class="form-group mb-3">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="form-control" placeholder="name@domain.com" required autofocus value="${param.email}"/>
                </div>

                <div class="form-group mb-3">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required/>
                </div>

                <button type="submit" class="btn btn-primary w-100" style="padding: 0.65rem; font-weight: 600;">Sign In</button>
            </form>
        </div>
        <div class="card-footer text-center" style="background: #fdfdfd; border-top: 1px solid #eee; padding: 1rem;">
            <span class="text-muted">Don't have an account? </span>
            <a href="${pageContext.request.contextPath}/register" style="color: #1565C0; font-weight: 600; text-decoration: none;">Create one here</a>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
