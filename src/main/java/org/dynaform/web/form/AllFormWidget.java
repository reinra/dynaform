package org.dynaform.web.form;

import org.dynaform.xml.form.layout.SectionStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * All form composition widget.
 * 
 * @author Rein Raudjärv
 */
public class AllFormWidget extends BaseFormWidget {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(AllFormWidget.class);

  private final static boolean SHOW_INDEXES = false;

  private final boolean allChildrenRequired;

  private final List<UiForm> children = new ArrayList<UiForm>();

  private int[] selectedIndexes;
  private int[] unselectedIndexes;

  private AllEventListener listener;

  public AllFormWidget(boolean allChildrenRequired) {
    this.allChildrenRequired = allChildrenRequired;
  }

  public void addChild(UiForm form) {
    children.add(form);
  }

  public void setListener(AllEventListener listener) {
    this.listener = listener;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("form/all");

    // Register child forms
    int i = 0;
    for (UiForm child : children)
      addWidget(String.valueOf(i++), child);
  }

  public boolean getAllChildrenRequired() {
    return allChildrenRequired;
  }

  public boolean isShowIndexes() {
    return SHOW_INDEXES;
  }  

  public int[] getSelectedIndexes() {
    return selectedIndexes;
  }

  public int getSelectedCount() {
    return selectedIndexes.length;
  }

  public int[] getUnselectedIndexes() {
    return unselectedIndexes;
  }

  public void setSelectedIndexes(int[] indexes) {
    this.selectedIndexes = indexes;
  }

  public void setUnselectedIndexes(int[] indexes) {
    this.unselectedIndexes = indexes;
  }

  public SectionStyle getOrientation() {
    return getLayoutCtx().getSectionStyle();
  }

  public boolean isInsideTable() {
    return getLayoutCtx().isTableRow();
  }

  private List<UiForm> getSelectedChildren() {
    List<UiForm> result = new ArrayList<UiForm>();
    for (int index : selectedIndexes)
      result.add(children.get(index));
    return result;
  }

  public boolean saveAndValidate() {
    boolean valid = true;
    for (UiForm form : getSelectedChildren())
      valid &= form.saveAndValidate();
    return valid;
  }

  // Specific events 

  public void handleEventMoveUp(String param) {
    moveUp(Integer.valueOf(param));
  }
  public void handleEventMoveDown(String param) {
    moveDown(Integer.valueOf(param));
  }
  public void handleEventEnable(String param) {
    enable(Integer.valueOf(param));
  }
  public void handleEventDisable(String param) {
    disable(Integer.valueOf(param));
  }

  private void moveUp(int indexDisplayed) {
    swap(indexDisplayed, indexDisplayed - 1);
  }
  private void moveDown(int indexDisplayed) {
    swap(indexDisplayed, indexDisplayed + 1);
  }
  private void swap(int indexDisplayed, int withIndexDisplayed) {
    if (log.isDebugEnabled())
      log.debug("Swapping selectedIndexes " + indexDisplayed + " and " + withIndexDisplayed);

    // Swap two selectedIndexes
    int[] newIndexes = Arrays.copyOf(selectedIndexes, selectedIndexes.length);
    int indexA = selectedIndexes[indexDisplayed];
    int indexB = selectedIndexes[withIndexDisplayed];
    newIndexes[indexDisplayed] = indexB;
    newIndexes[withIndexDisplayed] = indexA;
    listener.selectIndexes(newIndexes);
  }

  private void enable(int originalIndex) {
    // Add new index
    int[] newIndexes = Arrays.copyOf(selectedIndexes, selectedIndexes.length + 1);
    newIndexes[selectedIndexes.length] = originalIndex;
    listener.selectIndexes(newIndexes);
  }

  private void disable(int indexDisplayed) {
    // Remove an index
    int[] newIndexes = ArrayUtils.remove(selectedIndexes, indexDisplayed);
    listener.selectIndexes(newIndexes);
  }

  /**
   * All form event listener.
   * 
   * @author Rein Raudjärv
   * 
   * @see AllFormWidget
   */
  public static interface AllEventListener {
    /**
     * Handle selectedIndexes set event.
     */
    void selectIndexes(int[] indexes);
  }

}
