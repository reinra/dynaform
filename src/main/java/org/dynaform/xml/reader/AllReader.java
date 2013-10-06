package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.form.FormAll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Rein Raudjärv
 * 
 * @see FormAll
 */
public class AllReader implements XmlReader {

  private static final Log log = LogFactory.getLog(AllReader.class);
  
  private final FormAll form;
  private final List<XmlReader> children;
  
  public AllReader(FormAll form, List<XmlReader> children) {
    this.form = form;
    this.children = Collections.unmodifiableList(children);
  }
  
  public XmlVisitor create(XmlReader next) {
    log.info("Started all " + form);

    // Clear
    form.setSelectedIndexes(new int[0]);

    // Start reading
    return new AllReaderContext(next).new ItemReader(form.getIndexes()).create(null);
  }
  
  private class AllReaderContext {
    
    private final XmlReader afterAllReader;

    public AllReaderContext(XmlReader afterAllReader) {
      this.afterAllReader = afterAllReader;
    }
    
    public class ItemReader implements XmlReader {
      
      // Children not yet read
      private final int[] unselectedIndexes;
      
      public ItemReader(int[] unselectedIndexes) {
        this.unselectedIndexes = unselectedIndexes;
      }
      
      public XmlVisitor create(XmlReader next) {
        XmlReader result = form.areAllChildrenRequired() ? null : afterAllReader;
        
        for (int index : unselectedIndexes) {
          XmlReader reader = children.get(index);
          
          // Set this child active if reading of it succeeds
          reader = new AfterReader(reader, new EnableHandler(index));
          
          // Create reader in case reading the given child succeeds 
          XmlReader follows = unselectedIndexes.length == 1 ? afterAllReader : createNextReader(index);
          
          // Continue with the next reader it reading of this child succeeds 
          reader = SequenceReader.newInstance(reader, follows);
          
          if (result == null)
            result = reader;
          else {
            // Try to read this choice
            result = new MaybeReader(reader, result);
          }
        }
        
        return result.create(null);
      }
      
      private XmlReader createNextReader(int removeIndex) {
        return new ItemReader(ArrayUtils.removeElement(unselectedIndexes, removeIndex));
      }
      
      public String toString() {
        return ItemReader.class.getSimpleName() + " " + getReaders();
      }
      
      private List<XmlReader> getReaders() {
        List<XmlReader> result = new ArrayList<XmlReader>();
        for (int index : unselectedIndexes)
          result.add(children.get(index));
        return result;
      }
      
    }
    
  }
  private class EnableHandler implements Runnable {
    
    private final int index;
    
    public EnableHandler(int index) {
      this.index = index;
    }
    
    public void run() {
      int[] indexes = form.getSelectedIndexes();
      int[] newIndexes = ArrayUtils.add(indexes, index);
      form.setSelectedIndexes(newIndexes);
    }
    
    public String toString() {
      return EnableHandler.class.getSimpleName() + "(" + index + ") '" + form.getFullSelector() + "'";
    }
  }

}
