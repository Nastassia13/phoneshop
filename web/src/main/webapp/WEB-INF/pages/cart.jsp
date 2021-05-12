<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Cart" cart="${cart}">
    <h2>Cart</h2>
    <a href="${pageContext.servletContext.contextPath}/productList" class="btn btn-dark btn-lg"
       style="margin-top: 20px">
        Back to product list
    </a>
    <form:form method="put" commandName="cartForm">
        <table class="table table-striped table-bordered" style="margin-top: 20px">
            <thead class="table-dark">
            <tr>
                <th>Brand</th>
                <th>Model</th>
                <th>Color</th>
                <th>Display size</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                    <td>${item.phone.brand}</td>
                    <td>${item.phone.model}</td>
                    <td>
                        <c:forEach var="color" items="${item.phone.colors}">
                            ${color.code} <br>
                        </c:forEach>
                    </td>
                    <td>${item.phone.displaySizeInches}''</td>
                    <td>${item.phone.price} $</td>
                    <td style="width: 15%">
                        <form:input name="quantities" class="form-control" value="${cartForm.quantities[status.index]}"
                                    path="quantities[${status.index}]"/>
                        <input name="phoneIds" type="hidden" value="${item.phone.id}"/>
                        <div class="alert-danger" style="margin-top: 10px">
                            <form:errors path="quantities[${status.index}]"/>
                        </div>
                    </td>
                    <td>
                        <button name="phoneId" value="${item.phone.id}" class="btn btn-outline-dark"
                                form="deleteCartItem">Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${not empty cart.items}">
            <div class="row">
                <div class="col-11">
                    <button class="btn btn-dark float-end">Update</button>
                </div>
                <div class="col">
                    <a href="${pageContext.servletContext.contextPath}/order" class="btn btn-dark">
                        Order
                    </a>
                </div>
            </div>
        </c:if>
    </form:form>
    <form:form id="updateCart" method="post"/>
    <form:form id="deleteCartItem" method="delete"/>
</tags:master>
