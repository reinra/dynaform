package org.dynaform.web.demo;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.araneaframework.OutputData;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.core.BaseMenuWidget;
import org.araneaframework.uilib.core.MenuItem;

public class MenuWidget extends BaseMenuWidget {

  private static final long serialVersionUID = 1L;

  public MenuWidget() throws Exception {
    super(null);
  }

  protected MenuItem buildMenu() throws Exception {
    MenuItem result = new MenuItem();
    result.addMenuItem(new MenuItem("#Schemas", SchemaListWidget.class));
    return result;
  }

  protected void renderExceptionHandler(OutputData output, Exception e) throws Exception {
    if (ExceptionUtils.getRootCause(e) != null) {
      putViewDataOnce("rootStackTrace", 
          ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(e)));
    }        
    putViewDataOnce("fullStackTrace", ExceptionUtils.getFullStackTrace(e)); 

    ServletUtil.include("/WEB-INF/jsp/menuError.jsp", this, output);		
  }
}
