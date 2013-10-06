package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.form.FormChoice;
import org.dynaform.xml.form.ToggleFormVisitor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Rein Raudjärv
 * 
 * @see FormChoice
 * @see XmlReader 
 */
public class ChoiceReader implements XmlReader {

  private static final Log log = LogFactory.getLog(ChoiceReader.class);

  private final FormChoice form;
  private final List<XmlReader> children;

  public ChoiceReader(FormChoice form, List<XmlReader> children) {
    this.form = form;
    this.children = Collections.unmodifiableList(children);
  }
  
  public XmlVisitor create(final XmlReader next) {
    XmlReader result = null;
    int index = 0;
    for (Iterator<XmlReader> it = children.iterator(); it.hasNext();) {
      XmlReader reader = it.next();

      // Set this choice selectedChild if reading of it succeeds
      reader = new AfterReader(reader, new EnableHandler(index));

      // Continue with the next reader it reading of this choice succeeds 
      reader = SequenceReader.newInstance(reader, next);

      if (result == null)
        result = reader;
      else {
        // Disable the choice if reading of it fails
        result = new BeforeReader(result, new DisableHandler(index));

        // Try to read this choice
        result = new MaybeReader(reader, result);
      }

      index++;
    }

    if (log.isDebugEnabled()) {
      log.debug("Starting to read XML: " + result);
      log.debug("Next reader: " + next);
    }

    return result.create(null);
  }

  private class EnableHandler implements Runnable {
    private final int index;
    public EnableHandler(int index) {
      this.index = index;
    }
    public void run() {
      form.setSelectedIndex(index);
    }
    public String toString() {
      return EnableHandler.class.getSimpleName() + "(" + index + ") '" + form.getFullSelector() + "'";
    }
  }
  
  private class DisableHandler implements Runnable {
    private final int index;
    public DisableHandler(int index) {
      this.index = index;
    }
    public void run() {
      form.getChildren().get(index).accept(ToggleFormVisitor.DISABLER);
    }
    public String toString() {
      return DisableHandler.class.getSimpleName() + "(" + index + ") '" + form.getFullSelector() + "'";
    }
  }

}