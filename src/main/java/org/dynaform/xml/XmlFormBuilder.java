package org.dynaform.xml;

import org.dynaform.xml.form.ElementCounter;
import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormAll;
import org.dynaform.xml.form.FormChoice;
import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.form.FormRepeat;
import org.dynaform.xml.form.FormSequence;
import org.dynaform.xml.form.control.Control;
import org.dynaform.xml.form.control.Controls;
import org.dynaform.xml.form.data.Data;
import org.dynaform.xml.form.data.DataUtil;
import org.dynaform.xml.form.data.StringData;
import org.dynaform.xml.form.impl.FormAllImpl;
import org.dynaform.xml.form.impl.FormChoiceImpl;
import org.dynaform.xml.form.impl.FormElementImpl;
import org.dynaform.xml.form.impl.FormRepeatImpl;
import org.dynaform.xml.form.impl.FormSectionImpl;
import org.dynaform.xml.form.impl.FormSequenceImpl;
import org.dynaform.xml.form.impl.restriction.ChoiceImpl;
import org.dynaform.xml.form.label.SequenceHeaderFactory;
import org.dynaform.xml.form.restriction.Restrictions;
import org.dynaform.xml.reader.AllReader;
import org.dynaform.xml.reader.ChoiceReader;
import org.dynaform.xml.reader.RepeatReader;
import org.dynaform.xml.reader.SequenceReader;
import org.dynaform.xml.reader.WhitespaceReader;
import org.dynaform.xml.reader.XmlElementReader;
import org.dynaform.xml.reader.XmlReader;
import org.dynaform.xml.reader.text.FormElementHandler;
import org.dynaform.xml.reader.text.TextHandler;
import org.dynaform.xml.reader.text.TextXmlReader;
import org.dynaform.xml.writer.AllWriter;
import org.dynaform.xml.writer.ChoiceWriter;
import org.dynaform.xml.writer.FormElementWriter;
import org.dynaform.xml.writer.RepeatWriter;
import org.dynaform.xml.writer.SequenceWriter;
import org.dynaform.xml.writer.TextWriter;
import org.dynaform.xml.writer.TextXmlWriter;
import org.dynaform.xml.writer.XmlElementWriter;
import org.dynaform.xml.writer.XmlWriter;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.XSXPath;
import com.sun.xml.xsom.XmlString;
import com.sun.xml.xsom.XSModelGroup.Compositor;
import com.sun.xml.xsom.visitor.XSFunction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Converts XML Schema into XmlForm.
 * 
 * @author Rein Raudjärv
 * 
 * @see XSSchemaSet
 * @see XSFunction
 * @see XmlForm
 */
public class XmlFormBuilder implements XSFunction<XmlForm> {

  private static final Log log = LogFactory.getLog(XmlFormBuilder.class);
  
  private final static boolean ignoreWhitespace = true;
  
  public static XmlForm build(XSSchemaSet set) {
    return build(set, null);
  }
  
  public static XmlForm build(XSSchemaSet set, String element) {
    try {
      log.debug("\n\n\n-------- BUILDING XML FORM --------");
      
      XmlFormBuilder builder = new XmlFormBuilder();
      Map<String, XmlForm> xmlForms = builder.parse(set);
      XmlForm result = element == null ? getMainForm(xmlForms) : xmlForms.get(element);
      
      log.debug("\n-----------------------------------\n\n");
      
      return result;
    } catch (Exception e) {
      throw new RuntimeException("Failed to convert schema '" + set + "' into an XML form", e);
    }
  }

  private XmlFormBuilder() {
  }
  
  private Map<String, XmlForm> parse(XSSchemaSet set) {
    Map<String, XmlForm> result = new HashMap<String, XmlForm>();
    
    /*
     * Accepts all element declarations in the Schema set
     */
    for (Iterator<XSElementDecl> elements = set.iterateElementDecls(); elements.hasNext();) {
      XSElementDecl decl = elements.next();
      XmlForm xmlForm = decl.apply(this);
      if (xmlForm != null)
        result.put(decl.getName(), xmlForm);
    }
    return result;
  }
  
  private static XmlForm getMainForm(Map<String, XmlForm> xmlForms) {
    if (xmlForms.isEmpty())
      return null;
    if (xmlForms.size() == 1)
      return xmlForms.values().iterator().next();
    
    Map<Integer, XmlForm> sort = new TreeMap<Integer, XmlForm>(Collections.reverseOrder());
    for (Map.Entry<String,XmlForm> entry : xmlForms.entrySet()) {
      String name = entry.getKey();
      XmlForm xmlForm = entry.getValue(); 
      int count = ElementCounter.count(xmlForm.getForm());
      if (log.isDebugEnabled())
        log.debug("Root element '" + name + "' cotains " + count + " elements");
      sort.put(count, xmlForm);
    }
    return sort.values().iterator().next();
  }
  
  // #################### Element Declaration ####################

  public XmlForm elementDecl(XSElementDecl decl) {
    if (log.isDebugEnabled())
      log.debug("Element Declaration: " + decl);
      
    XSType type = decl.getType();
    
    if (type.isSimpleType())
      return elementDeclSimple(decl, type.asSimpleType());
    else
      return elementDeclComplex(decl, type.asComplexType());
  }

  private XmlForm elementDeclSimple(XSElementDecl decl, XSSimpleType simpleType) {
    FormElement element = declSimple(simpleType, decl.getName(), decl.getDefaultValue(), decl.getFixedValue());
    if (element == null)
      return null;
    
    XmlWriter writer = new XmlElementWriter(decl.getName(), null,
        new TextXmlWriter(new FormElementWriter(element)));
    
    XmlReader reader = new XmlElementReader(decl.getName(), null,
        new TextXmlReader(new FormElementHandler(element)));
    
    if (ignoreWhitespace)
      reader = WhitespaceReader.appendTo(reader);

    return new XmlFormImpl(element, writer, reader);
  }
  
  private FormElement declSimple(XSSimpleType simpleType,
      String name,
      XmlString defaultValue,
      XmlString fixedValue) {
    
    if (log.isDebugEnabled()) {
      log.debug("Simple Type: " + simpleType);
      log.debug("Primitive: " + simpleType.isPrimitive());
    }
    
    Data data = null;
    Control control = null;
    boolean required = false;
    
    if (simpleType.isRestriction()) {
      XSRestrictionSimpleType restriction = simpleType.asRestriction();
      
      if (isEnumeration(restriction)) {
        control = new Controls.SelectOneImpl();
        if (log.isDebugEnabled())
          log.debug("Control: " + control);
      }
      
      required = hasMinLength(restriction);
      
      data = createData(simpleType);
      if (data == null) {
        log.warn("Unsupported Primitive Type: " + simpleType);
        return null;
      }
    }
    
    else if (simpleType.isList()) {
      XSListSimpleType list = simpleType.asList();
      XSSimpleType itemType = list.getItemType();
      
      if (log.isDebugEnabled()) {
        log.debug("List: " + list);
        log.debug("Item type: " + itemType);
      }
      
      data = new StringData();
    }
    
    else if (simpleType.isUnion()) {
      log.warn("Unsupported Simple Type: " + simpleType);
      return null;
    }
    
    else
      throw new AssertionError("Unknown Simple Type: " + simpleType);
    
    if (control == null)
      control = new Controls.InputImpl();
    
    String value = null;
    boolean readOnly = false;
    
    if (defaultValue != null) {
      if (log.isDebugEnabled())
        log.debug("Default Value: " + defaultValue);
      
      value = defaultValue.toString();
    }
    
    if (fixedValue != null) {
      if (log.isDebugEnabled())
        log.debug("Fixed Value: " + fixedValue);
      
      value = fixedValue.toString();
      readOnly = true;
    }
    
    FormElement element = new FormElementImpl(name, control, data);
    
    if (value != null) {
      try {
        element.setXmlValue(value);
      } catch (Exception e) {
        log.warn("Failed to inject XML value: " + value, e);
      }
    }
    
    if (simpleType.isRestriction())
      processRestrictions(simpleType.asRestriction(), element.getRestrictions());
    
    element.setReadOnly(readOnly);
    element.setRequired(required);
    
    return element;
  }
  
  private static Data createData(XSSimpleType type) {
    Data data = DataUtil.newData(type.getName());
    if (data != null)
      return data;
    
    XSSimpleType baseType = type.getSimpleBaseType();
    if (baseType == null)
      return null; // This is xs:anySimpleType
    
    if (log.isDebugEnabled())
      log.debug("Base Type: " + baseType);
    
    return createData(baseType);
  }
  
  private static Object readXmlValue(XSSimpleType type, String value) {
    Data data = createData(type);
    try {
      data.setXmlValue(value);
    } catch (Exception e) {
      log.error("Could not convert value '" + value + "'");
      return null;
    }
    return data.getValue();
  }
  
  private static boolean isEnumeration(XSRestrictionSimpleType xsRrestriction) {
    Iterator<XSFacet> facets = xsRrestriction.iterateDeclaredFacets();
    while (facets.hasNext())
      if (XSFacet.FACET_ENUMERATION.equals(facets.next().getName()))
        return true;
    return false;
  }
  
  private static boolean hasMinLength(XSRestrictionSimpleType xsRrestriction) {
    Iterator<XSFacet> facets = xsRrestriction.iterateDeclaredFacets();
    while (facets.hasNext()) {
      XSFacet facet = facets.next();
      String name = facet.getName();
      if ((XSFacet.FACET_LENGTH.equals(name) || XSFacet.FACET_MINLENGTH.equals(name))
          && Integer.parseInt(facet.getValue().toString()) > 0)
        return true;
    }
    return false;
  }
  
  private static void processRestrictions(XSRestrictionSimpleType xsRrestriction, Restrictions restrictions) {
    if (log.isDebugEnabled())
      log.debug("Restriction: " + xsRrestriction);

    Iterator<XSFacet> facets = xsRrestriction.iterateDeclaredFacets();
    while (facets.hasNext()) {
      XSFacet facet = facets.next();

      if (log.isDebugEnabled())
        log.debug("Facet: " + facet + " " + facet.getValue() + " Fixed: " + facet.isFixed());

      String facetName = facet.getName();
      String facetValue = facet.getValue().toString();
      
      // Enumeration
      
      if (XSFacet.FACET_ENUMERATION.equals(facetName))
        restrictions.getChoices().add(new ChoiceImpl(facetValue));
      
      // Length
      
      else if (XSFacet.FACET_LENGTH.equals(facetName))
        restrictions.setLength(Integer.parseInt(facetValue));
      else if (XSFacet.FACET_MINLENGTH.equals(facetName))
        restrictions.setMinLength(Integer.parseInt(facetValue));
      else if (XSFacet.FACET_MAXLENGTH.equals(facetName))
        restrictions.setMaxLength(Integer.parseInt(facetValue));
      
      // Number Range
      
      else if (XSFacet.FACET_MININCLUSIVE.equals(facetName))
        restrictions.setMinInclusive(readXmlValue(xsRrestriction, facetValue));
      else if (XSFacet.FACET_MINEXCLUSIVE.equals(facetName))
        restrictions.setMinExclusive(readXmlValue(xsRrestriction, facetValue));
      else if (XSFacet.FACET_MAXINCLUSIVE.equals(facetName))
        restrictions.setMaxInclusive(readXmlValue(xsRrestriction, facetValue));
      else if (XSFacet.FACET_MAXEXCLUSIVE.equals(facetName))
        restrictions.setMaxExclusive(readXmlValue(xsRrestriction, facetValue));
      
      else
        log.warn("Unsupported Facet: " + facetName);
    }
  }
  
  private XmlForm elementDeclComplex(XSElementDecl decl, XSComplexType complexType) {
    if (log.isDebugEnabled())
      log.debug("Complex Type: " + complexType);

    List<Form> forms = new ArrayList<Form>();

    // Attributes
    Map<String, TextWriter> attrWriters = new LinkedHashMap<String, TextWriter>(); 
    Map<String, TextHandler> attrReaders = new LinkedHashMap<String, TextHandler>(); 
    Collection<? extends XSAttributeUse> attrs = complexType.getAttributeUses();
    attributes(forms, attrWriters, attrReaders, attrs);

    // Body
    XmlWriter bodyWriter = null;
    XmlReader bodyReader = null;
    {
      XSContentType contentType = complexType.getContentType();
      XmlForm xmlForm = contentType.apply(this);
      if (xmlForm != null) {
        Form form = xmlForm.getForm();
        if (form instanceof FormSequence)
          forms.addAll(((FormSequence) form).getChildren());
        else if (form != null)
          forms.add(form);

        bodyWriter = xmlForm.getWriter();
        bodyReader = xmlForm.getReader();
        
        if (bodyReader != null)
          bodyReader = WhitespaceReader.prependTo(bodyReader);
      }
    }

    // Construct the XmlForm
    Form form = FormSequenceImpl.newInstance(forms);
    if (form != null)
      form = new FormSectionImpl<Form>(decl.getName(), form);

    XmlWriter writer = new XmlElementWriter(decl.getName(), attrWriters, bodyWriter);
    
    XmlReader reader = new XmlElementReader(decl.getName(), attrReaders, bodyReader);
    
    if (ignoreWhitespace)
      reader = WhitespaceReader.appendTo(reader);

    return new XmlFormImpl(form, writer, reader);
  }

  private void attributes(List<Form> forms,
      Map<String, TextWriter> attrWriters,
      Map<String, TextHandler> attrReaders,
      Collection<? extends XSAttributeUse> attrs) {
    
    for (XSAttributeUse use : attrs) {
      XmlForm xmlForm = use.apply(this);
      if (xmlForm != null) {
        XSAttributeDecl attr = use.getDecl();
        String name = attr.getName();

        Form form = xmlForm.getForm();
        if (form != null)
          forms.add(form);

        XmlWriter writer = xmlForm.getWriter();
        if (writer instanceof TextXmlWriter) {
          TextWriter textWriter = ((TextXmlWriter) writer).getWriter();
          attrWriters.put(name, textWriter);
        }
        
        XmlReader reader = xmlForm.getReader();
        TextHandler handler = null;
        if (reader instanceof TextXmlReader) {
          TextXmlReader textXmlReader = (TextXmlReader) reader;
          handler = textXmlReader.getHandler();
        } else if (reader != null)
          throw new IllegalStateException("XmlReader " + reader + " cannot be assigned to an XML attribtue, TextXmlReader expected");
        if (handler != null)
          attrReaders.put(name, handler);
      }
    }
  }
  
  // #################### Particle ####################

  public XmlForm particle(XSParticle particle) {
    final XSTerm term = particle.getTerm();
    
    // Exactly one item
    if (particle.getMinOccurs() == 1 && particle.getMaxOccurs() == 1) 
      return term.apply(XmlFormBuilder.this);
    
    Factory<XmlForm> factory = new Factory<XmlForm>() {
      public XmlForm create() {
        return term.apply(XmlFormBuilder.this);
      }
    };
    
    if (log.isDebugEnabled()) {
      StringBuffer sb = new StringBuffer();
      sb.append("Particle Occurs: ");
      sb.append(particle.getMinOccurs());
      sb.append(" to ");
      if (particle.getMaxOccurs() == XSParticle.UNBOUNDED)
        sb.append("unbounded");
      else
        sb.append(particle.getMaxOccurs());
      log.debug(sb.toString());
    }
    
    // A fixed number of items
    if (particle.getMinOccurs() == particle.getMaxOccurs())
      return repeatFixed(factory, particle.getMinOccurs());
    
    // Dynamic number of items 
    Integer min = particle.getMinOccurs();
    Integer max = XSParticle.UNBOUNDED == particle.getMaxOccurs() ? null : Integer.valueOf(particle.getMaxOccurs());
    return repeatDynamic(factory, min, max);
  }

  private XmlForm repeatFixed(Factory<XmlForm> factory, int count) {
    List<Form> forms = new ArrayList<Form>(); 
    List<XmlWriter> writers = new ArrayList<XmlWriter>();
    List<XmlReader> readers = new ArrayList<XmlReader>();
    
    if (ignoreWhitespace)
      readers.add(WhitespaceReader.getIInstance());
    
    for (int i = 0; i < count; i++) {
      XmlForm xmlForm = factory.create();
      
      if (xmlForm != null) {
        Form form = xmlForm.getForm();
        if (form != null)
          forms.add(form);
        
        XmlWriter writer = xmlForm.getWriter();
        if (writer != null)
          writers.add(writer);
        
        XmlReader reader = xmlForm.getReader();
        if (reader != null)
          readers.add(reader);
      }
    }
    
    Form form = FormSequenceImpl.newInstance(forms);
    XmlWriter writer = SequenceWriter.newInstance(writers);
    XmlReader reader = SequenceReader.newInstance(readers);
    
    return new XmlFormImpl(form, writer, reader);
  }
  
  private XmlForm repeatDynamic(Factory<XmlForm> factory, Integer min, Integer max) {
    RepeatAdapterImpl adapter = new RepeatAdapterImpl(factory);
    FormRepeat<Form> form = new FormRepeatImpl<Form>(adapter, min, max);
    adapter.init(form);
    
    XmlWriter writer = new RepeatWriter(adapter);
    XmlReader reader = new RepeatReader(form, adapter);
    
    if (ignoreWhitespace)
      reader = WhitespaceReader.prependTo(reader);
    
    // Add minimum number of instances
    for (int i = 0; i < min; i++)
      form.add();
    
    form.setHeaderFactory(new SequenceHeaderFactory(form));
    
    return new XmlFormImpl(form, writer, reader);
  }
  
  // #################### Model Group ####################
  
  public XmlForm modelGroup(XSModelGroup group) {
    List<Form> forms = new ArrayList<Form>(); 
    List<XmlWriter> writers = new ArrayList<XmlWriter>();
    List<XmlReader> readers = new ArrayList<XmlReader>();
    
    XSParticle[] children = group.getChildren();
    for (int i = 0; i < children.length; i++) {
      XmlForm xmlForm = children[i].apply(this);
      if (xmlForm != null) {
        Form form = xmlForm.getForm();
        if (form != null) {
          forms.add(form);
          writers.add(xmlForm.getWriter());
          readers.add(xmlForm.getReader());
        }
      }
    }
    
    Compositor compositor = group.getCompositor();
    
    if (log.isDebugEnabled())
      log.debug("Compositor: " + compositor);
    
    Form form;
    XmlWriter writer;
    XmlReader reader;
    
    switch (compositor) {
    case SEQUENCE:
      form = FormSequenceImpl.newInstance(forms, true);
      writer = SequenceWriter.newInstance(writers);
      reader = SequenceReader.newInstance(readers);
      break;
    case CHOICE:
      FormChoice choice = new FormChoiceImpl(forms);
      writer = new ChoiceWriter(choice, writers);
      reader = new ChoiceReader(choice, readers);
      form = choice;
      break;
    case ALL:
      FormAll all = new FormAllImpl(forms, true);
      writer = new AllWriter(all, writers);
      reader = new AllReader(all, readers);
      form = all;
      break;
    default:
      throw new AssertionError("Unknown Compositor: " + compositor);
    }
    
    if (ignoreWhitespace)
      reader = WhitespaceReader.appendTo(reader);
    
    return new XmlFormImpl(form, writer, reader);    
  }
  
  // #################### Attributes ####################
  
  public XmlForm attributeDecl(XSAttributeDecl decl) {
    if (log.isDebugEnabled())
      log.debug("Attribute Declaration: " + decl);
    
    FormElement element = declSimple(decl.getType(), decl.getName(), decl.getDefaultValue(), decl.getFixedValue());
    if (element == null)
      return null;
    
    XmlWriter writer = new TextXmlWriter(new FormElementWriter(element));
    XmlReader reader = new TextXmlReader(new FormElementHandler(element));

    return new XmlFormImpl(element, writer, reader);
  }

  public XmlForm attributeUse(XSAttributeUse use) {
    boolean required = use.isRequired();
    
    if (log.isDebugEnabled())
      log.debug("Attribute Use: " + use +
          ", Required: " + required);
    
    XSAttributeDecl decl = use.getDecl();

    XmlForm xmlForm = decl.apply(this);

    if (required) {
      Form form = xmlForm.getForm();
      if (form instanceof FormElement<?>)
        ((FormElement<?>) form).setRequired(true);
    }
    
    return xmlForm;
  }

  // #################### Methods not implemented ####################

  public XmlForm simpleType(XSSimpleType simpleType) {
    if (log.isDebugEnabled())
      log.debug("Simple Type: " + simpleType);

    return null;
  }

  public XmlForm complexType(XSComplexType type) {
    if (log.isDebugEnabled())
      log.debug("Complex Type: " + type);
    
    return null;
  }
  
  public XmlForm modelGroupDecl(XSModelGroupDecl decl) {
    if (log.isDebugEnabled())
      log.debug("Model Group Declaration: " + decl);

    return null;
  }

  public XmlForm wildcard(XSWildcard wc) {
    if (log.isDebugEnabled())
      log.debug("Wildcard: " + wc);

    return null;
  }

  public XmlForm empty(XSContentType empty) {
    if (log.isDebugEnabled())
      log.debug("Empty: " + empty);
    
    return null;
  }

  public XmlForm annotation(XSAnnotation ann) {
    if (log.isDebugEnabled())
      log.debug("Annotation: " + ann);

    return null;
  }

  public XmlForm attGroupDecl(XSAttGroupDecl decl) {
    if (log.isDebugEnabled())
      log.debug("Attribute Group Declaration: " + decl);

    return null;
  }

  public XmlForm facet(XSFacet facet) {
    if (log.isDebugEnabled())
      log.debug("Facet: " + facet);

    return null;
  }

  public XmlForm identityConstraint(XSIdentityConstraint decl) {
    if (log.isDebugEnabled())
      log.debug("Identity Constraint: " + decl);

    return null;
  }

  public XmlForm notation(XSNotation notation) {
    if (log.isDebugEnabled())
      log.debug("Notation: " + notation);

    return null;
  }

  public XmlForm schema(XSSchema schema) {
    if (log.isDebugEnabled())
      log.debug("Schema: " + schema);
    
    return null;
  }

  public XmlForm xpath(XSXPath xpath) {
    if (log.isDebugEnabled())
      log.debug("XPath: " + xpath);
    
    return null;
  }

}
