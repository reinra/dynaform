<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    version="1.2">

    <ui:widgetContext>
		<p><b>All schemas:</b><ui:nbsp/><ui:eventButton eventId="refresh" labelId="#Refresh"/></p>

		<table>
			<c:forEach items="${widget.files}" var="file" varStatus="status">
				<tr>
					<td>
						<ui:eventLinkButton eventId="choose" eventParam="${status.index}">
							<c:out value="${file.name}" />
						</ui:eventLinkButton>
					</td>
				</tr>
			</c:forEach>
		</table>

    </ui:widgetContext>
</jsp:root>
