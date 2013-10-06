<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	version="1.2">

	<ui:widgetContext>

		<c:choose>
			<c:when test="${widget.tableContents}">
				<ui:widgetInclude id="0" />
			</c:when>

			<c:when test="${widget.insideTable}">
				<td><ui:widgetInclude id="0" /></td>
			</c:when>

			<c:otherwise>
				<table cellspacing="0" cellpadding="3" border="1">
					<tr>
						<jsp:element name="td">
							<jsp:attribute name="bgcolor">
								<c:out value="${widget.bgColor}" />
							</jsp:attribute>
						 	<jsp:body>
								<b><c:out value="${widget.label}" /></b>
							</jsp:body>
						</jsp:element>
					</tr>
		
					<tr>
						<td><ui:widgetInclude id="0" /></td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose>

	</ui:widgetContext>

</jsp:root>
