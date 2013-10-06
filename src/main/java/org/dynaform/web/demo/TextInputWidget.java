package org.dynaform.web.demo;

import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.data.StringData;

public class TextInputWidget extends BaseUIWidget {
	
	private static final long serialVersionUID = 1L;
	
	private FormWidget form;

	protected void init() throws Exception {
		setViewSelector("textInput");
		form = new FormWidget();
		form.addElement("text", "#Text", new TextareaControl(), new StringData(), true);
		addWidget("f", form);
	}

	public void handleEventSubmit() throws Exception {
	  if (form.convertAndValidate()) {
	    String value = (String) form.getValueByFullName("text");
	    getFlowCtx().finish(value);
	  }
	}
	
	public void handleEventCancel() throws Exception {
	  getFlowCtx().cancel();
	}
	
}
