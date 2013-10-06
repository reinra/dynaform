<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	version="1.2">

	<ui:widgetContext>

		<table cellspacing="0" cellpadding="3" border="1" bgcolor="#ccccff">
			<tr>
				<td><ui:eventButton eventId="xsdView" labelId="#XSD" /></td>
				<td><ui:eventButton eventId="formEdit" labelId="#Form" /></td>
				<td><ui:eventButton eventId="xmlEdit" labelId="#XML" disabled="true"/></td>
				<td><ui:eventButton eventId="metadataEdit" labelId="#DynaData" /></td>
				<td><ui:eventButton eventId="save" labelId="#Save" /></td>
				<td><ui:eventButton eventId="reset" labelId="#Reset" /></td>
			</tr>
		</table>

		<ui:form id="f">
			<p><ui:textarea id="xml" rows="24" cols="120" /></p>
		</ui:form>

	</ui:widgetContext>
</jsp:root>
