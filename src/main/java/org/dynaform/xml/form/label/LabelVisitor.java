package org.dynaform.xml.form.label;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormAll;
import org.dynaform.xml.form.FormChoice;
import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.form.FormFunction;
import org.dynaform.xml.form.FormRepeat;
import org.dynaform.xml.form.FormSection;
import org.dynaform.xml.form.FormSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LabelVisitor implements FormFunction<List<String>> {

  public static LabelVisitor getInstance() {
    return new LabelVisitor(1);
  }

  private final int depth;

  private LabelVisitor(int depth) {
    this.depth = depth;
  }

  public List<String> sequence(FormSequence composite) {
    if (depth == 0)
      return null;

    LabelVisitor childVisitor = new LabelVisitor(depth - 1);

    List<String> result = new ArrayList<String>();
    for (Form child : composite.getChildren()) {
      List<String> subResult = child.apply(childVisitor);
      if (subResult == null)
        result.add(null);
      else
        result.addAll(subResult);
    }
    return result;
  }

  public <E> List<String> element(FormElement<E> element) {
    return Arrays.asList(new String[] { element.getLabel() });
  }

  public <F extends Form> List<String> section(FormSection<F> section) {
    return Arrays.asList(new String[] { section.getLabel() });
  }

  public <F extends Form> List<String> repeat(FormRepeat<F> sequence) {
    return null;
  }

  public List<String> choice(FormChoice choice) {
    return null;
  }

  public List<String> all(FormAll all) {
    return null;
  }
  
}
