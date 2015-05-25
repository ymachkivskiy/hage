<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page trimDirectiveWhitespaces="true" %>


<jsp:useBean id="newsDate" class="java.util.Date" />

<div id="middle">
<fieldset>
<legend>Instances:</legend>
<a href="${pageContext.request.contextPath}/age/show/${computationType}/last/${gathererId}">show last</a>
<a href="${pageContext.request.contextPath}/age/list/${computationType}/last/${gathererId}">dispaly last data</a>

<c:set var="count" value="${fn:length(cdColl)}" scope="page" />
	<form:form action="/age/showChecked?computationType=${computationType}&computationInstance=*&gathererId=${gathererId}" method="POST" commandName="checkedStatistics">
		<c:forEach items="${cdColl}" var="cd">
			<form:checkbox path="checkboxNumber" value="${count}"/>
			${cd.toString()}
			<a href="${pageContext.request.contextPath}/age/list/${computationType}/${cd.toString()}/${gathererId}">display data</a>
			<a href="${pageContext.request.contextPath}/age/show/${computationType}/${cd.toString()}/${gathererId}">show single</a>
			<jsp:setProperty name="newsDate" property="time" value="${cd.toString()}" />
		Date: <fmt:formatDate value="${newsDate}" type="both" dateStyle="short" timeStyle="medium" />
		<br />
		<c:set var="count" value="${count - 1}" scope="page"/>
	</c:forEach>
	<input type="submit" value="Show checked" class="myButton"/>
	</form:form>
	</fieldset>
	</div>
