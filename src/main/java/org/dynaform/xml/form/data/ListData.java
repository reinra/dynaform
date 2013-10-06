package org.dynaform.xml.form.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ListData<E> implements Data<List<E>> {

  private static final long serialVersionUID = 1L;

  private final Data<E> itemData;
  
  private List<E> value;
  
  public ListData(Data<E> itemData) {
    if (itemData == null)
      throw new IllegalArgumentException("Item data cannot be null");
      
    this.itemData = itemData;
  }

  @SuppressWarnings("unchecked")
  public Class<List<E>> getType() {
    return (Class) List.class;
  }

  public List<E> getValue() {
    return value;
  }
  
  public void setValue(List<E> value) {
    this.value = value;
  }

  public String getXmlValue() {
    if (value == null)
      return null;
    
    StringBuilder sb = new StringBuilder();
    for (Iterator<E> it = value.iterator(); it.hasNext();) {
      E item = (E) it.next();
      itemData.setValue(item);
      sb.append(itemData.getXmlValue());
      if (it.hasNext())
        sb.append(" ");
    }
    return sb.toString();
  }

  public void setXmlValue(final String xmlValue) {
    if (xmlValue == null) {
      this.value = null;
      return;
    }
    
    StringTokenizer st = new StringTokenizer(xmlValue, " ");
    List<E> result = new ArrayList<E>();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      itemData.setXmlValue(token);
      E value = itemData.getValue();
      result.add(value);
    }
    this.value = result;
  }

}
