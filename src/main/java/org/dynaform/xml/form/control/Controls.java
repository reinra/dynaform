package org.dynaform.xml.form.control;

import org.dynaform.xml.form.control.Control.ComboBox;
import org.dynaform.xml.form.control.Control.Input;
import org.dynaform.xml.form.control.Control.MultiSelectBox;
import org.dynaform.xml.form.control.Control.Output;
import org.dynaform.xml.form.control.Control.RadioSelectBox;
import org.dynaform.xml.form.control.Control.RichTextArea;
import org.dynaform.xml.form.control.Control.Secret;
import org.dynaform.xml.form.control.Control.Select;
import org.dynaform.xml.form.control.Control.SelectBox;
import org.dynaform.xml.form.control.Control.SelectOne;
import org.dynaform.xml.form.control.Control.Text;
import org.dynaform.xml.form.control.Control.TextArea;
import org.dynaform.xml.form.control.Control.Upload;

/**
 * @author Rein Raudjärv
 * 
 * @see Control
 */
public abstract class Controls {

  public static class InputImpl implements Input {}
  
  public static class TextImpl extends InputImpl implements Text {
    private Integer size;
    private Integer maxLength;
    public Integer getSize() {
      return size;
    }
    public void setSize(Integer size) {
      this.size = size;
    }
    public Integer getMaxLength() {
      return maxLength;
    }
    public void setMaxLength(Integer maxLength) {
      this.maxLength = maxLength;
    }
  }
  
  public static class SecretImpl extends TextImpl implements Secret {}
  
  public static class TextAreaImpl implements TextArea {
    private Integer cols;
    private Integer rows;
    public Integer getCols() {
      return cols;
    }
    public void setCols(Integer cols) {
      this.cols = cols;
    }
    public Integer getRows() {
      return rows;
    }
    public void setRows(Integer rows) {
      this.rows = rows;
    }
  }
  
  public static class RichTextAreaImpl extends TextAreaImpl implements RichTextArea {}
  
  public static class OutputImpl implements Output {}
  
  public static class UploadImpl implements Upload {
    private Integer size;
    public Integer getSize() {
      return size;
    }
    public void setSize(Integer size) {
      this.size = size;
    }
  }
  
  public static class SelectImpl implements Select {}
  
  public static class MultiSelectBoxImpl extends SelectImpl implements MultiSelectBox {
    private Integer size;
    public Integer getSize() {
      return size;
    }
    public void setSize(Integer size) {
      this.size = size;
    }
  }
  
  public static class SelectOneImpl implements SelectOne {}
  
  public static class SelectBoxImpl extends SelectOneImpl implements SelectBox {
    private Integer size;
    public Integer getSize() {
      return size;
    }
    public void setSize(Integer size) {
      this.size = size;
    }
  }
  
  public static class ComboBoxImpl extends SelectOneImpl implements ComboBox {}
  
  public static class RadioSelectBoxImpl extends SelectOneImpl implements RadioSelectBox {}
  
}
