package org.dynaform.xml.reader;

import org.dynaform.xml.RepeatAdapter;
import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormRepeat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Rein Raudjärv
 * 
 * @see FormRepeat
 * @see RepeatAdapter
 * @see XmlReader
 */
public class RepeatReader<F extends Form> implements XmlReader {

  private static final Log log = LogFactory.getLog(RepeatReader.class);

  private final FormRepeat<F> form;
  private final RepeatAdapter<F> adapter;

  public RepeatReader(FormRepeat<F> form, RepeatAdapter<F> adapter) {
    this.form = form;
    this.adapter = adapter;
  }

  public XmlVisitor create(XmlReader next) {
    if (log.isInfoEnabled())
      log.info("Started sequence " + form);

    // Remove current elements
    form.clear();

    return new RepeatReaderContext(next).createItemReader().create(null);
  }

  // XmlReader for next element

  private class RepeatReaderContext {

    private final XmlReader afterRepeatReader;

    public RepeatReaderContext(XmlReader afterRepeatReader) {
      this.afterRepeatReader = afterRepeatReader;
    }

    private XmlReader createItemReader() {
      ItemReader reader = new ItemReader();
      return new MaybeReader(reader, reader.new FinishReader());
    }

    private class ItemReader implements XmlReader {

      private F formChild;

      public XmlVisitor create(XmlReader next) {
        formChild = form.add();

        if (log.isInfoEnabled())
          log.info("Created next item for sequence " + form + " (" + formChild + ")");

        XmlReader reader = adapter.getChildren().get(formChild).getReader();

        return reader.create(createItemReader());
      }

      public String toString() {
        if (formChild == null)
          return "RepeatReader {" + form + "}";

        return "SequenceItemReader {" + adapter.getChildren().get(formChild).getReader() + "}";
      }

      private class FinishReader implements XmlReader {

        public XmlVisitor create(XmlReader next) {
          // Remove last item
          form.remove(formChild);
          log.info("Removed last child of " + form + " (" + formChild + ")");

          int size = form.getChildren().size();
          Integer min = form.getMin();
          if (min != null && size < min.intValue()) {
            // Add missing items
            log.info("Add " + (min - size + 1) + " empty items");

            for (int i = size; i < min.intValue(); i++)
              form.add();

            throw new InvalidXmlException("At least " + min
                + " items expected (" + size + " found)");
          }

          return afterRepeatReader.create(next);
        }

        public String toString() {
          return afterRepeatReader == null ? null : afterRepeatReader
              .toString();
        }

      }

    }

  }
  
  // toString

  private String description;
  
  public synchronized String toString() {
    if (description == null)
      description = createDescription();

    return description;
  }

  public String createDescription() {
    return RepeatReader.class.getSimpleName() + "(" + getItemReaderToString()
    + ")";
  }

  private String getItemReaderToString() {
    String result;
    F childForm = form.add();
    try {
      XmlReader reader = adapter.getChildren().get(childForm).getReader();
      result = reader.toString();
    } finally {
      form.remove(childForm);
    }
    return result;
  }  

}