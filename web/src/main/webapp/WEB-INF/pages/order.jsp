<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Order">
    <h2>Order</h2>
    <a href="${pageContext.servletContext.contextPath}/cart" class="btn btn-dark btn-lg"
       style="margin-top: 20px">
        Back to cart
    </a>
    <form:form method="post" commandName="orderForm">
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
        <div class="alert-danger">
                ${stockError}
        </div>
        <div class="alert-danger">
                ${emptyOrder}
        </div>
        <table class="table">
            <tags:orderFormRow name="firstName" label="First name" order="${order}"/>
            <tags:orderFormRow name="lastName" label="Last name" order="${order}"/>
            <tags:orderFormRow name="contactPhoneNo" label="Phone" order="${order}"/>
            <tags:orderFormRow name="deliveryAddress" label="Address" order="${order}"/>
        </table>
        <label>
            <form:textarea name="additionalInformation" placeholder="Additional information" style="width: 500px"
                           path="additionalInformation"/>
        </label>
        <br>
        <button class="btn btn-dark" style="margin-top: 30px">Order</button>
    </form:form>
</tags:master>
