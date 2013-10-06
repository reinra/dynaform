package org.dynaform.web.form.builder;

import org.dynaform.web.form.AllFormWidget;
import org.dynaform.web.form.ChoiceFormWidget;
import org.dynaform.web.form.RepeatFormWidget;
import org.dynaform.web.form.SectionFormWidget;
import org.dynaform.web.form.SequenceFormWidget;
import org.dynaform.web.form.StandardFormWidget;
import org.dynaform.web.form.UiForm;
import org.dynaform.web.form.AllFormWidget.AllEventListener;
import org.dynaform.web.form.ChoiceFormWidget.ChoiceEventListener;
import org.dynaform.web.form.RepeatFormWidget.RepeatEventListener;
import org.dynaform.web.form.StandardFormWidget.ElementEventListener;
import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormAll;
import org.dynaform.xml.form.FormChoice;
import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.form.FormFunction;
import org.dynaform.xml.form.FormRepeat;
import org.dynaform.xml.form.FormSection;
import org.dynaform.xml.form.FormSequence;
import org.dynaform.xml.form.FormChoice.ChoiceChangeListener;
import org.dynaform.xml.form.FormElement.ElementChangeListener;
import org.dynaform.xml.form.FormSection.SectionChangeListener;
import org.dynaform.xml.form.restriction.Restrictions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.jsp.util.AutomaticFormElementUtil;
import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.constraint.AndConstraint;
import org.araneaframework.uilib.form.constraint.NumberInRangeConstraint;
import org.araneaframework.uilib.form.constraint.OptionalConstraint;
import org.araneaframework.uilib.form.constraint.StringLengthInRangeConstraint;

/**
 * Converts an abstract form into Aranea Widget.
 * 
 * @author Rein Raudjärv
 * 
 * @see Form
 * @see UiForm
 * @see ControlBuilder
 */
public class AraneaFormBuilder implements FormFunction<UiForm> {

  private static final Log log = LogFactory.getLog(AraneaFormBuilder.class);

  public static UiForm build(Form form) {
    try {
      log.debug("\n\n\n------- BUILDING ARANEA FORM -------");
      
      AraneaFormBuilder builder = new AraneaFormBuilder();
      UiForm result = form.apply(builder);
      
      log.debug("\n-----------------------------------\n\n");
      
      return result;
    } catch (Exception e) {
      throw new RuntimeException("Failed to convert XML form '" + form + "' into an Aranea widget", e);
    }
  }
  
  private AraneaFormBuilder() {
  }

  public <E> UiForm element(final FormElement<E> element) {
    if (log.isDebugEnabled())
      log.debug(element);
    
    AraneaControlEntry entry = ControlBuilder.getFormControl(element);

    if (log.isDebugEnabled()) {
      String originalType = element.getType().getSimpleName();
      String araneaType = entry.araneaType.getSimpleName();
      if (!originalType.equals(araneaType))
        log.debug("Type converted: " + originalType + " -> " + araneaType);
      
      String originalControl = element.getControl().getClass().getSimpleName();
      String araneaControl = entry.araneacontrol.getClass().getSimpleName();
      log.debug("Control converted: " + originalControl + " -> " + araneaControl);
    }
    
    org.araneaframework.uilib.form.Data araneaData = new org.araneaframework.uilib.form.Data(entry.araneaType);
    Converter converter = entry.converter;
    Object araneaValue = converter.convert(element.getValue());

    // Send events from Form to Widget and vice versa
    ElementAdapter adapter = new ElementAdapter(element, converter);
    element.addListener(adapter);
    
    final StandardFormWidget widget = new StandardFormWidget();
    
    final org.araneaframework.uilib.form.FormElement araneaElement = widget.addElement(
        ("#" + element.getLabel()),
        entry.araneacontrol,
        araneaData,
        araneaValue,
        element.isRequired(),
        adapter);
    
    adapter._setAraneaElement(araneaElement);
    
    // Initial values
    setUiTag(araneaElement, entry.tag, entry.tagAttributes);
    araneaElement.setDisabled(element.isReadOnly() || element.isDisabled());
    
    // Restrictions
    restrictions(element.getRestrictions(), araneaElement);

    return widget;
  }
  
  private static class ElementAdapter<E, A> implements ElementChangeListener<E>, ElementEventListener<A> {
    
    private final FormElement<E> element;
    private org.araneaframework.uilib.form.FormElement araneaElement;
    private Converter<E, A> converter;
    
    public ElementAdapter(
        FormElement<E> element,
        Converter<E, A> converter) {
      this.element = element;
      this.converter = converter;
    }
    
    public void _setAraneaElement(org.araneaframework.uilib.form.FormElement araneaElement) {
      this.araneaElement = araneaElement;
    }
    
    // Send events from Widget to Form

    public void setValue(A value) {
      if (!element.isReadOnly())
        element.setValue(converter.deconvert(value));
    }
    
    // Send events from Form to Widget
    
    public void onSetLabel(FormElement<E> element) {
      araneaElement.setLabel("#" + element.getLabel());
    }
    public void onSetValue(FormElement<E> element) {
      araneaElement.setValue(converter.convert(element.getValue()));
    }
    public void onSetReadOnly(FormElement<E> element) {
      araneaElement.setDisabled(element.isReadOnly() || element.isDisabled());
    }
    public void onSetRequired(FormElement<E> element) {
      araneaElement.setMandatory(element.isRequired());
    }
    public void onSetDisabled(FormElement<E> element) {
      araneaElement.setDisabled(element.isReadOnly() || element.isDisabled());
    }
    public void onSetControl(FormElement<E> element) {
      AraneaControlEntry entry = ControlBuilder.getFormControl(element);
      if (entry != null) {
        araneaElement.setControl(entry.araneacontrol);
        setUiTag(araneaElement, entry.tag, entry.tagAttributes);
        
        Converter oldConverter = converter;
        Converter newConverter = entry.converter;
        Object oldValue = araneaElement.getValue();
        Object newValue = newConverter.convert(oldConverter.deconvert(oldValue));
        araneaElement.getData().setControlValue(newValue);
        converter = newConverter;
      }
    }
    
  }
  
  private static void setUiTag(org.araneaframework.uilib.form.FormElement fe, String tag, Map<String, String> tagAttributes) {
    AutomaticFormElementUtil.setFormElementTag(fe, tag, tagAttributes);
  }
  
  private static void restrictions(Restrictions restrictions, org.araneaframework.uilib.form.FormElement araneaElement) {
    List<Constraint> constraints = toConstraints(restrictions);
    if (constraints == null)
      return;
    
    if (constraints.size() == 1)
      araneaElement.setConstraint(constraints.get(0));
    else
      araneaElement.setConstraint(new AndConstraint(constraints));
  }
  
  private static List<Constraint> toConstraints(Restrictions restrictions) {
    List<Constraint> constraints = new ArrayList<Constraint>();
    
    {
      Integer length = restrictions.getLength();
      if (length != null)
        constraints.add(new StringLengthInRangeConstraint(length, length));
    }
    
    {
      Integer minLength = restrictions.getMinLength();
      Integer maxLength = restrictions.getMaxLength();
      if (minLength != null || maxLength != null)
        constraints.add(new StringLengthInRangeConstraint(
            (minLength == null ? 0 : minLength),
            (maxLength == null ? Integer.MAX_VALUE : maxLength)));
    }
    
    {
      Object min = restrictions.getMinInclusive();
      Object max = restrictions.getMaxInclusive();
      if (min != null || max != null)
        constraints.add(new NumberInRangeConstraint(
            (min == null ? null : new BigInteger(min.toString())),
            (max == null ? null : new BigInteger(max.toString()))));
    }
    
    {
      Object min = restrictions.getMinExclusive();
      Object max = restrictions.getMaxExclusive();
      if (min != null || max != null)
        constraints.add(new NumberInRangeConstraint(
            (min == null ? null : new BigInteger(min.toString()).add(BigInteger.ONE)),
            (max == null ? null : new BigInteger(max.toString()).subtract(BigInteger.ONE))));
    }
    
    if (constraints.isEmpty())
      return null;
    
    // Wrap as Optional Constraints
    List<Constraint> result = new ArrayList<Constraint>();
    for (Constraint constraint : constraints)
      result.add(new OptionalConstraint(constraint));
    return result;
  }
  
  public <F extends Form> UiForm section(FormSection<F> section) {
    if (log.isDebugEnabled())
      log.debug(section);

    final SectionFormWidget widget = new SectionFormWidget(
        section.getLabel(),
        section.getSectionStyle(),
        section.getContent().apply(this));
    
    // Send events from Form to Widget 
    section.addListener(new SectionChangeListener<F>() {
      public void onSetLabel(FormSection<F> section) {
        widget.setLabel(section.getLabel());
      }
      public void onSetSectionStyle(FormSection<F> section) {
        widget.setOrientation(section.getSectionStyle());
      }
    });
    
    return widget;
  }

  public <F extends Form> UiForm repeat(final FormRepeat<F> repeat) {
    if (log.isDebugEnabled())
      log.debug(repeat);
    
    final RepeatFormWidget<UiForm> widdget
    = new RepeatFormWidget<UiForm>(
        repeat.getMin(),
        repeat.getMax(),
        repeat.getRepeatStyle(),
        repeat.getHeaderFactory());
    
    class Adapter implements RepeatEventListener, FormRepeat.RepeatChangeListener<F> {

      private final Map<String, F> formById = new HashMap<String, F>();
      private final Map<F, String> idByForm = new HashMap<F, String>();

      // Send events from Widget to Form
      
      public void add() {
        repeat.add();
      }
      public void remove(String id) {
        F form = formById.get(id);
        if (form == null)
          throw new AssertionError();

        repeat.remove(form);
      }

      // Send events from Form to Widget
      
      public void onAdd(F child) {
        UiForm widget = child.apply(AraneaFormBuilder.this);
        String id = widdget.addForm(widget);

        formById.put(id, child);
        idByForm.put(child, id);
      }
      public void onRemove(F child) {
        String id = idByForm.get(child);
        if (id == null)
          throw new AssertionError();

        widdget.removeForm(id);

        formById.remove(id);
        idByForm.remove(child);
      }
      
      public void onSetDisabled(FormRepeat<F> sequence) {
        widdget.setDisabled(sequence.isDisabled());
      }
      
      public void onSetRepeatStyle(FormRepeat<F> sequence) {
        widdget.setRepeatStyle(sequence.getRepeatStyle());
      }
    }

    Adapter adapter = new Adapter();
    widdget.setListener(adapter);
    repeat.addListener(adapter);

    // Process existing sub forms
    for (F form : repeat.getChildren()) {
      String id = widdget.addForm(form.apply(this));

      adapter.formById.put(id, form);
      adapter.idByForm.put(form, id);
    }
    
    // Initial values
    widdget.setDisabled(repeat.isDisabled());

    return widdget;
  }

  public UiForm sequence(final FormSequence sequence) {
    if (log.isDebugEnabled())
      log.debug(sequence);

    final SequenceFormWidget widget = new SequenceFormWidget();

    UiForm prevChild = null;
    for (Form form : sequence.getChildren()) {
      UiForm child = form.apply(this);

      if (prevChild instanceof StandardFormWidget
          && child instanceof StandardFormWidget) {
        
        if (log.isDebugEnabled())
          log.debug("Mergeing standard forms");

        ((StandardFormWidget) prevChild).merge((StandardFormWidget) child);
      } else {
        widget.addChild(child);
        prevChild = child;
      }
    }
    
    // In case of single item
    if (widget.getChildren().size() == 1) {
      if (log.isDebugEnabled())
        log.debug("Using the only child instead of sequence");
      return prevChild;
    }
    
    return widget;
  }
  
  public UiForm choice(final FormChoice choice) {
    if (log.isDebugEnabled())
      log.debug(choice);
    
    final ChoiceFormWidget widget = new ChoiceFormWidget();
    
    // Send events from Widget to Form
    widget.setListener(new ChoiceEventListener() {
      public void selectIndex(int index) {
        choice.setSelectedIndex(index);
      }
    });
    
    // Send events from Form to Widget
    choice.addListener(new ChoiceChangeListener() {
      public void onSetSelectedIndex(int index) {
        widget.selectIndex(index);
      }
    });
    
    // Add children
    for (Form form : choice.getChildren())
      widget.addChild(form.apply(this));
    
    // Select initial element
    widget.selectIndex(choice.getSelectedIndex());
    
    return widget;
  }

  public UiForm all(final FormAll all) {
    if (log.isDebugEnabled())
      log.debug(all);
    
    final AllFormWidget widget = new AllFormWidget(all.areAllChildrenRequired());
    
    // Send events from Widget to Form
    widget.setListener(new AllEventListener() {
      public void selectIndexes(int[] indexes) {
        all.setSelectedIndexes(indexes);
      }
    });
    
    // Send events from Form to Widget
    all.addListener(new FormAll.OnChangeListener() {
      public void onSetSelectedIndexes(int[] indexes) {
        widget.setSelectedIndexes(all.getSelectedIndexes());
        widget.setUnselectedIndexes(all.getUnselectedIndexes());
      }
    });
    
    // Add children
    for (Form form : all.getChildren())
      widget.addChild(form.apply(this));
    
    // Initial values
    widget.setSelectedIndexes(all.getSelectedIndexes());
    widget.setUnselectedIndexes(all.getUnselectedIndexes());
    
    return widget;
  }

}
