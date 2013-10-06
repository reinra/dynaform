package org.dynaform.dynadata;

import org.dynaform.dynadata.selector.FullSelector;
import org.dynaform.dynadata.selector.HighLevelIdSelector;

import org.dynaform.xml.form.BaseFormVisitor;
import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.form.FormRepeat;
import org.dynaform.xml.form.FormSection;
import org.dynaform.xml.form.FormRepeat.RepeatChangeListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Rein Raudjärv
 * 
 * @see DynaDataAttributes
 */
public class DynaDataImpl extends BaseFormVisitor implements DynaData {
  
  private static final Log log = LogFactory.getLog(DynaDataImpl.class);
  
  private static final String METADATA_SEGMENT_PREFIX = "@";

  private static final String CUSTOMIZED = "CUSTOMIZED";
  private static final String BROKEN = "BROKEN";
  private static final String GENERATED = "GENERATED";
  
  private final Form form;
  
  private final MappingCollection correct = new MappingSet();
  private final MappingCollection broken = new MappingSet();
  
  private final Map<String, DynaDataAttributes> generatedByFullId = new LinkedHashMap<String, DynaDataAttributes>();
  private final Map<String, Map<String,DynaDataAttributes>> generatedByIdAndFullId = new LinkedHashMap<String, Map<String,DynaDataAttributes>>();
  private final Map<String, DynaDataAttributes> generatedById = new LinkedHashMap<String, DynaDataAttributes>();
  
  public DynaDataImpl(Form form) {
    this.form = form;
    visit();
  }

  public void read(Reader in) {
    clear();
    try {
      doRead(new BufferedReader(in));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    visit();
  }

  public void write(Writer out) {
    doWrite(new PrintWriter(out));
  }
  
  private void visit() {
    correct.resetUsage();
    clearGenerated();
    form.accept(this);
    handleUnused();
    postProcessGenerated();
  }

  private void clearGenerated() {
    generatedByFullId.clear();
    generatedByIdAndFullId.clear();
    generatedById.clear();
  }

  private void handleUnused() {
    Collection<Mapping> unused = correct.getUnused();
    broken.addAll(unused);
    correct.removeAll(unused);
  }
  
  private void postProcessGenerated() {
    for (Entry<String, Map<String, DynaDataAttributes>> entry : generatedByIdAndFullId.entrySet()) {
      String id = entry.getKey();
      if (id != null) {
        Map<String, DynaDataAttributes> byFullId = entry.getValue();
        if (byFullId.size() > 1)
          log.debug(byFullId.size() + " results for '" + id + "'");
        DynaDataAttributes attrs = null;
        for (DynaDataAttributes localAttrs : byFullId.values()) {
          if (attrs == null)
            attrs = localAttrs;
          else
            attrs = attrs.intersection(localAttrs);
        }
        generatedById.put(id, attrs);
      }
    }
  }

  // Visiting form
  
  private static DynaDataAttributes merge(Collection<Mapping> mappings) {
    if (mappings == null || mappings.isEmpty())
      return null;
    
    Iterator<Mapping> it = mappings.iterator();
    DynaDataAttributes attrs = it.next().getAttributes();
    while (it.hasNext()) {
      DynaDataAttributes newAttrs = it.next().getAttributes();
      attrs = attrs.merge(newAttrs);
    }
    return attrs;
  }
  
  private void form(Form form) {
    boolean generalMatch = false;
    boolean fullMatch = false;
    
    Collection<Mapping> metadata = correct.get(form);
    if (metadata != null) {
      // Existing customized form element
      broken.removeAll(metadata);
      merge(metadata).visit(form);
      
      generalMatch = containsGeneralSelector(metadata);
      fullMatch = containsFullSelector(metadata);
    }
    
    if (metadata == null) {
      metadata = broken.get(form);
      if (metadata != null) {
        // Broken customized form element is back
        log.info("Broken element is back: " + form);
        correct.addAll(metadata);
        broken.removeAll(metadata);
        merge(metadata).visit(form);
        
        generalMatch = containsGeneralSelector(metadata);
        fullMatch = containsFullSelector(metadata);
      }
    }
    
    if (!generalMatch) {
      generateById(form);

      if (!fullMatch)
        generateByFullId(form);
    }
  }
  
  private static boolean containsFullSelector(Collection<Mapping> mappings) {
    for (Mapping mapping : mappings) {
      if (mapping.getSelector() instanceof FullSelector)
        return true;
    }
    return false;
  }
  
  private static boolean containsGeneralSelector(Collection<Mapping> mappings) {
    for (Mapping mapping : mappings) {
      if (mapping.getSelector() instanceof HighLevelIdSelector)
        return true;
    }
    return false;
  }

  private void generateByFullId(Form form) {
    generate(form, generatedByFullId, form.getFullSelector());
  }
  
  private void generateById(Form form) {
    String fullSelector = form.getFullSelector();
    String descendantOrSelfSelector = form.getDescendantOrSelfSelector();
    Map<String, DynaDataAttributes> byFullId = generatedByIdAndFullId.get(descendantOrSelfSelector);
    if (byFullId == null) {
      byFullId = new LinkedHashMap<String, DynaDataAttributes>();
      generatedByIdAndFullId.put(descendantOrSelfSelector, byFullId);
    }
    generate(form, byFullId, fullSelector);
  }

  private void generate(Form form, Map<String, DynaDataAttributes> metadata, String fullSelector) {
    DynaDataAttributes existingMapping = metadata.get(fullSelector);
    DynaDataAttributes mapping = DynaDataAttributes.newInstance(form);
    if (existingMapping != null)
      mapping = existingMapping.merge(mapping);
    metadata.put(fullSelector, mapping);
    
    if (log.isDebugEnabled())
      log.debug("Generated for " + form + ":" + mapping.getParams());
  }
  
  public <E> void element(FormElement<E> element) {
    form(element);
  }

  public <F extends Form> void section(FormSection<F> section) {
    form(section);
    
    // Visit content
    super.section(section);
  }
  
  public <F extends Form> void repeat(final FormRepeat<F> repeat) {
    form(repeat);
    
    List<F> children = repeat.getChildren();
    if (children.isEmpty()) {
      // Create a temporary child
      F child = null;
      try {
        child = repeat.add();
        child.accept(this);
      }
      finally {
        if (child != null)
          repeat.remove(child);
      }
    } else {
      // Visit all children
      super.repeat(repeat);
    }
    
    repeat.removeListener(REPEAT_CHANGE_LISTENER);
    repeat.addListener(REPEAT_CHANGE_LISTENER);
  }
  
  private final RepeatChangeListener REPEAT_CHANGE_LISTENER = new RepeatChangeListener() {
    public void onAdd(Form child) {
      // Process the new element
      child.accept(DynaDataImpl.this);
    }
    public void onRemove(Form child) {
      // Do nothing
    }
    public void onSetDisabled(FormRepeat sequence) {
      // Do nothing
    }
    public void onSetRepeatStyle(FormRepeat sequence) {
      // Do nothing
    }
  };
  
  // Writing

  private void doWrite(PrintWriter out) {
    writeSectionTitle(out, CUSTOMIZED);
    DynaDataUtil.write(out, correct);
    writeSectionTitle(out, BROKEN);
    DynaDataUtil.write(out, broken);
    writeSectionTitle(out, GENERATED);
    DynaDataUtil.write(out, generatedById);
    DynaDataUtil.write(out, generatedByFullId);
  }
  
  private static void writeSectionTitle(PrintWriter out, String section) {
    out.print(METADATA_SEGMENT_PREFIX);
    out.println(section);
  }
  
  // Reading
  
  private void clear() {
    correct.clear();
    broken.clear();
    clearGenerated();
  }
  
  private void doRead(BufferedReader in) throws IOException {
    MappingCollection mappings = null;
    String line;
    while ((line = in.readLine()) != null) {
      line = line.trim();
      if (line.isEmpty())
        continue;
      
      if (line.startsWith(METADATA_SEGMENT_PREFIX)) {
        String section = line.substring(1);
        
        if (CUSTOMIZED.equals(section))
          mappings = correct;
        if (BROKEN.equals(section))
          mappings = broken;
        if (GENERATED.equals(section))
          mappings = null;
      }
      
      else if (mappings != null)
        DynaDataUtil.parseLine(line, mappings);
    }
  }

}
