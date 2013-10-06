package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Rein Raudjärv
 * 
 * @see XmlReader
 */
public class SequenceReader implements XmlReader {
  
  public static XmlReader newInstance(XmlReader reader1, XmlReader reader2) {
    return newInstance(Arrays.asList(new XmlReader[] { reader1, reader2 }));
  }
  
  public static XmlReader newInstance(Collection<XmlReader> children) {
    if (children == null || children.isEmpty())
      return null;
    
    if (children.size() == 1)
      return children.iterator().next();
    
    List<XmlReader> items = new ArrayList<XmlReader>();
    for (Iterator<XmlReader> it = children.iterator(); it.hasNext();) {
      XmlReader reader = it.next();
      if (reader instanceof SequenceReader)
        items.addAll(((SequenceReader) reader).children);
      else
        items.add(reader);
    }
    return new SequenceReader(items);
  }
  
  private final List<XmlReader> children;
  
  private SequenceReader(List<XmlReader> children) {
    this.children = children;
  }

  public XmlVisitor create(XmlReader next) {
    XmlReader head = children.get(0);
    List<XmlReader> tail = children.subList(1, children.size());
    if (next != null) {
      tail = new ArrayList<XmlReader>(tail);
      tail.add(next);
    }
    return head.create(newInstance(tail));
  }
  
  public String toString() {
    return children.toString();
  }

}
