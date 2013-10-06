package org.dynaform.web.demo;

import org.araneaframework.uilib.core.BaseUIWidget;

public class RootWidget extends BaseUIWidget {

  private static final long serialVersionUID = 1L;

	protected void init() throws Exception {
	  MenuWidget menuWidget = new MenuWidget();
		addWidget("menu", menuWidget);
		menuWidget.start(new SchemaListWidget());
		setViewSelector("root");
	}
	
}
