package org.dynaform.web.demo;

import org.dynaform.xml.form.BaseFormVisitor;
import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormAll;
import org.dynaform.xml.form.FormChoice;
import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.form.FormRepeat;
import org.dynaform.xml.form.FormSection;
import org.dynaform.xml.form.FormSequence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Rein Raudjärv
 */
public class DebugVisitor extends BaseFormVisitor {
  
  private static final Log log = LogFactory.getLog(DebugVisitor.class);
  
  private static final DebugVisitor INSTANCE = new DebugVisitor();

  public static DebugVisitor getInstance() {
    return INSTANCE;
  }
  
  private DebugVisitor() {
  }
  
  public <E> void element(FormElement<E> element) {
    log.debug("Form Element " + element.getFullSelector());
  }
  
  public <F extends Form> void section(FormSection<F> section) {
    log.debug("Form Section: " + section.getFullSelector());
    super.section(section);
  }
  
  public <F extends Form> void repeat(FormRepeat<F> repeat) {
    log.debug("Form Repeat: " + repeat.getFullSelector());
    super.repeat(repeat);
  }
  
  public void sequence(FormSequence sequence) {
    log.debug("Form Sequence: " + sequence.getFullSelector());
    super.sequence(sequence);
  }

  public void choice(FormChoice choice) {
    log.debug("Form Choice: " + choice.getFullSelector());
    super.choice(choice);
  }
  
  public void all(FormAll all) {
    log.debug("Form All: " + all.getFullSelector());
    super.all(all);
  }
  
}