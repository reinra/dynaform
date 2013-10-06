<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    version="1.2">

    <ui:widgetContext>
		<ui:form id="f">

		<c:choose>
			<c:when test="${widget.insideTable}">
				<c:forEach items="${widget.form.elements}" var="item">
					<td><ui:automaticFormElement id="${item.key}" /></td>
				</c:forEach>
			</c:when>

			<c:when test="${widget.orientation.vertical}">
				<table cellspacing="0" cellpadding="3" border="1">
					<!-- 
					<tr>
						<td colspan="2" bgcolor="#dddddd">
							Standard: <c:out value="${widget.orientation}"/>
						</td>
					</tr>
					 -->
					<c:forEach items="${widget.form.elements}" var="item">
						<tr>
							<td valign="top" bgcolor="#eeeeee"><ui:label id="${item.key}"/></td>
							<td><ui:automaticFormElement id="${item.key}" /></td>
						</tr>
					</c:forEach>
				</table>
			</c:when>

			<c:otherwise>
				<table cellspacing="0" cellpadding="3" border="1">
					<tr>
						<!-- 
						<td rowspan="2" bgcolor="#dddddd">
							Standard: <c:out value="${widget.orientation}"/>
						</td>
						 -->
						<c:forEach items="${widget.form.elements}" var="item">
							<td valign="top" bgcolor="#eeeeee"><ui:label id="${item.key}"/></td>
						</c:forEach>
					</tr>
					<tr>
						<c:forEach items="${widget.form.elements}" var="item">
							<td><ui:automaticFormElement id="${item.key}" /></td>
						</c:forEach>
					</tr>
				</table>
			</c:otherwise>
		</c:choose>

		</ui:form>
    </ui:widgetContext>
</jsp:root>
