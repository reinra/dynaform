package org.dynaform.web.form;

import org.dynaform.xml.form.layout.SectionStyle;

/**
 * @author Rein Raudjärv
 * 
 * @see LayoutContext
 */
public class RootLayoutContext implements LayoutContext {
  
  public LayoutContext getParentContext() {
    throw new AssertionError("Root LayoutContext has no parent");
  }
  
  public SectionStyle getSectionStyle() {
    return SectionStyle.VERTICAL;
  }
  
  public boolean isTableRow() {
    return false;
  }
  
}