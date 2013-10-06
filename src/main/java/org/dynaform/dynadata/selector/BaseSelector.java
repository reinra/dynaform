package org.dynaform.dynadata.selector;


public abstract class BaseSelector implements Selector {

  @Override
  public String toString() {
    return getString();
  }
  
  @Override
  public int hashCode() {
    return getString().hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Selector))
      return false;
    return getString().equals(((Selector) obj).getString());
  }
  
}
