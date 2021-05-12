<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product List" cart="${cart}">
    <form class="input-group mb-3">
        <input name="query" type="text" class="form-control" placeholder="Search" value="${param.query}"><br>
        <div class="input-group-append">
            <button class="btn btn-dark">Search</button>
        </div>
    </form>

    <table class="table table-striped table-bordered">
        <thead class="table-dark">
        <tr>
            <th>Image</th>
            <th>
                Brand
                <tags:sortLink sort="brand" order="asc"/>
                <tags:sortLink sort="brand" order="desc"/>
            </th>
            <th>
                Model
                <tags:sortLink sort="model" order="asc"/>
                <tags:sortLink sort="model" order="desc"/>
            </th>
            <th>Color</th>
            <th>
                Display size
                <tags:sortLink sort="displaySizeInches" order="asc"/>
                <tags:sortLink sort="displaySizeInches" order="desc"/>
            </th>
            <th>
                Price
                <tags:sortLink sort="price" order="asc"/>
                <tags:sortLink sort="price" order="desc"/>
            </th>
            <th>Quantity</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="phone" items="${phones}">
            <tr>
                <td>
                    <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}"
                         height="150px">
                </td>
                <td>${phone.brand}</td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/productDetails/${phone.id}">${phone.model}</a>
                </td>
                <td>
                    <c:forEach var="color" items="${phone.colors}">
                        ${color.code} <br>
                    </c:forEach>
                </td>
                <td>${phone.displaySizeInches}''</td>
                <td>$ ${phone.price}</td>
                <td style="width: 15%">
                    <input id="input${phone.id}" class="form-control" value="1"><br>
                    <div id="message${phone.id}"></div>
                </td>
                <td>
                    <button id="${phone.id}" class="btn btn-outline-dark">Add to cart</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <ul class="pagination justify-content-end">
        <c:set var="first" value="${param.page > 1 ? (param.page < pagesCount ? param.page - 1 : param.page - 2) : 1}"/>
        <c:set var="second" value="${param.page > 1 ? (param.page < pagesCount ? param.page : param.page - 1) : 2}"/>
        <c:set var="third" value="${param.page > 1 ? (param.page < pagesCount ? param.page + 1 : param.page) : 3}"/>
        <li class="page-item ${param.page == 1 ? 'disabled' : ''}">
            <a class="page-link"
               href="${pageContext.servletContext.contextPath}/productList?query=${param.query}&sort=${param.sort}&order=${param.order}&page=1">
                &laquo;
            </a>
        </li>
        <li class="page-item ${param.page == 1 ? 'disabled' : ''}">
            <a class="page-link"
               href="${pageContext.servletContext.contextPath}/productList?query=${param.query}&sort=${param.sort}&order=${param.order}&page=${param.page - 1}">
                <
            </a>
        </li>
        <li class="page-item ${param.page == first ? 'active' : ''}">
            <a class="page-link"
               href="${pageContext.servletContext.contextPath}/productList?query=${param.query}&sort=${param.sort}&order=${param.order}&page=${first}">
                    ${first}
            </a>
        </li>
        <li class="page-item ${param.page == second ? 'active' : ''}">
            <a class="page-link"
               href="${pageContext.servletContext.contextPath}/productList?query=${param.query}&sort=${param.sort}&order=${param.order}&page=${second}">
                    ${second}
            </a>
        </li>
        <li class="page-item ${param.page == third ? 'active' : ''}">
            <a class="page-link"
               href="${pageContext.servletContext.contextPath}/productList?query=${param.query}&sort=${param.sort}&order=${param.order}&page=${third}">
                    ${third}
            </a>
        </li>
        <li class="page-item ${param.page == pagesCount ? 'disabled' : ''}">
            <a class="page-link"
               href="${pageContext.servletContext.contextPath}/productList?query=${param.query}&sort=${param.sort}&order=${param.order}&page=${param.page + 1}">
                >
            </a>
        </li>
        <li class="page-item ${param.page == pagesCount ? 'disabled' : ''}">
            <a class="page-link"
               href="${pageContext.servletContext.contextPath}/productList?query=${param.query}&sort=${param.sort}&order=${param.order}&page=${pagesCount}">
                &raquo;
            </a>
        </li>
    </ul>
</tags:master>