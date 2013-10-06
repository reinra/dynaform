package org.dynaform.web.form;

import org.dynaform.xml.form.layout.SectionStyle;

/**
 * @author Rein Raudjärv
 */
public class LayoutUtil {

  public static SectionStyle getInheritedSectionStyle(LayoutContext layoutCtx) {
    SectionStyle sectionStyle = layoutCtx.getSectionStyle();
    while(true) {
      sectionStyle = layoutCtx.getSectionStyle();
      if (sectionStyle.isInheritable())
        return sectionStyle;
      layoutCtx = layoutCtx.getParentContext();
    }
  }
  
}
