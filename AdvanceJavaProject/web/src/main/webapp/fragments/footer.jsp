<%@ page contentType="text/html;charset=UTF-8" language="java" %>
</div> <%-- End .page-content --%>

<footer class="footer">
    <div class="container">
        <div class="d-flex justify-between flex-wrap gap-4">
            <div style="max-width: 360px;">
                <h5 class="d-flex align-center gap-2">
                    <span class="brand-icon" style="display:inline-flex;align-items:center;justify-content:center;width:24px;height:24px;background:#2196F3;color:#fff;border-radius:4px;font-size:12px;font-weight:700;">CB</span>
                    CareerBridge
                </h5>
                <p class="text-muted" style="color: #94a3b8 !important; font-size: 0.875rem;">
                    Bridging ambitious talent with leading employers. Discover opportunities, submit applications, and advance your professional journey.
                </p>
            </div>
            <div>
                <h5>Quick Links</h5>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/jobs">Browse Jobs</a></li>
                    <li><a href="${pageContext.request.contextPath}/register">Create Account</a></li>
                    <li><a href="${pageContext.request.contextPath}/login">Sign In</a></li>
                </ul>
            </div>
            <div>
                <h5>For Employers</h5>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/register">Post a Job</a></li>
                    <li><a href="${pageContext.request.contextPath}/employer/dashboard">Employer Dashboard</a></li>
                </ul>
            </div>
            <div>
                <h5>Support & Legal</h5>
                <ul class="footer-links">
                    <li><a href="#">Privacy Policy</a></li>
                    <li><a href="#">Terms of Service</a></li>
                    <li><a href="#">Contact Us</a></li>
                </ul>
            </div>
        </div>

        <div class="footer-bottom">
            <span>&copy; <%= java.time.Year.now().getValue() %> CareerBridge Inc. All rights reserved.</span>
            <span>Empowering Careers Everywhere</span>
        </div>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
