<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.core.model.order.Order" %>

<tr>
    <td style="width: 100px">
        ${label} <span style="color: red">*</span><br>
    </td>
    <td style="width: 200px">
        <form:input name="${name}" placeholder="${label}" value="${param[name]}" path="${name}"/>
        <div class="alert-danger" style="margin-top: 10px; width: 200px">
            <form:errors path="${name}"/>
        </div>
    </td>
</tr>