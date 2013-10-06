package org.dynaform.dynadata;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormAll;
import org.dynaform.xml.form.FormChoice;
import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.form.FormFunction;
import org.dynaform.xml.form.FormRepeat;
import org.dynaform.xml.form.FormSection;
import org.dynaform.xml.form.FormSequence;
import org.dynaform.xml.form.FormVisitor;
import org.dynaform.xml.form.control.Control;
import org.dynaform.xml.form.control.Controls.ComboBoxImpl;
import org.dynaform.xml.form.control.Controls.InputImpl;
import org.dynaform.xml.form.control.Controls.OutputImpl;
import org.dynaform.xml.form.control.Controls.RadioSelectBoxImpl;
import org.dynaform.xml.form.control.Controls.RichTextAreaImpl;
import org.dynaform.xml.form.control.Controls.SecretImpl;
import org.dynaform.xml.form.control.Controls.SelectBoxImpl;
import org.dynaform.xml.form.control.Controls.SelectOneImpl;
import org.dynaform.xml.form.control.Controls.TextAreaImpl;
import org.dynaform.xml.form.control.Controls.TextImpl;
import org.dynaform.xml.form.layout.RepeatStyle;
import org.dynaform.xml.form.layout.SectionStyle;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Rein Raudjärv
 * 
 * @see DynaDataImpl
 */
public class DynaDataAttributes {

  private static final Log log = LogFactory.getLog(DynaDataAttributes.class);

  private static final String LABEL = "label";
  private static final String READ_ONLY = "readonly";
  private static final String REQUIRED = "required";
  
  private static final String CONTROL = "control";
  private static final String SIZE = "size";
  private static final String MAX_LENGTH = "maxlength";
  private static final String COLUMNS = "cols";
  private static final String ROWS = "rows";
  
  private static final String SECTION_STYLE = "section-style";
  private static final String REPEAT_STYLE = "repeat-style";
  
  private final String params;

  private final String label;
  private final Boolean readonly;
  private final Boolean required;
  
  private final Control control;
  private final SectionStyle orientation;
  private final RepeatStyle repeatStyle;

  public DynaDataAttributes(
      String params,
      String label,
      Boolean readonly,
      Boolean required,
      Control control,
      SectionStyle orientation,
      RepeatStyle repeatStyle) {
    this.params = params;
    this.label = label;
    this.readonly = readonly;
    this.required = required;
    this.control = control;
    this.orientation = orientation;
    this.repeatStyle = repeatStyle;
  }
  
  public DynaDataAttributes merge(DynaDataAttributes metadata) {
    String label = metadata.label != null ? metadata.label : this.label;
    Boolean readonly = metadata.readonly != null ? metadata.readonly : this.readonly;
    Boolean required = metadata.required != null ? metadata.required : this.required;
    Control control = metadata.control != null ? metadata.control : this.control;
    SectionStyle orientation = metadata.orientation != null ? metadata.orientation : this.orientation;
    RepeatStyle repeatStyle = metadata.repeatStyle != null ? metadata.repeatStyle : this.repeatStyle;
    
    String params = paramsToString(createParams(label, readonly, required, control, orientation, repeatStyle));
    return new DynaDataAttributes(params, label, readonly, required, control, orientation, repeatStyle);
  }
  
  public DynaDataAttributes intersection(DynaDataAttributes metadata) {
    Map<String, Object> params1 = createParams(label, readonly, required, control, orientation, repeatStyle);
    Map<String, Object> params2 = createParams(metadata.label, metadata.readonly, metadata.required, metadata.control, metadata.orientation, metadata.repeatStyle);
    Map<String, Object> paramsIntersect = new LinkedHashMap<String, Object>();
    for (Entry<String, Object> entry : params1.entrySet()) {
      String key = entry.getKey();
      Object value1 = entry.getValue();
      Object value2 = params2.get(entry.getKey());
      if (value1 != null && value1.equals(value2))
          paramsIntersect.put(key, value1);
    }
    log.debug("Intersection between " + params1 + " and " + params2 + " is " + paramsIntersect);
    return newInstance(paramsToString(paramsIntersect));
  }

  private static Map<String, Object> createParams(
      String label,
      Boolean readonly,
      Boolean required,
      Control control,
      SectionStyle orientation,
      RepeatStyle repeatStyle) {
    
    Map<String, Object> params = new LinkedHashMap<String, Object>();
    params.put(LABEL, label);
    params.put(READ_ONLY, readonly != null ? readonly.booleanValue() : null);
    params.put(REQUIRED, required != null ? required.booleanValue() : null);
    control(control, params);
    if (orientation != null)
      params.put(SECTION_STYLE, orientation.toString().toLowerCase());
    if (repeatStyle != null)
      params.put(REPEAT_STYLE, repeatStyle.toString().toLowerCase());
    return params;
  }

  public String getParams() {
    return params;
  }
  
  public void visit(Form form) {
    form.accept(METADATA_APPLIER);
  }

  public static DynaDataAttributes newInstance(String paramStr) {
    Map<String, String> params = parseParams(paramStr); 
    String label = params.get(LABEL);
    Boolean readonly = params.containsKey(READ_ONLY) ? Boolean.valueOf(params.get(READ_ONLY)) : null;
    Boolean required = params.containsKey(REQUIRED) ? Boolean.valueOf(params.get(REQUIRED)) : null;
    Control control = readControl(params);
    
    SectionStyle orientation = null;
    try {
      if (params.containsKey(SECTION_STYLE))
        orientation = SectionStyle.valueOf(params.get(SECTION_STYLE).toUpperCase());
    } catch (IllegalArgumentException e) {
      log.error(e.getMessage(), e);
    }
    
    RepeatStyle repeatStyle = null;
    try {
      if (params.containsKey(REPEAT_STYLE))
        repeatStyle = RepeatStyle.valueOf(params.get(REPEAT_STYLE).toUpperCase());
    } catch (IllegalArgumentException e) {
      log.error(e.getMessage(), e);
    }
    
    return new DynaDataAttributes(paramStr, label, readonly, required, control, orientation, repeatStyle);
  }
  
  public static DynaDataAttributes newInstance(Form form) {
    return form.apply(FORM_METADATA_READER);
  }

  private static Control readControl(Map<String, String> params) {
    String controlStr = params.get(CONTROL);
    if (controlStr == null)
      return null;

    if (Control.Input.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      return new InputImpl();
    }
    if (Control.Text.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      TextImpl text = new TextImpl();
      if (params.containsKey(SIZE)) text.setSize(Integer.valueOf(params.get(SIZE)));
      if (params.containsKey(MAX_LENGTH)) text.setMaxLength(Integer.valueOf(params.get(MAX_LENGTH)));
      return text;
    }
    if (Control.Secret.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      SecretImpl secret = new SecretImpl();
      if (params.containsKey(SIZE)) secret.setSize(Integer.valueOf(params.get(SIZE)));
      if (params.containsKey(MAX_LENGTH)) secret.setMaxLength(Integer.valueOf(params.get(MAX_LENGTH)));
      return secret;
    }

    if (Control.TextArea.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      TextAreaImpl textarea = new TextAreaImpl();
      if (params.containsKey(COLUMNS)) textarea.setCols(Integer.valueOf(params.get(COLUMNS)));
      if (params.containsKey(ROWS)) textarea.setRows(Integer.valueOf(params.get(ROWS)));
      return textarea;
    }
    if (Control.RichTextArea.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      RichTextAreaImpl richtextarea = new RichTextAreaImpl();
      if (params.containsKey(COLUMNS)) richtextarea.setCols(Integer.valueOf(params.get(COLUMNS)));
      if (params.containsKey(ROWS)) richtextarea.setRows(Integer.valueOf(params.get(ROWS)));
      return richtextarea;
    }
    
    if (Control.Output.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      return new OutputImpl();
    }

    if (Control.SelectOne.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      return new SelectOneImpl();
    }
    if (Control.SelectBox.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      SelectBoxImpl selectbox = new SelectBoxImpl();
      if (params.containsKey(SIZE)) selectbox.setSize(Integer.valueOf(params.get(SIZE)));
      return selectbox;
    }
    if (Control.ComboBox.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      return new ComboBoxImpl();
    }
    if (Control.RadioSelectBox.class.getSimpleName().equalsIgnoreCase(controlStr)) {
      return new RadioSelectBoxImpl();
    }

    return null;
  }

  // Helper methods

  private static Map<String, String> parseParams(String params) {
    Map<String, String> result = new LinkedHashMap<String, String>();
    StringTokenizer st = new StringTokenizer(params, ";");
    while (st.hasMoreTokens()) {
      String param = st.nextToken().trim();
      if (param.isEmpty())
        break;
      int k = param.indexOf(":");
      if (k == -1)
        throw new IllegalArgumentException(param);
      String key = param.substring(0, k).trim();
      String value = param.substring(k + 1).trim();
      if (value.length() > 2 && value.charAt(0) == '\"' && value.charAt(value.length() - 1) == '\"')
        value = value.substring(1, value.length() - 1);
      log.info(key + ":" + value);
      result.put(key, value);
    }
    return result;
  }

  private static String paramsToString(Map<String, Object> attrs) {
    StringBuilder sb = new StringBuilder();

    // Remove null values
    for (Iterator<Object> it = attrs.values().iterator(); it.hasNext();) {
      Object value = it.next();
      if (value == null)
        it.remove();
    }

    for (Entry<String, Object> entry: attrs.entrySet()) {
      String name = entry.getKey();
      Object value = entry.getValue();
      boolean escape = LABEL.equals(name);

      sb.append(" ");
      sb.append(name);
      sb.append(": ");
      if (escape) sb.append("\"");
      sb.append(value);
      if (escape) sb.append("\"");
      sb.append(";");
    }

    return sb.toString();
  }
  
  private static FormFunction<DynaDataAttributes> FORM_METADATA_READER = new FormFunction<DynaDataAttributes>() {

    public <E> DynaDataAttributes element(FormElement<E> element) {
      String label = element.getLabel();
      boolean readOnly = element.isReadOnly();
      boolean required = element.isRequired();
      Control control = element.getControl();

      Map<String, Object> attrs = new LinkedHashMap<String, Object>();
      attrs.put(LABEL, label);
      attrs.put(READ_ONLY, readOnly);
      attrs.put(REQUIRED, required);
      control(control, attrs);
      
      return new DynaDataAttributes(paramsToString(attrs), label, readOnly, required, control, null, null);
    }
    
    public <F extends Form> DynaDataAttributes section(FormSection<F> section) {
      String label = section.getLabel();
      SectionStyle orientation = section.getSectionStyle();
      
      Map<String, Object> attrs = new LinkedHashMap<String, Object>();
      attrs.put(LABEL, label);
      attrs.put(SECTION_STYLE, orientation.toString().toLowerCase());
      
      return new DynaDataAttributes(paramsToString(attrs), label, null, null, null, orientation, null);
    }
    
    public <F extends Form> DynaDataAttributes repeat(FormRepeat<F> sequence) {
      RepeatStyle repeatStyle = sequence.getRepeatStyle();
      
      Map<String, Object> attrs = new LinkedHashMap<String, Object>();
      attrs.put(REPEAT_STYLE, repeatStyle.toString().toLowerCase());
      
      return new DynaDataAttributes(paramsToString(attrs), null, null, null, null, null, repeatStyle);
    }
    
    public DynaDataAttributes sequence(FormSequence composite) {
      return null;
    }
    
    public DynaDataAttributes choice(FormChoice choice) {
      return null;
    }

    public DynaDataAttributes all(FormAll all) {
      return null;
    }

  };
  
  private static void control(Control control, Map<String, Object> attrs) {
    if (control instanceof Control.Secret) {
      Control.Secret secret = (Control.Secret) control;
      attrs.put(CONTROL, Control.Secret.class.getSimpleName());
      attrs.put(SIZE, secret.getSize());
      attrs.put(MAX_LENGTH, secret.getMaxLength());
    }
    else if (control instanceof Control.Text) {
      Control.Text text = (Control.Text) control;
      attrs.put(CONTROL, Control.Text.class.getSimpleName());
      attrs.put(SIZE, text.getSize());
      attrs.put(MAX_LENGTH, text.getMaxLength());
    }
    else if (control instanceof Control.Input) {
      attrs.put(CONTROL, Control.Input.class.getSimpleName());
    }
    
    else if (control instanceof Control.RichTextArea) {
      Control.RichTextArea richtextarea = (Control.RichTextArea) control;
      attrs.put(CONTROL, Control.RichTextArea.class.getSimpleName());
      attrs.put(COLUMNS, richtextarea.getCols());
      attrs.put(ROWS, richtextarea.getRows());
    }
    else if (control instanceof Control.TextArea) {
      Control.TextArea textarea = (Control.TextArea) control;
      attrs.put(CONTROL, Control.TextArea.class.getSimpleName());
      attrs.put(COLUMNS, textarea.getCols());
      attrs.put(ROWS, textarea.getRows());
    }
    
    else if (control instanceof Control.SelectBox) {
      Control.SelectBox selectbox = (Control.SelectBox) control;
      attrs.put(CONTROL, Control.SelectBox.class.getSimpleName());
      attrs.put(SIZE, selectbox.getSize());
    }
    else if (control instanceof Control.ComboBox) {
      attrs.put(CONTROL, Control.ComboBox.class.getSimpleName());
    }
    else if (control instanceof Control.RadioSelectBox) {
      attrs.put(CONTROL, Control.RadioSelectBox.class.getSimpleName());
    }
    else if (control instanceof Control.SelectOne) {
      attrs.put(CONTROL, Control.SelectOne.class.getSimpleName());
    }
  }
  
  private final FormVisitor METADATA_APPLIER = new FormVisitor() {
    
    public <E> void element(FormElement<E> element) {
      if (label != null)
        element.setLabel(label);
      if (readonly != null)
        element.setReadOnly(readonly.booleanValue());
      if (required != null)
        element.setRequired(required.booleanValue());
      if (control != null)
        element.setControl(control);
    }

    public <F extends Form> void section(FormSection<F> section) {
      if (label != null)
        section.setLabel(label);
      if (orientation != null)
        section.setSectionStyle(orientation);
    }
    
    public <F extends Form> void repeat(FormRepeat<F> sequence) {
      if (repeatStyle != null)
        sequence.setRepeatStyle(repeatStyle);
    }
    
    public void sequence(FormSequence composite) {
    }

    public void choice(FormChoice choice) {
    }

    public void all(FormAll all) {
    }

  };

}
