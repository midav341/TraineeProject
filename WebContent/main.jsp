<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="nameApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MAIN</title>
<style>
body {
	margin: 0; /* Убираем отступы */
}

.block {
	margin-left: 20px; /* Отступ слева */
	white-space: nowrap;
}
</style>
<script type="text/javascript">
	var springMessages = new Array();

	springMessages['folder'] = '<spring:message code="js.folder" javaScriptEscape="true" />';
	springMessages['folder.create'] = '<spring:message code="js.folder.create" javaScriptEscape="true" />';
	springMessages['folder.create.button'] = '<spring:message code="js.folder.create.button" javaScriptEscape="true" />';
	springMessages['folder.create.name'] = '<spring:message code="js.folder.create.name" javaScriptEscape="true" />';
	springMessages['folder.delete'] = '<spring:message code="js.folder.delete" javaScriptEscape="true" />';
	springMessages['folder.delete.button'] = '<spring:message code="js.folder.delete.button" javaScriptEscape="true" />';
	springMessages['folder.rename'] = '<spring:message code="js.folder.rename" javaScriptEscape="true" />';
	springMessages['folder.rename.button'] = '<spring:message code="js.folder.rename.button" javaScriptEscape="true" />';
	springMessages['folder.rename.name'] = '<spring:message code="js.folder.rename.name" javaScriptEscape="true" />';
	springMessages['folder.copy'] = '<spring:message code="js.folder.copy" javaScriptEscape="true" />';
	springMessages['folder.copy.button'] = '<spring:message code="js.folder.copy.button" javaScriptEscape="true" />';
	springMessages['folder.copy.name'] = '<spring:message code="js.folder.copy.name" javaScriptEscape="true" />';
	springMessages['folder.move'] = '<spring:message code="js.folder.move" javaScriptEscape="true" />';
	springMessages['folder.move.button'] = '<spring:message code="js.folder.move.button" javaScriptEscape="true" />';
	springMessages['folder.move.name'] = '<spring:message code="js.folder.move.name" javaScriptEscape="true" />';

	springMessages['file'] = '<spring:message code="js.file" javaScriptEscape="true" />';
	springMessages['file.upload'] = '<spring:message code="js.file.upload" javaScriptEscape="true" />';
	springMessages['file.upload.button'] = '<spring:message code="js.file.upload.button" javaScriptEscape="true" />';
	springMessages['file.delete'] = '<spring:message code="js.file.delete" javaScriptEscape="true" />';
	springMessages['file.rename'] = '<spring:message code="js.file.rename" javaScriptEscape="true" />';
	springMessages['file.move'] = '<spring:message code="js.file.move" javaScriptEscape="true" />';
	springMessages['file.copy'] = '<spring:message code="js.file.copy" javaScriptEscape="true" />';
	springMessages['download'] = '<spring:message code="js.download" javaScriptEscape="true" />';
</script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.8/angular.js"></script>
<script src="resources/js/FileAndFoldersOnClickListener.js"></script>
</head>
<body ng-controller="NameCtrl">

	<br>
	<%
		request.setCharacterEncoding("UTF-8");
		String answerText = (String) request.getAttribute("answerText");
		String email = (String) request.getSession().getAttribute("email");
		if (answerText != null) {
	%>
	<%=answerText%>
	<%
		}
	%>
	<spring:message code="js.download.app"
		var="downloadApp" />
	<table width="100%">
		<tr>
			<td width="50%" align ="right">
				<form action="DownloadFile" method="post">
					<input type="hidden" name="filepath" value="DropBoxApp.jar"> <input
						type="submit" value="${downloadApp}">
				</form>
			</td>
			<td width="50%">
				<form action="logout" method="post" id="logoutForm" align="right"
					style="margin-right: 20px">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /> <input type="submit" value="logout">
				</form> <br>
				<div align="right" style="margin-right: 20px">
					<a href="main?lang=en"> en </a> | <a href="main?lang=ru"> ru </a>
				</div>
			</td>
		</tr>
	</table>

	<br>
	<br>
	<div align=center>
		<table class="block">
			<tr>
				<td><spring:message code="mainPage.label.headOfTree" /> <%=email%>:<br>
					↓
					<button ng-click="rootBtnClick('<%=email%>')">
						<%=email%>
					</button>
					<dir id="dir" content="tree"></dir></td>
				<td width="100dp"></td>
				<td>
					<div ng-bind-html="controlsText"></div>
				</td>
			</tr>
		</table>
	</div>
<a href="https://github.com/midav341/traineeProjects" TARGET ="_blank"><font size="2">GIT</font></a>
</body>
</html>