package org.dynaform.web.form;

import org.dynaform.xml.form.layout.SectionStyle;

/**
 * Layout Context provides tells
 * how the components should be rendered.
 * 
 * @author Rein Raudjärv
 */
public interface LayoutContext {

  /**
   * @return parent context.
   */
  LayoutContext getParentContext();
  
  /**
   * @return current section style.
   */
  SectionStyle getSectionStyle();
  
  /**
   * @return whether we are inside a table row
   * (but not in a table cell yet).
   */
  boolean isTableRow();
  
}
