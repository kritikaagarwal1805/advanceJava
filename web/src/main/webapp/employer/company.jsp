<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/fragments/header.jsp">
    <jsp:param name="pageTitle" value="${not empty company && not empty company.companyId ? 'Edit Company Profile' : 'Create Company Profile'} - CareerBridge" />
</jsp:include>
<jsp:include page="/fragments/messages.jsp" />

<div class="container page-content">
    <div class="row justify-center">
        <div style="max-width: 800px; margin: 0 auto; width: 100%;">
            <div class="d-flex justify-between align-center mb-4">
                <div>
                    <h1 class="h2 mb-1">
                        <c:choose>
                            <c:when test="${not empty company && not empty company.companyId}">
                                Edit Company Profile
                            </c:when>
                            <c:otherwise>
                                Register Company Profile
                            </c:otherwise>
                        </c:choose>
                    </h1>
                    <p class="text-muted">
                        Manage your company's branding, location, industry, and description visible to prospective job seekers.
                    </p>
                </div>
                <c:if test="${not empty company && not empty company.companyId}">
                    <a href="${pageContext.request.contextPath}/employer/dashboard" class="btn btn-outline-secondary">
                        &larr; Back to Dashboard
                    </a>
                </c:if>
            </div>

            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <form action="${pageContext.request.contextPath}/employer/company" method="post" id="companyForm">
                        <%-- CSRF Token --%>
                        <input type="hidden" name="_csrf" value="${csrfToken != null ? csrfToken : sessionScope.csrfToken}" />

                        <%-- Hidden ID for Update --%>
                        <c:if test="${not empty company && not empty company.companyId}">
                            <input type="hidden" name="companyId" value="${company.companyId}" />
                        </c:if>

                        <c:if test="${not empty company.status}">
                            <div class="d-flex align-center gap-2 mb-4 p-3 rounded" style="background-color: var(--color-gray-100);">
                                <span class="font-size-sm font-weight-600">Current Profile Status:</span>
                                <span class="badge ${company.status == 'ACTIVE' ? 'badge-success' : (company.status == 'PENDING' ? 'badge-warning' : 'badge-secondary')}">
                                    ${company.status}
                                </span>
                            </div>
                        </c:if>

                        <!-- Company Name -->
                        <div class="form-group mb-4">
                            <label for="companyName" class="form-label">Company Name <span class="required">*</span></label>
                            <input type="text"
                                   id="companyName"
                                   name="name"
                                   class="form-control"
                                   value="<c:out value='${company.name}' />"
                                   placeholder="e.g. Acme Technologies Inc."
                                   required
                                   maxlength="200" />
                            <span class="form-hint">The registered name of your business as shown to candidates.</span>
                        </div>

                        <div class="d-flex gap-4 flex-wrap mb-4">
                            <!-- Industry -->
                            <div class="form-group flex-1" style="flex: 1; min-width: 250px;">
                                <label for="industry" class="form-label">Industry <span class="required">*</span></label>
                                <select id="industry" name="industry" class="form-control" required>
                                    <option value="" disabled ${empty company.industry ? 'selected' : ''}>-- Select Industry --</option>
                                    <option value="Information Technology" ${company.industry == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                                    <option value="Software & Cloud Services" ${company.industry == 'Software & Cloud Services' ? 'selected' : ''}>Software & Cloud Services</option>
                                    <option value="Data & Artificial Intelligence" ${company.industry == 'Data & Artificial Intelligence' ? 'selected' : ''}>Data & Artificial Intelligence</option>
                                    <option value="Financial Services & Banking" ${company.industry == 'Financial Services & Banking' ? 'selected' : ''}>Financial Services & Banking</option>
                                    <option value="Healthcare & Pharmaceuticals" ${company.industry == 'Healthcare & Pharmaceuticals' ? 'selected' : ''}>Healthcare & Pharmaceuticals</option>
                                    <option value="E-Commerce & Retail" ${company.industry == 'E-Commerce & Retail' ? 'selected' : ''}>E-Commerce & Retail</option>
                                    <option value="Education & EdTech" ${company.industry == 'Education & EdTech' ? 'selected' : ''}>Education & EdTech</option>
                                    <option value="Manufacturing & Hardware" ${company.industry == 'Manufacturing & Hardware' ? 'selected' : ''}>Manufacturing & Hardware</option>
                                    <option value="Media & Entertainment" ${company.industry == 'Media & Entertainment' ? 'selected' : ''}>Media & Entertainment</option>
                                    <option value="Telecommunications" ${company.industry == 'Telecommunications' ? 'selected' : ''}>Telecommunications</option>
                                    <option value="Consulting & Professional Services" ${company.industry == 'Consulting & Professional Services' ? 'selected' : ''}>Consulting & Professional Services</option>
                                    <option value="Other" ${company.industry == 'Other' ? 'selected' : ''}>Other</option>
                                </select>
                            </div>

                            <!-- Location -->
                            <div class="form-group flex-1" style="flex: 1; min-width: 250px;">
                                <label for="location" class="form-label">Headquarters / Location <span class="required">*</span></label>
                                <input type="text"
                                       id="location"
                                       name="location"
                                       class="form-control"
                                       value="<c:out value='${company.location}' />"
                                       placeholder="e.g. Bengaluru, Karnataka, India"
                                       required
                                       maxlength="200" />
                            </div>
                        </div>

                        <!-- Website URL -->
                        <div class="form-group mb-4">
                            <label for="website" class="form-label">Website URL</label>
                            <input type="url"
                                   id="website"
                                   name="website"
                                   class="form-control"
                                   value="<c:out value='${company.website}' />"
                                   placeholder="https://www.yourcompany.com"
                                   maxlength="500" />
                            <span class="form-hint">Official website link including https://</span>
                        </div>

                        <!-- Logo URL -->
                        <div class="form-group mb-4">
                            <label for="logoUrl" class="form-label">Logo Image URL</label>
                            <input type="url"
                                   id="logoUrl"
                                   name="logoUrl"
                                   class="form-control"
                                   value="<c:out value='${company.logoUrl}' />"
                                   placeholder="https://example.com/logo.png"
                                   maxlength="500" />
                            <span class="form-hint">Direct public link to your company brand logo image (PNG/JPEG/SVG).</span>
                            <c:if test="${not empty company.logoUrl}">
                                <div class="mt-2 d-flex align-center gap-2">
                                    <span class="font-size-xs text-muted">Preview:</span>
                                    <img src="${company.logoUrl}" alt="Company Logo Preview" style="max-height: 48px; max-width: 120px; object-fit: contain; border: 1px solid var(--color-gray-200); border-radius: 4px; padding: 2px;" />
                                </div>
                            </c:if>
                        </div>

                        <!-- Description -->
                        <div class="form-group mb-5">
                            <label for="description" class="form-label">About the Company <span class="required">*</span></label>
                            <textarea id="description"
                                      name="description"
                                      class="form-control"
                                      rows="6"
                                      placeholder="Provide an overview of your organization, mission, culture, and what makes working here exciting..."
                                      required><c:out value="${company.description}" /></textarea>
                            <span class="form-hint">A comprehensive company summary helps attract qualified candidates.</span>
                        </div>

                        <!-- Actions -->
                        <div class="d-flex justify-between align-center pt-3 border-top">
                            <a href="${pageContext.request.contextPath}/employer/dashboard" class="btn btn-outline-secondary">
                                Cancel
                            </a>
                            <button type="submit" class="btn btn-primary btn-lg">
                                <c:choose>
                                    <c:when test="${not empty company && not empty company.companyId}">
                                        💾 Save Changes
                                    </c:when>
                                    <c:otherwise>
                                        🚀 Create Company Profile
                                    </c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp" />
