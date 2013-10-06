package org.dynaform.xml.form;

/**
 * @author Rein Raudjärv
 */
public class ElementCounter extends BaseFormVisitor {

  private int counter;

  public <E> void element(FormElement<E> element) {
    counter++;
  }
  
  public int getCount() {
    return counter;
  }
  
  public static int count(Form form) {
    if (form == null)
      return 0;
    
    ElementCounter counter = new ElementCounter();
    form.accept(counter);
    return counter.getCount();
  }

}
