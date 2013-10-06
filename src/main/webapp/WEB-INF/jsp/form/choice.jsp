<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    version="1.2">

   <ui:widgetContext>

		<c:if test="${widget.insideTable}">
			<![CDATA[<td>]]>
		</c:if>

		<ui:updateRegion id="choice">

		<c:choose>
	
			<!-- VERTICAL -->
			<c:when test="${widget.orientation.vertical}">
				<table cellspacing="0" cellpadding="3" border="1">
		
					<c:forEach items="${widget.children}" var="item">
						<tr>
							<td>
								<c:choose>
									<c:when test="${not (item.key eq widget.selectedIndex)}">
										<ui:eventButton eventId="select" eventParam="${item.key}" updateRegions="choice">Select</ui:eventButton>
									</c:when>
									<c:otherwise>
										<ui:eventButton disabled="true">Select</ui:eventButton>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<ui:widgetInclude id="${item.key}" />
							</td>
						</tr>
					</c:forEach>
		
				</table>
			</c:when>

			<!-- HORIZONTAL -->
			<c:otherwise>
				<table cellspacing="0" cellpadding="3" border="1">
		
					<!-- Select Button Row -->
					<tr>
						<c:forEach items="${widget.children}" var="item">
							<td>
								<c:choose>
									<c:when test="${not (item.key eq widget.selectedIndex)}">
										<ui:eventButton eventId="select" eventParam="${item.key}" updateRegions="choice">Select</ui:eventButton>
									</c:when>
									<c:otherwise>
										<ui:eventButton disabled="true">Select</ui:eventButton>
									</c:otherwise>
								</c:choose>
							</td>
						</c:forEach>
					</tr>

					<!-- Body Row -->
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

		</ui:updateRegion>

		<c:if test="${widget.insideTable}">
			<![CDATA[</td>]]>
		</c:if>

  </ui:widgetContext>
</jsp:root>
