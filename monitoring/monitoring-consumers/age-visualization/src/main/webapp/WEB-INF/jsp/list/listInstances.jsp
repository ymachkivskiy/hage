<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<jsp:useBean id="newsDate" class="java.util.Date" />


<div id="middle">
<fieldset>
<legend>Instances:</legend>
<a href="${uri}${slash}last/">last</a>
<br/>
<c:forEach items="${cdColl}" var="cd">
	<a href="${uri}${slash}${cd.toString()}/">${cd.toString()}</a> 
	<jsp:setProperty name="newsDate" property="time" value="${cd.toString()}" />
	Date: <fmt:formatDate value="${newsDate}" type="both" dateStyle="short" timeStyle="medium" />
	<br />
</c:forEach>
</fieldset>
</div>
	
