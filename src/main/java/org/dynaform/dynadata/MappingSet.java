package org.dynaform.dynadata;

import org.dynaform.xml.form.Form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MappingSet implements MappingCollection {

  private final Map<String, Mapping> allMappings = new LinkedHashMap<String, Mapping>();
  private final Map<String, Mapping> usedMappings = new LinkedHashMap<String, Mapping>();
  
  public void clear() {
    allMappings.clear();
    usedMappings.clear();
  }
  
  public void add(Mapping mapping) {
    allMappings.put(mapping.getSelector().getString(), mapping);
    usedMappings.put(mapping.getSelector().getString(), mapping);
  }

  public void addAll(Collection<Mapping> mappings) {
    for (Mapping mapping : mappings)
      add(mapping);
  }

  public Collection<Mapping> getAll() {
    return allMappings.values();
  }
  
  public void removeAll(Collection<Mapping> mappings) {
    for (Mapping mapping : mappings) {
      allMappings.remove(mapping.getSelector().getString());
      usedMappings.remove(mapping.getSelector().getString());
    }
  }

  public Collection<Mapping> get(Form form) {
    Collection<Mapping> result = new ArrayList<Mapping>();
    for (Mapping mapping : allMappings.values()) {
      if (mapping.getSelector().applies(form)) {
        result.add(mapping);
        usedMappings.put(mapping.getSelector().getString(), mapping);
      }
    }
    return result.isEmpty() ? null : result;
  }
  
  public void resetUsage() {
    usedMappings.clear();
  }

  public Collection<Mapping> getUnused() {
    Map<String, Mapping> result = new LinkedHashMap<String, Mapping>(allMappings);
    for (String key : usedMappings.keySet())
      result.remove(key);
    return result.values();
  }

}
