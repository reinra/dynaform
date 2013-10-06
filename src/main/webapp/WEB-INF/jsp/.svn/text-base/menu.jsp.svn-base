<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="1.2">

    <!-- WidgetContext id must be set here, because we want to render MenuWidget 
    	not TemplateRootWidget (which includes this JSP, thereby providing its own widget context) here. -->
    <ui:widgetContext id="menu">
		Menu:
        <c:forEach items="${widget.menu.subMenu}" var="item">
            <c:if test="${item.value.selected}">
                <ui:bold>
                   <ui:eventLinkButton eventId="menuSelect" eventParam="${item.value.label}" labelId="${item.value.label}"/>
                </ui:bold>
            </c:if>

            <c:if test="${not item.value.selected}">
                <ui:eventLinkButton eventId="menuSelect" eventParam="${item.value.label}" labelId="${item.value.label}"/>
            </c:if>
            <ui:nbsp/>
        </c:forEach>
        <ui:newLine/>
    </ui:widgetContext>
    
</jsp:root>
