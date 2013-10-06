package org.dynaform.xml.form;

public class ToggleFormVisitor extends BaseFormVisitor {

  public static final FormVisitor DISABLER = new ToggleFormVisitor(true);
  public static final FormVisitor ENABLER = new ToggleFormVisitor(false);
  
  private final boolean disabled;
  
  private ToggleFormVisitor(boolean disabled) {
    this.disabled = disabled;
  }
  
  @Override
  public <E> void element(FormElement<E> element) {
    element.setDisabled(disabled);
  }
  
  @Override
  public <F extends Form> void repeat(FormRepeat<F> sequence) {
    sequence.setDisabled(disabled);
    super.repeat(sequence);
  }

}
