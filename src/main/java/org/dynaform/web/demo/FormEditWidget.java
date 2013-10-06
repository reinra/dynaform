package org.dynaform.web.demo;

import org.dynaform.web.form.LayoutContext;
import org.dynaform.web.form.RootLayoutContext;
import org.dynaform.web.form.UiForm;

import java.util.logging.Logger;
import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.data.BooleanData;

public class FormEditWidget extends BaseUIWidget {
	
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(FormEditWidget.class.getName());
	
	private final UiForm form;
	
	private final FormWidget toolbar = new FormWidget();
	
	public FormEditWidget(UiForm form) {
		this.form = form;
	}
	
	protected void init() throws Exception {
		setViewSelector("formEdit");
		toolbar.addElement("save", "#Auto save", new CheckboxControl(), new BooleanData(), Boolean.TRUE, false);
		addWidget("form", form);
		addWidget("toolbar", toolbar);
	}
	
  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(getEnvironment(), LayoutContext.class, new RootLayoutContext());
  }
	
	private SchemaContext getContext() {
	  return (SchemaContext) getEnvironment().requireEntry(SchemaContext.class);
	}
	
	public void handleEventXsdView() {
	  if (isAutosaveDisabled() || validateAndSave())
	    getContext().selectSchemaView();
	}
	
  public void handleEventXmlEdit() {
    if (isAutosaveDisabled() || validateAndSave())
      getContext().selectXmlEdit();
  }
  
  public void handleEventMetadataEdit() {
    if (isAutosaveDisabled() || validateAndSave())
      getContext().selectMetadataEdit();
  }
  
  public void handleEventSave() {
    if (validateAndSave())
      getMessageCtx().showInfoMessage("Data saved successfully.");
  }
  
  public void handleEventReset() {
    getContext().selectFormEdit();
    getMessageCtx().showInfoMessage("Data was reset successfully.");
  }
  
	private boolean validateAndSave() {
	  boolean valid = form.saveAndValidate();
	  log.info("Form is valid: " + valid);
	  return valid;
	}
	
	private boolean isAutosaveEnabled() {
	  if (toolbar.convertAndValidate()) {
	    Boolean autosave = (Boolean) toolbar.getValueByFullName("save");
	    return autosave.booleanValue();
	  }
	  return false;
	}
	
	private boolean isAutosaveDisabled() {
	  return !isAutosaveEnabled();
	}
	
}
