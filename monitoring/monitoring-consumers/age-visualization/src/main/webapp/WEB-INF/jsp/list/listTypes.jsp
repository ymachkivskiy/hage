<%@ page language="Java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



<div id="middle">
<fieldset>
<legend>Types:</legend>
<c:forEach items="${cdColl}" var="cd">
	<a href="${uri}${slash}${cd.toString()}/">${cd.toString()}</a>
	<br />
</c:forEach>
</fieldset>
</div>

	
