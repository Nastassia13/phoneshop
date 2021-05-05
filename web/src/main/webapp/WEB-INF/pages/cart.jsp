<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Cart">
    <table class="table table-striped table-bordered">
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
        <c:forEach var="item" items="${cart.items}">
            <tr>
                <td>${item.phoneId.brand}</td>
                <td>${item.phoneId.model}</td>
                <td>
                    <c:forEach var="color" items="${item.phoneId.colors}">
                        ${color.code} <br>
                    </c:forEach>
                </td>
                <td>${item.phoneId.displaySizeInches}''</td>
                <td>$ ${item.phoneId.price}</td>
                <td style="width: 10%">
                    <input name="query" class="form-control" value="${item.quantity}}">
                </td>
                <td>
                    <button class="btn btn-outline-dark">Delete</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</tags:master>
