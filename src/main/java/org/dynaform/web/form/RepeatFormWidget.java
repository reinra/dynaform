package org.dynaform.web.form;

import org.dynaform.xml.form.label.HeaderFactory;
import org.dynaform.xml.form.layout.RepeatStyle;
import org.dynaform.xml.form.layout.SectionStyle;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;


/**
 * Form sequence.
 * 
 * @author Rein Raudjärv
 * @param <F> type of child form
 */
public class RepeatFormWidget<F extends UiForm> extends BaseFormWidget {

	private static final long serialVersionUID = 1L;

	/**
	 * Sub-forms by their Id. 
	 */
	private final Map<String, F> children = new LinkedHashMap<String, F>();
	
	/**
	 * Minimum number of children. 
	 */
	private final Integer min;

	/**
   * Maximum number of children. 
   */
	private final Integer max;
	
	private boolean disabled;
	
	private RepeatStyle repeatStyle;
	
	/**
	 * Headers factory.
	 */
	private final HeaderFactory headerFactory;
	
	/**
	 * Event listener for handling add and remove button events.
	 */
	private RepeatEventListener listener;
	
	public RepeatFormWidget(Integer min, Integer max, RepeatStyle repeatStyle, HeaderFactory headersFactory) {
		this.min = min;
		this.max = max;
		this.repeatStyle = repeatStyle;
		this.headerFactory = headersFactory;
	}
	
	public void setListener(RepeatEventListener listener) {
		this.listener = listener;
	}

	@Override
	protected void init() throws Exception {
		setViewSelector("form/repeat");
		
		for (Entry<String, F> entry : children.entrySet())
			addWidget(entry.getKey(), entry.getValue());
	}
	
  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(getEnvironment(), LayoutContext.class, new RepeatLayoutContext());
  }
  
  public boolean isDisabled() {
    return disabled;
  }
  
  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }
	
  public SectionStyle getOrientation() {
    return getLayoutCtx().getSectionStyle();
  }
  
  public void setRepeatStyle(RepeatStyle repeatStyle) {
    this.repeatStyle = repeatStyle;
  }
  
  public RepeatStyle getRepeatStyle() {
    return repeatStyle;
  }
  
  public boolean isInsideTable() {
    return getLayoutCtx().isTableRow();
  }
  
  public String getTitle() {
    return headerFactory.getHedaer();
  }
  
  public List<String> getHeaders() {
    return headerFactory.getSubHeaders();
  }
  
  public int getHeaderCount() {
    List<String> headers = getHeaders();
    return headers == null ? 1 : headers.size();
  }

	public Map<String, F> getChildren() {
		return children;
	}
	
	public int getSize() {
		return children.size();
	}
	
	public Integer getMin() {
		return min;
	}

	public Integer getMax() {
		return max;
	}
	
	public boolean saveAndValidate() {
    boolean valid = true;
    for (UiForm form : children.values())
      valid &= form.saveAndValidate();
    return valid;
	}
	
	public void handleEventAdd() {
		if (max != null && children.size() >= max)
			throw new IllegalStateException();

		listener.add();
	}
	
	public void handleEventRemove(String param) {
		if (min != null && children.size() <= min)
			throw new IllegalStateException();
		if (!children.containsKey(param))
			throw new IllegalArgumentException();
		
		listener.remove(param);
	}
	
	/**
	 * Repeat form event listener.
	 * 
	 * @author Rein Raudjärv
	 * 
	 * @see RepeatFormWidget
	 */
	public static interface RepeatEventListener {
		/**
		 * Handle add button event.
		 * 
		 * @see RepeatFormWidget#addForm(UiForm)
		 */
		void add();
		
		/**
		 * Handle remove button event.
		 * 
		 * @param id child id.
		 * 
		 * @see RepeatFormWidget#removeForm(String)
		 */
		void remove(String id);
	}
	
  /**
   * Next Id in the sequence.
   */
  private int nextId;
  
  /**
   * Generates a unique Id.
   * 
   * @return unique Id.
   */
  private String nextId() {
    return String.valueOf(nextId++);
  }
	
	/**
	 * Add new child.
	 * 
	 * @param form child.
	 * @return Id.
	 */
	public String addForm(F form) {
		String id = nextId();
		children.put(id, form);
		if (isInitialized())
			addWidget(id, form);
		
		return id;
	}
	
	/**
	 * Remove an existing child.
	 * 
	 * @param id child Id.
	 * @return child.
	 */
	public F removeForm(String id) {
		F form = children.remove(id);
		if (isInitialized())
			removeWidget(id);
		
		return form;
	}
	
	public class RepeatLayoutContext implements LayoutContext {

    public LayoutContext getParentContext() {
      return getLayoutCtx();
    }

    public SectionStyle getSectionStyle() {
      /*
       * Return a SectionStyle for children
       * if the corresponding RepeatStyle is specified.
       * <p>
       * <code>TABLE</code> is handled by
       * <code>isTableRow()</code> method.
       */
      if (RepeatStyle.ROWS.equals(repeatStyle))
        return SectionStyle.ROW;
      if (RepeatStyle.COLUMNS.equals(repeatStyle))
        return SectionStyle.COLUMN;
      
      /*
       * Use the SectionStyle inherited.
       */
      return LayoutUtil.getInheritedSectionStyle(getParentContext());
    }
    
    public boolean isTableRow() {
      return RepeatStyle.TABLE.equals(repeatStyle);
    }
	  
	}

}
