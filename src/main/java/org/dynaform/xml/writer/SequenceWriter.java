package org.dynaform.xml.writer;

import org.dynaform.xml.XmlVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Rein Raudjärv
 * 
 * @see XmlWriter
 */
public class SequenceWriter implements XmlWriter {

	private final Collection<XmlWriter> children;
	
	public static XmlWriter newInstance(Collection<XmlWriter> writers) {
		if (writers == null || writers.isEmpty())
			return null;
		
		if (writers.size() == 1)
			return writers.iterator().next();
		
		List<XmlWriter> items = new ArrayList<XmlWriter>();
		for (Iterator<XmlWriter> it = writers.iterator(); it.hasNext();) {
      XmlWriter writer = it.next();
      if (writer instanceof SequenceWriter)
        items.addAll(((SequenceWriter) writer).children);
      else
        items.add(writer);
    }
		return new SequenceWriter(items); 
	}
	
	private SequenceWriter(Collection<XmlWriter> children) {
		this.children = children;
	}
	
	public boolean isEmpty() {
		return false;
	}

	public void write(XmlVisitor xa) {
		for (XmlWriter child : children)
			child.write(xa);
	}
	
	public String toString() {
	  return SequenceWriter.class.getSimpleName() + " " + children;
	}

}
