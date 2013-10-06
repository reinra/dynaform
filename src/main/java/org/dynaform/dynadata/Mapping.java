package org.dynaform.dynadata;

import org.dynaform.dynadata.selector.Selector;

/**
 * DynaData entry.
 * 
 * @author Rein Raudjärv
 * 
 * @see Selector
 * @see DynaDataAttributes
 */
public class Mapping {
  
  private final Selector selector;
  private final DynaDataAttributes attributes;
  
  public Mapping(Selector selector, DynaDataAttributes attributes) {
    this.selector = selector;
    this.attributes = attributes;
  }

  public Selector getSelector() {
    return selector;
  }

  public DynaDataAttributes getAttributes() {
    return attributes;
  }
  
}
