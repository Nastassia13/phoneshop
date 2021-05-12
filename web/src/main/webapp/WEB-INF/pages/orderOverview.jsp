<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Order overview">
    <h2>Thank you for your order</h2>
    <h3>Order number: ${order.id}</h3>
    <table class="table table-striped table-bordered" style="margin-top: 20px">
        <thead class="table-dark">
        <tr>
            <th>Brand</th>
            <th>Model</th>
            <th>Color</th>
            <th>Display size</th>
            <th>Quantity</th>
            <th>Price</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${order.items}" varStatus="status">
            <tr>
                <td>${item.phone.brand}</td>
                <td>${item.phone.model}</td>
                <td>
                    <c:forEach var="color" items="${item.phone.colors}">
                        ${color.code} <br>
                    </c:forEach>
                </td>
                <td>${item.phone.displaySizeInches}''</td>
                <td>${item.quantity}</td>
                <td>${item.phone.price} $</td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="5" style="text-align: right">Subtotal</td>
            <td>${order.subtotal} $</td>
        </tr>
        <tr>
            <td colspan="5" style="text-align: right">Delivery</td>
            <td>${order.deliveryPrice} $</td>
        </tr>
        <tr>
            <td colspan="5" style="text-align: right">TOTAL</td>
            <td>${order.totalPrice} $</td>
        </tr>
        </tbody>
    </table>
    <br>
    <table class="table">
        <tr>
            <td style="width: 100px">First name</td>
            <td>${order.firstName}</td>
        </tr>
        <tr>
            <td style="width: 100px">Last name</td>
            <td>${order.lastName}</td>
        </tr>
        <tr>
            <td style="width: 100px">Address</td>
            <td>${order.deliveryAddress}</td>
        </tr>
        <tr>
            <td style="width: 100px">Phone</td>
            <td>${order.contactPhoneNo}</td>
        </tr>
    </table>
    <div style="width: 500px">
            ${order.additionalInformation}
    </div>
    <br>
    <a href="${pageContext.servletContext.contextPath}/productList" class="btn btn-dark btn-lg"
       style="margin-top: 20px">
        Back to shopping
    </a>
</tags:master>

