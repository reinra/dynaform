package org.dynaform.web.form;

import org.dynaform.xml.form.layout.SectionStyle;

import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;

/**
 * Section form widget.
 * 
 * @author Rein Raudjärv
 */
public class SectionFormWidget extends BaseFormWidget {

	private static final long serialVersionUID = 1L;
	
	private final UiForm form;
	private String label;
	private SectionStyle sectionStyle;
	
	public SectionFormWidget(String label, SectionStyle orientation, UiForm form) {
		this.label = label;
		this.sectionStyle = orientation;
		this.form = form;
	}
	
	@Override
	protected void init() throws Exception {
		setViewSelector("form/section");
		addWidget("0", form);
	}
	
  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(getEnvironment(), LayoutContext.class, new SectionLayoutContext());
  }
  
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
    this.label = label;
  }
	
	public void setOrientation(SectionStyle orientation) {
	  this.sectionStyle = orientation;
	}
	
  public boolean isInsideTable() {
    return getLayoutCtx().isTableRow();
  }
  
  private boolean isRepeated() {
    return getLayoutCtx() instanceof RepeatFormWidget.RepeatLayoutContext;
  }
  
  public boolean isTableContents() {
    return isRepeated() && getLayoutCtx().isTableRow();
  }
	
	public String getBgColor() {
	  String[] colors = getColors();
	  int size = getPathSize(getScope().toPath().toString());
	  int index = size % colors.length;
	  return colors[index];
	}
	
	public UiForm getForm() {
		return form;
	}
	
	public boolean saveAndValidate() {
	  return form.saveAndValidate();
	}
	
	private static int getPathSize(String path) {
	  int index = path.indexOf(".");
    return 1 + (index == -1 ? 0 : getPathSize(path.substring(index + 1)));
	}

  private static String[] getColors() {
    return new String[] {
        "#ffcccc",
        "#ffcc00",
        "#ccccff",
        "#ccffcc"
    };
  }
  
  private class SectionLayoutContext implements LayoutContext {

    public LayoutContext getParentContext() {
      return getLayoutCtx();
    }

    public SectionStyle getSectionStyle() {
      /*
       * Use the specified SectionStyle unless <code>INHERIT</code>. 
       */
       if (!SectionStyle.INHERIT.equals(sectionStyle))
          return sectionStyle;
       
       /*
        * If the parent is FormRepeat use the SectionStyle provided.
        */
       if (isRepeated())
         return getParentContext().getSectionStyle();
       
       /*
        * Use the SectionStyle inherited.
        */
       return LayoutUtil.getInheritedSectionStyle(getParentContext());
    }

    public boolean isTableRow() {
      return isTableContents();
    }
    
  }

}
