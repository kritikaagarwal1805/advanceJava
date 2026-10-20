<%@ tag description="Renders pagination" pageEncoding="UTF-8" %>
<%@ attribute name="currentPage" required="true" type="java.lang.Integer" %>
<%@ attribute name="totalPages" required="true" type="java.lang.Integer" %>
<%@ attribute name="baseUrl" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty totalPages and totalPages > 1}">
    <c:set var="separator" value="${baseUrl.contains('?') ? '&' : '?'}" />
    <c:set var="curr" value="${not empty currentPage and currentPage > 0 ? currentPage : 1}" />

    <%-- Calculate bounds --%>
    <c:choose>
        <c:when test="${totalPages <= 7}">
            <c:set var="startPage" value="1" />
            <c:set var="endPage" value="${totalPages}" />
        </c:when>
        <c:otherwise>
            <c:set var="startPage" value="${curr - 2 > 1 ? curr - 2 : 1}" />
            <c:set var="endPage" value="${curr + 2 < totalPages ? curr + 2 : totalPages}" />
            <%-- Adjust if near boundaries --%>
            <c:if test="${curr <= 3}">
                <c:set var="startPage" value="1" />
                <c:set var="endPage" value="5" />
            </c:if>
            <c:if test="${curr >= totalPages - 2}">
                <c:set var="startPage" value="${totalPages - 4}" />
                <c:set var="endPage" value="${totalPages}" />
            </c:if>
        </c:otherwise>
    </c:choose>

    <nav aria-label="Page navigation" class="pagination-nav">
        <ul class="pagination">
            <%-- Previous Link --%>
            <c:choose>
                <c:when test="${curr > 1}">
                    <li class="page-item">
                        <a class="page-link" href="${baseUrl}${separator}page=${curr - 1}" aria-label="Previous">&laquo; Prev</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item disabled">
                        <span class="page-link" aria-hidden="true">&laquo; Prev</span>
                    </li>
                </c:otherwise>
            </c:choose>

            <%-- First Page + Ellipsis if needed --%>
            <c:if test="${startPage > 1}">
                <li class="page-item">
                    <a class="page-link" href="${baseUrl}${separator}page=1">1</a>
                </li>
                <c:if test="${startPage > 2}">
                    <li class="page-item disabled"><span class="page-link">&hellip;</span></li>
                </c:if>
            </c:if>

            <%-- Numeric Page Links --%>
            <c:forEach var="p" begin="${startPage}" end="${endPage}">
                <c:choose>
                    <c:when test="${p == curr}">
                        <li class="page-item active" aria-current="page">
                            <span class="page-link">${p}</span>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link" href="${baseUrl}${separator}page=${p}">${p}</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <%-- Ellipsis + Last Page if needed --%>
            <c:if test="${endPage < totalPages}">
                <c:if test="${endPage < totalPages - 1}">
                    <li class="page-item disabled"><span class="page-link">&hellip;</span></li>
                </c:if>
                <li class="page-item">
                    <a class="page-link" href="${baseUrl}${separator}page=${totalPages}">${totalPages}</a>
                </li>
            </c:if>

            <%-- Next Link --%>
            <c:choose>
                <c:when test="${curr < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="${baseUrl}${separator}page=${curr + 1}" aria-label="Next">Next &raquo;</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item disabled">
                        <span class="page-link" aria-hidden="true">Next &raquo;</span>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</c:if>
