package org.dynaform.web.demo;

import org.dynaform.dynadata.DynaData;
import org.dynaform.dynadata.DynaDataImpl;

import org.dynaform.web.form.builder.AraneaFormBuilder;
import org.dynaform.xml.SchemaUtil;
import org.dynaform.xml.XmlForm;
import org.dynaform.xml.XmlUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * @author Rein Raudjärv
 */
public class SchemaWidget extends BaseUIWidget implements SchemaContext {
	
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(SchemaWidget.class);
	
	private final File xsdFile;
	private final XmlForm xmlForm;
	private final StandardFlowContainerWidget flow = new StandardFlowContainerWidget();
	private final Widget form;
	
	private final File dynaDataFile;
	private DynaData dynaData;
	
	private boolean editMode = true;
	
	public SchemaWidget(File xsdFile, File dynaDataFile) {
    this.xsdFile = xsdFile;
    this.xmlForm = createXmlForm(xsdFile);
    this.form = new FormEditWidget(AraneaFormBuilder.build(xmlForm.getForm()));
    this.dynaDataFile = dynaDataFile;
    this.dynaData = new DynaDataImpl(xmlForm.getForm());
    loadMetadata();
  }
	
	public File getXsdFile() {
    return xsdFile;
  }
	
	public XmlForm getXmlForm() {
	  return xmlForm;
	}
	
	public File getDynaDataFile() {
	  return dynaDataFile;
	}
	
	public DynaData getDynaData() {
	  return dynaData;
	}
	
	private void loadMetadata() {
	  if (dynaDataFile == null || !dynaDataFile.isFile())
	    return;
	  
	  FileReader in = null;
	  try {
	    in = new FileReader(dynaDataFile);
	    dynaData.read(in);
	  } catch (IOException e) {
	    throw new RuntimeException(e);
    }
	  finally {
	    if (in != null)
        try {
          in.close();
        } catch (IOException e) {
        }
	  }
	}
	
  protected void init() throws Exception {
		setViewSelector("schema");
		addWidget("f", flow);
		flow.start(form);
	}
  
  @Override
  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(getEnvironment(), SchemaContext.class, this);
  }
  
  public void handleEventList() {
    getFlowCtx().cancel();
  }
  
  public void handleEventReload() {
    getFlowCtx().replace(new SchemaWidget(xsdFile, dynaDataFile));
  }
  
  public void handleEventDeleteMetadata() {
    if (getDynaDataFile().delete()) {
      reload();
      getMessageCtx().showInfoMessage("DynaData deleted successfully.");
    } else {
      getMessageCtx().showErrorMessage("Could not delette dynaData as it's not stored yet.");
    }
  }

  public void reload() {
    getFlowCtx().replace(new SchemaWidget(xsdFile, dynaDataFile));
  }
  
  public void selectSchemaView() {
    Widget widget = new XsdViewWidget(xsdFile);
    
    if (editMode)
      flow.start(widget);
    else
      flow.replace(widget);
    
    editMode = false;
  }

  public void selectFormEdit() {
    if (!editMode)
      flow.cancel();
    else {
      try {
        String xml = XmlUtil.writeXml(xmlForm.getWriter());
        XmlUtil.readXml(xml, xmlForm.getReader());
      } catch (RuntimeException e) {
        log.error("Could not reset the form: " + e.getMessage(), e);
      }
    }
    
    editMode = true;
  }

  public void selectXmlEdit() {
    Widget widget = new XmlEditWidget();
    
    if (editMode)
      flow.start(widget);
    else
      flow.replace(widget);
    
    editMode = false;
  }
  
  public void selectMetadataEdit() {
    Widget widget = new DynaDataEditWidget();
    
    if (editMode)
      flow.start(widget);
    else
      flow.replace(widget);
    
    editMode = false;
  }
  
  private static XmlForm createXmlForm(File file) {
    XmlForm xmlForm = SchemaUtil.toXmlForm(file);
    
    if (log.isDebugEnabled())
      xmlForm.getForm().accept(DebugVisitor.getInstance());
    
    return xmlForm;
  }
	
}
