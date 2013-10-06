package org.dynaform.xml.form;

/**
 * Visitor for all selected form components.
 * 
 * @author Rein Raudjärv
 */
public class BaseFormVisitor implements FormVisitor {

	public <E> void element(FormElement<E> element) {
		// do nothing
	}
	
	public <F extends Form> void section(FormSection<F> section) {
	  section.getContent().accept(this);
	}

	public <F extends Form> void repeat(FormRepeat<F> sequence) {
		for (F child : sequence.getChildren())
			child.accept(this);
	}
	
	public void sequence(FormSequence sequence) {
	  for (Form child : sequence.getChildren())
	    child.accept(this);
	}

  public void choice(FormChoice choice) {
    choice.getSelectedChild().accept(this);
  }

  public void all(FormAll all) {
    for (Form child : all.getSelectedChildren())
      child.accept(this);
  }

}
