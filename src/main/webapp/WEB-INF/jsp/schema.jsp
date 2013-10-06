<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	version="1.2">

	<ui:widgetContext>

		<table cellspacing="0" cellpadding="3" border="1" bgcolor="#ffcccc">
			<tr>
				<td>Schema: <b><c:out value="${widget.xsdFile.name}"/></b></td>
				<td><ui:eventButton eventId="list" labelId="#Choose"/></td>
				<td><ui:eventButton eventId="reload" labelId="#Reload"/></td>
				<td><ui:eventButton eventId="deleteMetadata" labelId="#Delete DynaData" style="background-color: #ffaaaa" /></td>
			</tr>
		</table>

		<ui:widgetInclude id="f" />

	</ui:widgetContext>
</jsp:root>
