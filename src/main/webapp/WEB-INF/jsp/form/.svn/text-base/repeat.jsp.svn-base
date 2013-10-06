<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    version="1.2">

  <ui:widgetContext>

	<c:if test="${widget.insideTable}">
		<![CDATA[<td>]]>
	</c:if>

	<!-- 
	Repeat: <c:out value="${widget.repeatStyle}"/>
	 -->

	<ui:updateRegion id="repeat">

		<c:choose>
			<!-- TABLE -->
			<c:when test="${widget.repeatStyle eq 'TABLE'}">
				<table cellspacing="0" cellpadding="3" border="1">
				
				<!-- Table Title -->
				<c:if test="${not empty widget.title}">
					<tr>
						<jsp:element name="td">
							<jsp:attribute name="colspan"><c:out value="${widget.headerCount + 2}"/></jsp:attribute>
							<jsp:attribute name="align">center</jsp:attribute>
							<jsp:attribute name="bgcolor">#88dd44</jsp:attribute>
							<jsp:body><b><c:out value="${widget.title}"/></b></jsp:body>
						</jsp:element>
					</tr>
				</c:if>

				<!-- Table Header -->
				<c:if test="${not empty widget.headers}">
					<tr>
						<td bgcolor="#aaff66">#</td>
						<c:forEach items="${widget.headers}" var="item" varStatus="status">
							<td bgcolor="#aaff66">
								<c:out value="${item}"></c:out>
							</td>
						</c:forEach>
						<td bgcolor="#aaff66"/>
					</tr>
				</c:if>

				<!-- "No Entries" -->
				<c:if test="${empty widget.children}">
					<tr>
						<jsp:element name="td">
							<jsp:attribute name="colspan"><c:out value="${widget.headerCount + 2}"/></jsp:attribute>
							<jsp:attribute name="align">center</jsp:attribute>
							<jsp:body>(No entries)</jsp:body>
						</jsp:element>
					</tr>
				</c:if>
		
				<!-- Table Body -->
				<c:forEach items="${widget.children}" var="item" varStatus="status">
					<tr>
						<!-- Row Index -->
						<td>
							<c:out value="${status.index+1}">.</c:out>
						</td>

						<!-- Row Body -->
						<ui:widgetInclude id="${item.key}" />

						<!-- Remove Button -->
						<td>
							<c:if test="${empty widget.min or widget.size gt widget.min}">
								<ui:eventButton disabled="${widget.disabled}" eventId="remove" eventParam="${item.key}" updateRegions="repeat">Remove</ui:eventButton>
							</c:if>
						</td>
					</tr>
				</c:forEach>
		
				<!-- Add Button -->
				<c:if test="${empty widget.max or widget.size lt widget.max}">
					<tr>
						<jsp:element name="td">
							<jsp:attribute name="colspan"><c:out value="${widget.headerCount + 2}"/></jsp:attribute>
							<jsp:attribute name="align">center</jsp:attribute>
							<jsp:body>
								<ui:eventButton disabled="${widget.disabled}" eventId="add" updateRegions="repeat">Add<c:if test="${not empty widget.title}"><c:out value=" ${widget.title}"/></c:if></ui:eventButton>
							</jsp:body>
						</jsp:element>
					</tr>
				</c:if>
		
				</table>
			</c:when>

			<!-- NORMAL or ROWS -->
			<c:when test="${widget.repeatStyle ne 'COLUMNS'}">
				<table cellspacing="0" cellpadding="3" border="1">

				<c:forEach items="${widget.children}" var="item" varStatus="status">
					<tr>
						<c:if test="${empty widget.max or widget.max gt 1}">
						<td>
							<c:out value="${status.index+1}">.</c:out>
						</td>
						</c:if>
						<td>
							<ui:widgetInclude id="${item.key}" />
						</td>
						<td>
							<c:if test="${empty widget.min or widget.size gt widget.min}">
								<ui:eventButton disabled="${widget.disabled}" eventId="remove" eventParam="${item.key}" updateRegions="repeat">Remove</ui:eventButton>
							</c:if>
						</td>
					</tr>
				</c:forEach>
		
				<c:if test="${empty widget.max or widget.size lt widget.max}">
					<tr>
						<td colspan="3">
							<ui:eventButton disabled="${widget.disabled}" eventId="add" updateRegions="repeat">Add<c:if test="${not empty widget.title}"><c:out value=" ${widget.title}"/></c:if></ui:eventButton>
						</td>
					</tr>		
				</c:if>
		
				</table>
			</c:when>

			<!-- COLUMNS -->
			<c:otherwise>
				<table cellspacing="0" cellpadding="3" border="1">
		
				<tr>
					<c:if test="${empty widget.children}">
						<td rowspan="3">No entries</td>				
					</c:if>
					
					<c:forEach items="${widget.children}" var="item" varStatus="status">
						<td>
							<c:out value="${status.index+1}">.</c:out>
						</td>
					</c:forEach>

					<td rowspan="3">
						<c:if test="${empty widget.max or widget.size lt widget.max}">
							<ui:eventButton disabled="${widget.disabled}" eventId="add" updateRegions="repeat">Add<c:if test="${not empty widget.title}"><c:out value=" ${widget.title}"/></c:if></ui:eventButton>
						</c:if>
					</td>
				</tr>
				<tr>
					<c:forEach items="${widget.children}" var="item" varStatus="status">
						<td>
							<ui:widgetInclude id="${item.key}" />
						</td>
					</c:forEach>
				</tr>
				<tr>
					<c:forEach items="${widget.children}" var="item" varStatus="status">
						<td>
							<c:if test="${empty widget.min or widget.size gt widget.min}">
								<ui:eventButton disabled="${widget.disabled}" eventId="remove" eventParam="${item.key}" updateRegions="repeat">Remove</ui:eventButton>
							</c:if>
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
