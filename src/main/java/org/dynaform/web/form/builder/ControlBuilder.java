package org.dynaform.web.form.builder;

import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.form.control.Control;
import org.dynaform.xml.form.data.Data;
import org.dynaform.xml.form.data.DateData;
import org.dynaform.xml.form.restriction.Choice;
import org.dynaform.xml.form.restriction.Restrictions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.DisplayControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * Converts an abstract form into Aranea Widget.
 * 
 * @author Rein Raudjärv
 * 
 * @see FormElement
 * @see AraneaControlEntry
 * @see AraneaFormBuilder
 */
public class ControlBuilder {

  private static final int MAX_LENGTH_LONG  = String.valueOf(Long.MIN_VALUE).length();
  private static final int MAX_LENGTH_INT   = String.valueOf(Integer.MIN_VALUE).length();
  private static final int MAX_LENGTH_SHORT = String.valueOf(Short.MIN_VALUE).length();
  private static final int MAX_LENGTH_BYTE  = String.valueOf(Byte.MIN_VALUE).length();
  
  private static final int SELECT_BOX_MAX_SIZE = 8;
  
  private static final String SELECT_BOX_NULL_VALUE = null;
  private static final String SELECT_BOX_NULL_LABEL = "";

  private static final String RADIO_SELECT_BOX_NULL_VALUE = "__null__";
  private static final String RADIO_SELECT_BOX_NULL_LABEL = "-";
  
  static <E> AraneaControlEntry getFormControl(FormElement<E> element) {
    Class<E> type = element.getType();
    Control control = element.getControl();
    
    if (control instanceof Control.SelectOne)
      return getSelectOneControl(type, control, element.getRestrictions());
    
    if (String.class.equals(type))
      return getStringControl((FormElement<String>) element);
    
    if (Integer.class.equals(type) || Long.class.equals(type)
        || Short.class.equals(type) || Byte.class.equals(type)
        || BigInteger.class.equals(type))
      return getIntegerControl(type, control);
    
    if (Float.class.equals(type) || Double.class.equals(type) || BigDecimal.class.equals(type))
      return getFloatControl(type, control);
    
    if (Date.class.equals(type))
      return getDateControl(element.getData(), type, control);
    
    if (Boolean.class.equals(type))
      return getBooleanControl(type, control);
    
    return null;
  }
  
  private static AraneaControlEntry getIntegerControl(Class<?> type, Control control) {
    final boolean display = control instanceof Control.Output;
    final org.araneaframework.uilib.form.Control arControl = new NumberControl();
    final String tag = display ? "numberInputDisplay" : "numberInput";
    final Map<String, String> tagAttributes = new HashMap<String, String>();
    Class<?> arType = type;
    Converter converter = Converters.NOP;
    Integer size = null;
    
    // Use the size of the Control
    if (control instanceof Control.Text) {
      Control.Text text = (Control.Text) control;
      if (text.getSize() != null) size = text.getSize();
    }

    // or use the default size
    if (!display && size == null) {
      if (Long.class.equals(type))         size = MAX_LENGTH_LONG;
      else if (Integer.class.equals(type)) size = MAX_LENGTH_INT;
      else if (Short.class.equals(type))   size = MAX_LENGTH_SHORT;
      else if (Byte.class.equals(type))    size = MAX_LENGTH_BYTE;
    }

    /*
     * Aranea does not support Short and Byte values.
     * We convert these values into Integers. 
     */
    if (Short.class.equals(type)) {
      arType = Integer.class;
      converter = Converters.SHORT_TO_INTEGER;
    }
    else if (Byte.class.equals(type)) {
      arType = Integer.class;
      converter = Converters.BYTE_TO_INTEGER;
    }
    
    if (size != null) tagAttributes.put("size", "" + size);
    
    return new AraneaControlEntry(arControl, arType, converter, tag, tagAttributes);
  }
  
  private static AraneaControlEntry getFloatControl(Class<?> type, Control control) {
    final org.araneaframework.uilib.form.Control arControl = new FloatControl();
    final String tag = "floatInput";
    final Map<String, String> tagAttributes = new HashMap<String, String>();
    final Converter converter = Converters.NOP;
    final Class<?> arType = type;
    Integer size = null;
    
    // Use the size of the Control
    if (control instanceof Control.Text) {
      Control.Text text = (Control.Text) control;
      if (text.getSize() != null) size = text.getSize();
    }
    
    if (size != null) tagAttributes.put("size", "" + size);
    
    return new AraneaControlEntry(arControl, arType, converter, tag, tagAttributes);
  }
  
  private static AraneaControlEntry getDateControl(Data<?> data, Class<?> type, Control control) {
    final Map<String, String> tagAttributes = new HashMap<String, String>();
    final Converter converter = Converters.NOP;
    final Class<?> arType = type;
    final org.araneaframework.uilib.form.Control arControl;
    String tag;
    
    if (data instanceof DateData._Date) {
      arControl = new DateControl();
      tag = "dateInput";
    }
    else if (data instanceof DateData._Time) {
      arControl = new TimeControl();
      tag = "timeInput";
    }
    else if (data instanceof DateData._DateTime) {
      arControl = new DateTimeControl();
      tag = "dateTimeInput";
    }
    else
      throw new IllegalArgumentException("Illegal data: " + data);
    
    if (control instanceof Control.Output)
      tag += "Display";
    
    return new AraneaControlEntry(arControl, arType, converter, tag, tagAttributes);
  }
  
  private static AraneaControlEntry getBooleanControl(Class<?> type, Control control) {
    final org.araneaframework.uilib.form.Control arControl = new CheckboxControl();
    final String tag = control instanceof Control.Output ? "checkboxDisplay" : "checkbox";
    final Map<String, String> tagAttributes = new HashMap<String, String>();
    final Converter converter = Converters.NOP;
    final Class<?> arType = type;
    
    return new AraneaControlEntry(arControl, arType, converter, tag, tagAttributes);
  }
  
  private static AraneaControlEntry getStringControl(FormElement<String> element) {
    final Control control = element.getControl();
    
    final Class<?> arType = String.class;
    final Map<String, String> tagAttributes = new HashMap<String, String>();
    final Converter converter = Converters.NOP;
    org.araneaframework.uilib.form.Control arControl;
    String tag;
    
    if (element.isReadOnly()) {
      arControl = new DisplayControl();
      tag = "textDisplay";
    }
    else if (control instanceof Control.Secret) {
      Control.Secret secret = (Control.Secret) control;
      arControl = new TextControl();
      tag = "passwordInput";
      if (secret.getSize() != null) tagAttributes.put("size", "" + secret.getSize());
    }
    else if (control instanceof Control.Text) {
      arControl = new TextControl();
      Control.Text text = (Control.Text) control;
      tag = "textInput";
      if (text.getSize() != null) tagAttributes.put("size", "" + text.getSize());
    }
    else if (control instanceof Control.TextArea) {
      arControl = new TextareaControl();
      Control.TextArea textarea = (Control.TextArea) control;
      if (control instanceof Control.RichTextArea)
        tag = "richTextarea";
      else
        tag = "textarea";
      if (textarea.getCols() != null) tagAttributes.put("cols", "" + textarea.getCols());
      if (textarea.getRows() != null) tagAttributes.put("rows", "" + textarea.getRows());
    }
    else if (control instanceof Control.Input) {
      arControl = new TextControl();
      tag = "textInput";
    }
    else
      return null;
    
    return new AraneaControlEntry(arControl, arType, converter, tag, tagAttributes);
  }
  
  private static <E> AraneaControlEntry getSelectOneControl(Class<E> type, Control control, Restrictions<E> restrictions) {
    final SelectControl arControl = new SelectControl();
    final Map<String, String> tagAttributes = new HashMap<String, String>();
    final String tag;
    final Class<?> arType = type;
    Converter converter = Converters.NOP;
    
    if (control instanceof Control.RadioSelectBox) {
      tag = "radioSelect";
      tagAttributes.put("type", "vertical");
      arControl.addItem(new DisplayItem(RADIO_SELECT_BOX_NULL_VALUE, RADIO_SELECT_BOX_NULL_LABEL));
      converter = new Converter() {
        public Object convert(Object value) {
          return value == null ? RADIO_SELECT_BOX_NULL_VALUE : value;
        }
        public Object deconvert(Object value) {
          return RADIO_SELECT_BOX_NULL_VALUE.equals(value) ? null : value;
        }
      };
    }
    else {
      tag = "select";
      arControl.addItem(new DisplayItem(SELECT_BOX_NULL_VALUE, SELECT_BOX_NULL_LABEL));
      
      if (control instanceof Control.SelectBox) {
        Control.SelectBox selectbox = (Control.SelectBox) control;
        Integer size = selectbox.getSize();
        if (size == null) size = Math.min(arControl.getDisplayItems().size(), SELECT_BOX_MAX_SIZE);
        tagAttributes.put("size", "" + size);
      }
    }
    
    List<Choice<E>> choices = restrictions.getChoices();
    for (Choice<?> choice : choices)
      arControl.addItem(new DisplayItem(choice.getLabel(), choice.getLabel()));
    
    return new AraneaControlEntry(arControl, arType, converter, tag, tagAttributes);
  }
  
}
