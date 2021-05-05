<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>

<a href="?query=${param.query}&sort=${sort}&order=${order}"
   style="${sort eq param.sort and order eq param.order ? 'font-size: 125%; color: white;' : 'color: grey;'}">
    ${order eq 'asc' ? '&#8679' : '&#8681'}
</a>
