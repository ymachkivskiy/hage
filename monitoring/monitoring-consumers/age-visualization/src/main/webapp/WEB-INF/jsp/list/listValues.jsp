<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page trimDirectiveWhitespaces="true" %>

<div id="middle">
    <c:forEach items="${cdColl}" var="cd">
        ${cd.toString()}

        <br/>
    </c:forEach>
</div>