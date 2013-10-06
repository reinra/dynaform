package org.dynaform.xml.form.label;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormRepeat;
import org.dynaform.xml.form.FormSection;
import org.dynaform.xml.form.FormSequence;
import org.dynaform.xml.form.Labeled;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SequenceHeaderFactory implements HeaderFactory {

  private static final Log log = LogFactory.getLog(SequenceHeaderFactory.class);
  
  private final FormRepeat<Form> formRepeat;

  public SequenceHeaderFactory(FormRepeat<Form> sequenceForm) {
    this.formRepeat = sequenceForm;
  }

  public String getHedaer() {
    List<Form> children = formRepeat.getChildren();
    if (!children.isEmpty()) {
      Form child = children.get(0);
      return getHeader(child);
    }

    Form child = formRepeat.add();
    try {
      return getHeader(child);
    }
    finally {
      formRepeat.remove(child);
    }
  }

  public List<String> getSubHeaders() {
    List<Form> children = formRepeat.getChildren();
    if (!children.isEmpty()) {
      Form child = children.get(0);
      return getSubHeaders(child);
    }

    Form child = formRepeat.add();
    try {
      return getSubHeaders(child);
    }
    finally {
      formRepeat.remove(child);
    }
  }

  private String getHeader(Form child) {
    if (child instanceof Labeled)
      return ((Labeled) child).getLabel();
    
    return null;
  }

  private List<String> getSubHeaders(Form child) {
    if (child instanceof FormSection) {
      FormSection section = (FormSection) child;
      return section.getContent().apply(LabelVisitor.getInstance());
    }
    if (child instanceof FormSequence) {
      return child.apply(LabelVisitor.getInstance());
    }

    return null;
  }

}
