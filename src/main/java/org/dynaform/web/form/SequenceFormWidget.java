package org.dynaform.web.form;

import org.dynaform.xml.form.layout.SectionStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Sequence form composition widget.
 * 
 * @author Rein Raudjärv
 */
public class SequenceFormWidget extends BaseFormWidget {

	private static final long serialVersionUID = 1L;

	private final List<UiForm> children = new ArrayList<UiForm>();
	
	public void addChild(UiForm form) {
	  children.add(form);
	}
	
	@Override
	protected void init() throws Exception {
		setViewSelector("form/sequence");
		
    // Register child forms
    int i = 0;
    for (UiForm child : children)
      addWidget(String.valueOf(i++), child);
	}
	
  public SectionStyle getOrientation() {
    return getLayoutCtx().getSectionStyle();
  }
  
  public boolean isInsideTable() {
    return getLayoutCtx().isTableRow();
  }
  
  public boolean saveAndValidate() {
    boolean valid = true;
    for (UiForm form : children)
      valid &= form.saveAndValidate();
    return valid;
  }

}
