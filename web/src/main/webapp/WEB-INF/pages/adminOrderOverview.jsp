<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Order overview">
    <div class="row">
        <h3 class="col">Order number: ${order.id}</h3>
        <h3 class="col text-end">Order status: ${order.status}</h3>
    </div>
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
    <h4 id="messageAdmin"></h4>
    <c:if test="${order.status eq 'NEW'}">
        <button id="delivered" class="btn btn-dark btn-lg">Delivered</button>
        <button id="rejected" class="btn btn-dark btn-lg">Rejected</button>
    </c:if><br>
    <a href="${pageContext.servletContext.contextPath}/admin/orders" class="btn btn-dark btn-lg"
       style="margin-top: 20px">
        Back
    </a>

    <script>
        $(document).ready(function () {
            $('#delivered').click(function () {
                $.ajax({
                    type: 'POST',
                    url: '${pageContext.servletContext.contextPath}/ajaxAdminDelivered/${orderId}',
                    success: function (text) {
                        let message = $('#messageAdmin')
                        message.html(text)
                        message.show()
                        window.location.href = '${pageContext.servletContext.contextPath}/admin/orders/${orderId}'
                    }
                })
            })
            $('#rejected').click(function () {
                $.ajax({
                    type: 'POST',
                    url: '${pageContext.servletContext.contextPath}/ajaxAdminRejected/${orderId}',
                    success: function (text) {
                        let message = $('#messageAdmin')
                        message.html(text)
                        message.show()
                        window.location.href = '${pageContext.servletContext.contextPath}/admin/orders/${orderId}'
                    }
                })
            })
        })
    </script>
</tags:master>

