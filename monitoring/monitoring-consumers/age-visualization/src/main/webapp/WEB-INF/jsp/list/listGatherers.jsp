<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page trimDirectiveWhitespaces="true" %>


<div id="middle">

<fieldset>
<legend>Gatherers:</legend>
<a href="${pageContext.request.contextPath}/age/show/${computationType}/${computationInstance}">show all</a>

<c:set var="count" value="1" scope="page" />
	<form:form action="/age/showChecked?computationType=${computationType}&computationInstance=${computationInstance}&gathererId=" method="POST" commandName="checkedStatistics" target="_blank">

		<c:forEach items="${cdColl}" var="cd">
			<form:checkbox path="checkboxNumber" value="${count}"/>
			<span>${cd.toString()}</span>
			<a href="${pageContext.request.contextPath}/age/list/${computationType}/${computationInstance}/${cd.toString()}">display data</a>
			<a href="${pageContext.request.contextPath}/age/show/${computationType}/${computationInstance}/${cd.toString()}">show single</a>
			
		<br />
		<c:set var="count" value="${count + 1}" scope="page"/>
	</c:forEach>
	<input type="submit" value="Show checked" class="myButton"/>
	</form:form>
</fieldset>
</div>

