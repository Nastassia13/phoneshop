<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ attribute name="cart" type="com.es.core.model.cart.Cart" %>

<html>
<head>
    <title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Phone List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
</head>

<body style="margin: 30px">
<header>
    <sec:authorize access="!isAuthenticated()">
        <a class="float-end" href="<c:url value="/login"/>">Login</a>
    </sec:authorize>
    <sec:authorize access="isAuthenticated()">
        <div class="row justify-content-end">
            <div class="col-1">
                <div class="float-end">
                    Hello, <c:out value="${pageContext.request.remoteUser}"/>!
                </div>
            </div>
            <div class="col-1">
                <a class="float-end" href="${pageContext.servletContext.contextPath}/admin/orders">Admin</a>
            </div>
            <div class="col-1">
                <form method="post" action="${pageContext.request.contextPath}/logout">
                    <a class="float-end" href="<c:url value="/logout"/>">Logout</a>
                </form>
            </div>
        </div>
    </sec:authorize>
    <br>
    <div class="display-1">
        <a style="color: black" class="text-decoration-none"
           href="${pageContext.servletContext.contextPath}/productList">Phonify</a>
        <c:if test="${not empty cart}">
            <a id="cart" href="${pageContext.servletContext.contextPath}/cart" class="btn btn-dark btn-lg float-end"
               style="margin-top: 25px">
            </a>
        </c:if>
        <a href="${pageContext.servletContext.contextPath}/quickOrder" class="btn btn-dark btn-lg float-end"
           style="margin-top: 25px; margin-right: 20px">
            Quick order
        </a>
    </div>
    <hr style="height: 2px">
</header>
<main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf"
            crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script>
        $(document).ready(function () {
            $('#cart').html('My cart: ${cart.totalQuantity} items ${cart.totalCost} $')
            $('button').click(function () {
                let id = $(this).attr('id')
                let input = $('#input' + id).prop('value')
                $.ajax({
                    type: 'POST',
                    url: '${pageContext.servletContext.contextPath}/ajaxCart',
                    data: {
                        phoneId: id,
                        quantity: input
                    },
                    success: function (dto) {
                        console.log(dto.totalQuantity)
                        $('[id^=message]').hide()
                        let message = $('#message' + id)
                        if (dto.success === true) {
                            message.attr('class', 'alert alert-success')
                            $('#cart').html('My cart: ' + dto.totalQuantity.toString() + ' items ' + dto.totalCost.toString() + '$')
                        } else {
                            message.prop('class', 'alert alert-danger')
                        }
                        message.html(dto.message)
                        message.show()
                    }
                })
            })
        })
    </script>
    <jsp:doBody/>
</main>
<footer>
    <p style="margin-top: 30px">(c) Expert-Soft</p>
</footer>
</body>
</html>