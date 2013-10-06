package org.dynaform.web.demo;

import org.dynaform.xml.XmlUtil;
import org.dynaform.xml.writer.XmlWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.data.StringData;

public class XmlEditWidget extends BaseUIWidget {
  
  private static final Log log = LogFactory.getLog(XmlEditWidget.class);

  private static final long serialVersionUID = 1L;

  private final FormWidget form = new FormWidget();

  protected void init() throws Exception {
    setViewSelector("xmlEdit");
    form.addElement("xml", "#XML", new TextareaControl(), new StringData(), getXml(), true);
    addWidget("f", form);
  }

  private String getXml() {
    XmlWriter writer = getContext().getXmlForm().getWriter();
    return XmlUtil.writeXml(writer);
  }

  private SchemaContext getContext() {
    return (SchemaContext) getEnvironment().requireEntry(SchemaContext.class);
  }

  public void handleEventXsdView() {
    if (storeData())
      getContext().selectSchemaView();
  }

  public void handleEventFormEdit() {
    if (storeData())
      getContext().selectFormEdit();
  }
  
  public void handleEventMetadataEdit() {
    if (storeData())
      getContext().selectMetadataEdit();
  }

  public void handleEventSave() {
    if (storeData())
      getMessageCtx().showInfoMessage("Data saved successfully.");
  }

  public void handleEventReset() {
    form.setValueByFullName("xml", getXml());
    getMessageCtx().showInfoMessage("Data was reset successfully.");
  }

  private boolean storeData() {
    if (form.convertAndValidate()) {
      String xml = (String) form.getValueByFullName("xml");
      try {
        if (log.isDebugEnabled())
          log.debug("Starting to read XML");
        XmlUtil.readXml(xml, getContext().getXmlForm().getReader());
        if (log.isDebugEnabled())
          log.debug("Finished reading XML");
        return true;
      } catch (RuntimeException e) {
        log.error("Could not read the XML", e);
        getMessageCtx().showErrorMessage("Could not read the XML, see log for details");
      }
    }
    return false;
  }

}
