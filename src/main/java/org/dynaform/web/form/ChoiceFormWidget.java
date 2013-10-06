package org.dynaform.web.form;

import org.dynaform.xml.form.layout.SectionStyle;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Choice form composition widget.
 * 
 * @author Rein Raudjärv
 */
public class ChoiceFormWidget extends BaseFormWidget {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(ChoiceFormWidget.class);

  private final List<UiForm> children = new ArrayList<UiForm>();

  private int selectedIndex;

  /**
   * Event listener for handling choice selection events.
   */
  private ChoiceEventListener listener;

  public void addChild(UiForm form) {
    children.add(form);
  }

  public void setListener(ChoiceEventListener listener) {
    this.listener = listener;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("form/choice");

    // Register child forms
    int i = 0;
    for (UiForm child : children)
      addWidget(String.valueOf(i++), child);
  }

  public SectionStyle getOrientation() {
    return getLayoutCtx().getSectionStyle();
  }

  public boolean isInsideTable() {
    return getLayoutCtx().isTableRow();
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  private UiForm getSelectedChild() {
    return children.get(selectedIndex);
  }

  public boolean saveAndValidate() {
    return getSelectedChild().saveAndValidate();
  }

  public void handleEventSelect(String param) {
    listener.selectIndex(Integer.parseInt(param));
  }

  public void selectIndex(int index) {
    selectedIndex = index;
  }

  /**
   * Choice form event listener.
   * 
   * @author Rein Raudjärv
   * 
   * @see ChoiceFormWidget
   */
  public static interface ChoiceEventListener {
    /**
     * Handle choice selection event.
     */
    void selectIndex(int index);
  }

}
