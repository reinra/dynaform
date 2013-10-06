<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">
	<ui:widgetContext>
		<![CDATA[
			<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">	        		
		]]>
		<html xmlns="http://www.w3.org/1999/xhtml">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

			<ui:importScripts/>
			<ui:importStyles/>

			<ui:importStyles file="styles/_styles_global.css" media="all"/>
			<ui:importStyles group="templateScreenStyleGroup.css" media="screen"/>

		<ui:richTextAreaInit>
			<ui:attribute name="theme" value="advanced"/>
			<ui:attribute name="theme_advanced_buttons1" value="bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo,link,unlink,code"/>
			<ui:attribute name="theme_advanced_buttons3" value=""/>
			<ui:attribute name="theme_advanced_toolbar_location" value="top"/>
			<ui:attribute name="theme_advanced_toolbar_align" value="left"/>
			<ui:attribute name="theme_advanced_path_location" value="bottom"/>
			<ui:attribute name="extended_valid_elements" value="a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"/>
		</ui:richTextAreaInit>

			<title>DynaForm</title>
<style type="text/css">

body { font-size: 10pt; }
body, html { margin-left: 6px; margin-right: 6px; }
textarea { font-family: Courier New, Verdana, serif; }

button { background-color: #eeeeee; font-weight: bold; }
button[disabled] { background-color: #ffffcc; }

a { text-decoration: none; color: #0000ff; }
a:hover { text-decoration: underline; color: #0000ff; }

</style>
		</head>

		<ui:body>

				<ui:systemForm method="POST">
					<ui:registerScrollHandler/>
					<ui:registerPopups/>
					<ui:registerOverlay/>

					<div id="content">
						<ui:messages type="info" styleClass="msg-info"/>
						<ui:messages type="error" styleClass="msg-error"/>

						<ui:widgetInclude id="menu"/>
					</div>

					<div class="clear1"><ui:nbsp/></div>
				</ui:systemForm>

		</ui:body>
		</html>
	</ui:widgetContext>
</jsp:root>
