
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="resources/css/home.css" rel="stylesheet">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.8/angular.js"></script>
<title>HOME</title>
</head>
<body>

	<div align="right">
		<a href="?lang=en"> en </a> | <a href="?lang=ru"> ru </a>
	</div>
	${answerText}
	<spring:message code="homePage.label.placeholder.email" var="email" />
	<spring:message code="homePage.label.placeholder.password"
		var="password" />

	<form name="login" action="<c:url value='j_spring_security_check' />"
		method="post" class="box login">
		<fieldset class="boxBody">
			<label><spring:message code="homePage.label.form.login" /></label> <input
				type="text" tabindex="1" name="loginEmail" placeholder="${email}"
				required> <input type="text" tabindex="2" name="loginPass"
				placeholder="${password}" required> <input type="hidden"
				name="${_csrf.parameterName}" value="${_csrf.token}" /> <input
				type="submit" class="btnLogin" tabindex="10" value="Login">
		</fieldset>
	</form>

	<form name="Registration" action="registration" method="post"
		class="box Registration">
		<fieldset class="boxBody">
			<label><spring:message code="homePage.label.form.reg" /></label> <input
				type="text" tabindex="3" name="regEmail" placeholder="${email}"
				required> <input type="text" tabindex="4" name="regPass"
				placeholder="${password}" required> <input type="hidden"
				name="${_csrf.parameterName}" value="${_csrf.token}" /> <input
				type="submit" class="btnLogin" tabindex="20" value="Registration">
		</fieldset>
	</form>
	<br>
	<br>
	<br>
	<a href="https://github.com/midav341/traineeProjects" TARGET="_blank"><font size="2">GIT</font></a>
</body>
</html>