package org.dynaform.web.form;


import org.dynaform.xml.form.layout.SectionStyle;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;

/**
 * Standard form containing a set of form controls.
 * 
 * @author Rein Raudjärv
 */
public class StandardFormWidget extends BaseFormWidget {

  private static final long serialVersionUID = 1L;

  /**
   * Aranea form widget. 
   */
  private final FormWidget form = new FormWidget();

  /**
   * Listeners for storing form data by the form element Ids.
   */
  private final Map<String, ElementEventListener<?>> listeners = new HashMap<String, ElementEventListener<?>>();

  private String nextId() {
    return String.valueOf(form.getElements().size());
  }
  
  public <E> FormElement addElement(
      String labelId,
      Control control,
      Data data,
      E initialValue,
      boolean mandatory,
      ElementEventListener<?> listener) {

    String id = nextId();
    listeners.put(id, listener);
    return form.addElement(id, labelId, control, data, initialValue, mandatory);
  }

  /**
   * Merges another {@link StandardFormWidget} with the current one. 
   * 
   * @param widget a standard form to be merged.
   * @return this standard form.
   */
  public StandardFormWidget merge(StandardFormWidget widget) {
    Map<String, String> nameMappings = new HashMap<String, String>();

    // Merge form
    Map<String, GenericFormElement> elements = widget.form.getElements();
    for (Entry<String, GenericFormElement> entry: elements.entrySet()) {
      String oldName = entry.getKey();
      String newName = nextId();
      nameMappings.put(oldName, newName);

      form.addElement(newName, entry.getValue());
    }

    // Merge listeners
    for (Entry<String, ElementEventListener<?>> entry : widget.listeners.entrySet()) {
      String oldName = entry.getKey();
      String newName = nameMappings.get(oldName);

      listeners.put(newName, entry.getValue());
    }

    return this;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("form/standard");
    addWidget("f", form);
  }

  public SectionStyle getOrientation() {
    return getLayoutCtx().getSectionStyle();
  }

  public boolean isInsideTable() {
    return getLayoutCtx().isTableRow();
  }

  public FormWidget getForm() {
    return form;
  }
  
  public boolean saveAndValidate() {
    boolean allValid = true;
    for (Entry<String, ElementEventListener<?>> entry : listeners.entrySet()) {
      String name = entry.getKey();
      FormElement element = form.getElementByFullName(name);
      
      boolean valid = element.convertAndValidate();
      if (valid) {
        ElementEventListener listener = entry.getValue();
        Object value = element.getValue();
        listener.setValue(value);
      }
      
      allValid &= valid;
    }
    return allValid;
  }

  /**
   * Element form event listener.
   * 
   * @author Rein Raudjärv
   * 
   * @see StandardFormWidget
   */
  public static interface ElementEventListener<E> {
    /**
     * Handle value change.
     */
    void setValue(E value);
  }

}
