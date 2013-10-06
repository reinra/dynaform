package org.dynaform.xml;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormRepeat;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Rein Raudjärv
 * 
 * @see FormRepeat
 */
public class RepeatAdapterImpl<F extends Form> implements RepeatAdapter<F>, Factory<F>,
    FormRepeat.RepeatChangeListener<F> {

  /**
   * Factory for generating new {@link XmlForm}s.
   */
  private final Factory<XmlForm> factory;
  
  /**
   * All child elements.
   */
  private final Map<F, XmlForm> children = new LinkedHashMap<F, XmlForm>();
  
  private boolean initialized = false;

  public RepeatAdapterImpl(Factory<XmlForm> factory) {
    this.factory = factory;
  }
  
  public void init(FormRepeat<F> form) {
    form.addListener(this);
    initialized = true;
  }
  
  public Map<F, XmlForm> getChildren() {
    return children;
  }

  @SuppressWarnings("unchecked")
  public F create() {
    XmlForm child = factory.create();
    if (child == null)
      return null;
    
    F childForm = (F) child.getForm();
    if (initialized)
      children.put(childForm, child);
    return childForm;
  }

  public void onAdd(F childForm) {
    // Ensure a writer exists for the given formChild
    if (!children.containsKey(childForm))
      throw new AssertionError();
  }

  public void onRemove(F childForm) {
    // Remove the writer of the given formChild
    XmlForm xmlForm = children.remove(childForm);
    if (xmlForm == null)
      throw new AssertionError();
  }

  public void onSetDisabled(FormRepeat<F> sequence) { /* do nothing */ }
  public void onSetRepeatStyle(FormRepeat<F> sequence) { /* do nothing */ }

}