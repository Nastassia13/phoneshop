<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product List" cart="${cart}">
    <a href="${pageContext.servletContext.contextPath}/productList" class="btn btn-dark btn-lg">
        Back to product list
    </a>
    <h2 style="margin-top: 20px">Order with code ${id} not found</h2>
</tags:master>