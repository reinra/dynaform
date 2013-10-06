<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    version="1.2">

    <ui:widgetContext>

		<table cellspacing="0" cellpadding="3" border="1" bgcolor="#ccccff">
			<tr>
				<td><ui:eventButton eventId="xsdView" labelId="#XSD" disabled="true" /></td>
				<td><ui:eventButton eventId="formEdit" labelId="#Form" /></td>
				<td><ui:eventButton eventId="xmlEdit" labelId="#XML" /></td>
				<td><ui:eventButton eventId="metadataEdit" labelId="#DynaData" /></td>
			</tr>
		</table>

		<pre>
		<ol>
			<c:forEach items="${widget.lines}" var="line">
				<li>
					<c:out value="${line}" />
				</li>			
			</c:forEach>
		</ol>
		</pre>

    </ui:widgetContext>
</jsp:root>
