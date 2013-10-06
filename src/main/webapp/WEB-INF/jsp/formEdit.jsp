<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	version="1.2">

	<ui:widgetContext>

		<table cellspacing="0" cellpadding="3" border="1" bgcolor="#ccccff">
			<tr>
				<td><ui:eventButton eventId="xsdView" labelId="#XSD" /></td>
				<td><ui:eventButton eventId="form" labelId="#Form" disabled="true" /></td>
				<td><ui:eventButton eventId="xmlEdit" labelId="#XML" /></td>
				<td><ui:eventButton eventId="metadataEdit" labelId="#DynaData" /></td>
				<td><ui:eventButton eventId="save" labelId="#Save" /></td>
				<td><ui:eventButton eventId="reset" labelId="#Reset" /></td>
				<td>
					<ui:form id="toolbar">
						<ui:formElement id="save">
							<ui:checkbox/>
							<ui:label showColon="false" />
						</ui:formElement>
					</ui:form>
				</td>
			</tr>
		</table>

		<br/>

		<ui:widgetInclude id="form" />

	</ui:widgetContext>
</jsp:root>
