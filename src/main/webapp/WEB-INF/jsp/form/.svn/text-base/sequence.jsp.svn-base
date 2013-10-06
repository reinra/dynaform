<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    version="1.2">

   <ui:widgetContext>

		<!-- 
		Sequnce: <c:out value="${widget.orientation}"/> / <c:out value="${widget.insideTable}"/>
		 -->

		<c:choose>
			<c:when test="${widget.insideTable}">
				<c:forEach items="${widget.children}" var="item">
					<ui:widgetInclude id="${item.key}" />
				</c:forEach>
			</c:when>

			<c:when test="${widget.orientation.vertical}">
				<table cellspacing="0" cellpadding="2" border="0">
					<c:forEach items="${widget.children}" var="item">
						<tr>
							<td>
								<ui:widgetInclude id="${item.key}" />
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:when>

			<c:otherwise>
				<table cellspacing="0" cellpadding="2" border="0">
					<tr>
						<c:forEach items="${widget.children}" var="item">
							<td>
								<ui:widgetInclude id="${item.key}" />
							</td>
						</c:forEach>
					</tr>
				</table>
			</c:otherwise>
		</c:choose>

  </ui:widgetContext>
</jsp:root>
