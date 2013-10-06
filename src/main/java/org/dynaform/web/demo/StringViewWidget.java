package org.dynaform.web.demo;

import org.araneaframework.uilib.core.BaseUIWidget;

public class StringViewWidget extends BaseUIWidget {
	
	private static final long serialVersionUID = 1L;
	
	private String content;

	public StringViewWidget(String content) {
		this.content = content;
	}

	protected void init() throws Exception {
		setViewSelector("stringView");
	}

	public String getContent() {
		return content;
	}

	public void handleEventReturn() throws Exception {
		getFlowCtx().cancel();
	}
	
}
