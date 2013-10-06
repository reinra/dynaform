<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    version="1.2">

   <ui:widgetContext>

		<c:if test="${widget.insideTable}">
			<![CDATA[<td>]]>
		</c:if>

		<ui:updateRegion id="all">

		<c:choose>

			<!-- VERTICAL -->
			<c:when test="${widget.orientation.vertical}">
				<table cellspacing="0" cellpadding="3" border="1">

					<c:forEach items="${widget.selectedIndexes}" var="index" varStatus="status">
						<tr>
							<!-- Index -->
							<c:if test="${widget.showIndexes}">
								<td>
									<c:out value="${status.index+1}"/>.
									(<c:out value="${index+1}"/>.)<ui:nbsp/>
								</td>
							</c:if>
							<!-- Body -->
							<td>
								<ui:widgetInclude id="${index}" />
							</td>
							<!-- Up Button -->
							<td>
								<c:if test="${!status.first}">
									<ui:eventButton eventId="moveUp" eventParam="${status.index}" updateRegions="all">Up</ui:eventButton>
								</c:if>
							</td>
							<!-- Down Button -->
							<td>
								<c:if test="${!status.last}">
									<ui:eventButton eventId="moveDown" eventParam="${status.index}" updateRegions="all">Down</ui:eventButton>
								</c:if>
							</td>
							<!-- Disable Button -->
							<c:if test="${!widget.allChildrenRequired}">
								<td>
									<ui:eventButton eventId="disable" eventParam="${status.index}" updateRegions="all">Disable</ui:eventButton>
								</td>
							</c:if>
						</tr>
					</c:forEach>

					<c:forEach items="${widget.unselectedIndexes}" var="index" varStatus="status">
						<tr>
							<!-- Index -->
							<c:if test="${widget.showIndexes}">
								<td>
									<c:out value="${widget.selectedCount + status.index + 1}"/>.
									(<c:out value="${index+1}"/>.)<ui:nbsp/>
								</td>
							</c:if>
							<!-- Body -->
							<td>
								<ui:widgetInclude id="${index}" />
							</td>
							<td/>
							<td/>
							<td>
								<ui:eventButton eventId="enable" eventParam="${index}" updateRegions="all">Enable</ui:eventButton>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:when>

			<!-- HORIZONTAL -->
			<c:otherwise>
				<table cellspacing="0" cellpadding="3" border="1">

					<!-- Index Row -->
					<c:if test="${widget.showIndexes}">
						<tr>
							<c:forEach items="${widget.selectedIndexes}" var="index" varStatus="status">
								<td>
									<c:out value="${status.index+1}"/>.
									(<c:out value="${index+1}"/>.)<ui:nbsp/>
								</td>
							</c:forEach>

							<c:forEach items="${widget.unselectedIndexes}" var="index" varStatus="status">
								<td>
									<c:out value="${widget.selectedCount + status.index + 1}"/>.
									(<c:out value="${index+1}"/>.)<ui:nbsp/>
								</td>
							</c:forEach>
						</tr>
					</c:if>

					<!-- Body Row -->
					<tr>
						<c:forEach items="${widget.selectedIndexes}" var="index">
							<td>
								<ui:widgetInclude id="${index}" />
							</td>
						</c:forEach>

						<c:forEach items="${widget.unselectedIndexes}" var="index">
							<td>
								<ui:widgetInclude id="${index}" />
							</td>
						</c:forEach>
					</tr>

					<!-- Move Left Row -->
					<tr>
						<c:forEach items="${widget.selectedIndexes}" varStatus="status">
							<td>
								<c:if test="${!status.first}">
									<ui:eventButton eventId="moveUp" eventParam="${status.index}" updateRegions="all">Left</ui:eventButton>
								</c:if>
							</td>
						</c:forEach>

						<c:forEach items="${widget.unselectedIndexes}">
							<td/>
						</c:forEach>
					</tr>

					<!-- Move Right Row -->
					<tr>
						<c:forEach items="${widget.selectedIndexes}" varStatus="status">
							<td>
								<c:if test="${!status.last}">
									<ui:eventButton eventId="moveDown" eventParam="${status.index}" updateRegions="all">Right</ui:eventButton>
								</c:if>
							</td>
						</c:forEach>

						<c:forEach items="${widget.unselectedIndexes}">
							<td/>
						</c:forEach>
					</tr>

					<!-- Enable/Disable Button Row -->
					<c:if test="${!widget.allChildrenRequired}">
						<tr>
							<c:forEach items="${widget.selectedIndexes}" varStatus="status">
								<td>
									<ui:eventButton eventId="disable" eventParam="${status.index}" updateRegions="all">Disable</ui:eventButton>
								</td>
							</c:forEach>

							<c:forEach items="${widget.unselectedIndexes}" var="index">
								<td>
									<ui:eventButton eventId="enable" eventParam="${index}" updateRegions="all">Enable</ui:eventButton>
								</td>
							</c:forEach>
						</tr>
					</c:if>

				</table>
			</c:otherwise>
		</c:choose>

		</ui:updateRegion>

		<c:if test="${widget.insideTable}">
			<![CDATA[</td>]]>
		</c:if>

  </ui:widgetContext>
</jsp:root>
