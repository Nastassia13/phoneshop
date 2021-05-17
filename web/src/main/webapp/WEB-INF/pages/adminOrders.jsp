<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tags:master pageTitle="Order">
    <h2>Orders</h2>
    <table class="table table-striped table-bordered" style="margin-top: 20px">
        <thead class="table-dark">
        <tr>
            <th>Order number</th>
            <th>Customer</th>
            <th>Phone</th>
            <th>Address</th>
            <th>Date</th>
            <th>Total price</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/admin/orders/${order.id}"> ${order.id}</a>
                </td>
                <td>${order.firstName} ${order.lastName}</td>
                <td>${order.contactPhoneNo}</td>
                <td>${order.deliveryAddress}</td>
                <td>
                    <fmt:parseDate value="${order.orderDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                    <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" />
                </td>
                <td>${order.totalPrice} $</td>
                <td>${order.status}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</tags:master>
