package org.dynaform.dynadata;

import org.dynaform.xml.form.Form;

import java.util.Collection;

public interface MappingCollection {
  
  void clear();
  
  void add(Mapping mapping);
  
  void addAll(Collection<Mapping> mappings);
  
  void removeAll(Collection<Mapping> mappings);
  
  Collection<Mapping> getAll();

  Collection<Mapping> get(Form form);
  
  void resetUsage();
  
  Collection<Mapping> getUnused();
  
}
