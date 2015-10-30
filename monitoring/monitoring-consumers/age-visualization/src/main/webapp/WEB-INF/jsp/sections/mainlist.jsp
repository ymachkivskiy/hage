<%@ page language="Java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/style.css"/>

</head>

<body>
<c:set var="slash" scope="request" value="/"/>
<c:set var="uri" scope="request" value="${requestScope['javax.servlet.forward.request_uri']}"/>
<c:if test="${fn:substring(uri, fn:length(uri)-1, fn:length(uri)) == '/'}">
    <c:set var="slash" value="" scope="request"/>
</c:if>
<div id="topId">
    <div id="headerId">
        <tiles:insertAttribute name="header"/>
    </div>

    <div id="bodyId">
        <tiles:insertAttribute name="body"/>
    </div>

</div>

</body>
</html>