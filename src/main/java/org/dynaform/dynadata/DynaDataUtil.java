package org.dynaform.dynadata;

import org.dynaform.dynadata.selector.Selector;
import org.dynaform.dynadata.selector.SelectorFactory;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Rein Raudjärv
 */
public class DynaDataUtil {

  private static final Log log = LogFactory.getLog(DynaDataUtil.class);
  
  public static void write(PrintWriter out, MappingCollection mappings) {
    for (Mapping mapping : mappings.getAll()) {
      String selector = mapping.getSelector().getString();
      String params = mapping.getAttributes().getParams();
      
      out.print(selector);
      out.print(" {");
      out.print(params);
      out.println(" }");
    }
    out.println();
  }
  
  public static void write(PrintWriter out, Map<String, DynaDataAttributes> metadatas) {
    for (Entry<String, DynaDataAttributes> entry : metadatas.entrySet()) {
      String selector = entry.getKey();
      DynaDataAttributes metadata = entry.getValue();
      
      out.print(selector);
      out.print(" {");
      out.print(metadata.getParams());
      out.println(" }");
    }
    out.println();
  }
  
  public static void parseLine(String line, MappingCollection mappings) {
    int i = line.indexOf(" {");
    int j = line.indexOf("}");
    if (i == -1 || j == -1)
      throw new IllegalArgumentException(line);
    String selectorStr = line.substring(0, i).trim();
    String params = " " + line.substring(i + 2, j).trim();
    
    if (log.isDebugEnabled())
      log.debug("Selector: '" + selectorStr + "' params: '" + params + "'");
    
    Selector selector = SelectorFactory.newInstance(selectorStr);
    DynaDataAttributes attrs = DynaDataAttributes.newInstance(params);
    mappings.add(new Mapping(selector, attrs));
  }
  
}
