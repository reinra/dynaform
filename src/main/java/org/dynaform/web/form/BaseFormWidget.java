package org.dynaform.web.form;

import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * Base Aranea form Widget.
 * 
 * @author Rein Raudjärv
 * 
 * @see StandardFormWidget
 * @see SectionFormWidget
 * @see RepeatFormWidget
 * @see SequenceFormWidget
 * @see ChoiceFormWidget
 * @see AllFormWidget
 */
public abstract class BaseFormWidget extends BaseUIWidget implements UiForm {

  private static final long serialVersionUID = 1L;

  public final LayoutContext getLayoutCtx() {
    return (LayoutContext) getEnvironment().requireEntry(LayoutContext.class);
  }

}
