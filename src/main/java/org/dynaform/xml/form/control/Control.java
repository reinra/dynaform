package org.dynaform.xml.form.control;


/**
 * @author Rein Raudjärv
 * 
 * @šee Controls
 */
public interface Control {
  
  /** A general input control */
  interface Input extends Control {}
  
  /** A single line text input. */
  interface Text extends Input {
    // Specifies the width (in characters)
    Integer getSize();
    void setSize(Integer size);
    // Specifies the maximum length (in characters)
    Integer getMaxLength();
    void setMaxLength(Integer maxLength);
  }
  
  /** Like "text", but the input text is rendered in such a way as to hide the characters. */
  interface Secret extends Text {}
  
  /** A multi-line text input. */
  interface TextArea extends Control {
    // Specifies the visible width
    Integer getCols();
    void setCols(Integer cols);
    // Specifies the visible number of rows
    Integer getRows();
    void setRows(Integer rows);
  }
  
  /** A rich multi-line text input. */
  interface RichTextArea extends TextArea {}
  
  /** Renders a value but provides no means for entering or changing data.  */
  interface Output extends Control {}
  
  /** Enables the common feature found on Web sites to upload a file from the local file system. */
  interface Upload extends Control {
    // Specifies the width (in characters)
    Integer getSize();
    void setSize(Integer size);
  }
  
  /** Allows the user to make multiple selections from a set of choices. */
  interface Select extends Control {}
  
  interface MultiSelectBox extends Select {
    // Specifies the visible number of rows
    Integer getSize();
    void setSize(Integer size);
  }
  
  /** Allows the user to make a single selection from a set of choices. */
  interface SelectOne extends Control {}

  interface SelectBox extends SelectOne {
    // Specifies the visible number of rows
    Integer getSize();
    void setSize(Integer size);
  }
  
  interface ComboBox extends SelectOne {}
  
  interface RadioSelectBox extends SelectOne {}

}
