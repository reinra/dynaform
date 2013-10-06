package org.dynaform.xml.form.layout;

public enum SectionStyle {
  
  HORIZONTAL(true, false),
  VERTICAL(true, true),
  
  ROW(false, false),
  COLUMN(false, true),
  
  INHERIT(true, false);
  
  public static final SectionStyle DEFAULT = INHERIT;
  
  private final boolean inheritable;
  private final boolean vertical;
  
  private SectionStyle(boolean inheritable, boolean vertical) {
    this.inheritable = inheritable;
    this.vertical = vertical;
  }
  
  public boolean isInheritable() {
    return inheritable;
  }
  public boolean isVertical() {
    return vertical;
  }
  
}
