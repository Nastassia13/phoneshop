<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Quick order" cart="${cart}">
    <c:forEach var="item" items="${success}">
        <div class="alert-success" style="margin-top: 10px">
                ${item} product added to cart successfully
        </div>
    </c:forEach>
    <form:form method="put" commandName="quickForm">
        <table class="table" style="margin-top: 20px">
            <thead class="table-dark">
            <tr>
                <th>Product code</th>
                <th>Quantity</th>
            </tr>
            </thead>
            <c:forEach begin="0" end="7" varStatus="status">
                <tbody>
                <tr>
                    <td>
                        <form:input name="phoneModels" class="form-control"
                                    value="${quickForm.phoneModels[status.index]}"
                                    path="phoneModels[${status.index}]"/>
                        <div class="alert-danger" style="margin-top: 10px">
                            <form:errors path="phoneModels[${status.index}]"/>
                        </div>
                    </td>
                    <td>
                        <form:input name="quantities" class="form-control" value="${quickForm.quantities[status.index]}"
                                    path="quantities[${status.index}]"/>
                        <div class="alert-danger" style="margin-top: 10px">
                            <form:errors path="quantities[${status.index}]"/>
                        </div>
                    </td>
                </tr>
                </tbody>
            </c:forEach>
        </table>
        <button class="btn btn-dark">Add to cart</button>
    </form:form>
</tags:master>