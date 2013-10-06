package org.dynaform.dynadata;

import java.io.Reader;
import java.io.Writer;

/**
 * DynaData of a Form.
 * 
 * @author Rein Raudjärv
 */
public interface DynaData {

  /**
   * Read and apply new data.
   * 
   * @param in reader.
   */
  void read(Reader in);

  /**
   * Writes current data.
   * <p>
   * This includes data read plus
   * generated data about new form elements.
   * 
   * @param out writer.
   */
  void write(Writer out);
  
}
