package org.dynaform.web.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.data.StringData;

public class DynaDataEditWidget extends BaseUIWidget {
  
  private static final Log log = LogFactory.getLog(DynaDataEditWidget.class);

  private static final long serialVersionUID = 1L;

  private final FormWidget form = new FormWidget();

  protected void init() throws Exception {
    setViewSelector("metadataEdit");
    form.addElement("metadata", "#MetadataProcessor", new TextareaControl(), new StringData(), readMetadata(), true);
    addWidget("f", form);
  }
  
  private SchemaContext getContext() {
    return (SchemaContext) getEnvironment().requireEntry(SchemaContext.class);
  }
  
  private String readMetadata() {
    StringWriter s = new StringWriter();
    getContext().getDynaData().write(s);
    String metadata = s.getBuffer().toString();
//    log.info("MetadataProcessor: " + metadata);
    return metadata;
  }

  public void handleEventXsdView() {
    if (storeData())
      getContext().selectSchemaView();
  }

  public void handleEventFormEdit() {
    if (storeData())
      getContext().selectFormEdit();
  }
  
  public void handleEventXmlEdit() {
    if (storeData())
      getContext().selectXmlEdit();
  }
  
  public void handleEventSave() {
    if (storeData())
      getMessageCtx().showInfoMessage("DynaData applied successfully.");
  }

  public void handleEventReset() {
    form.setValueByFullName("metadata", readMetadata());
    getMessageCtx().showInfoMessage("DynaData was reset successfully.");
  }
  
  private boolean storeData() {
    if (form.convertAndValidate()) {
      String metadata = (String) form.getValueByFullName("metadata");
      log.info("DynaData: " + metadata);

      try {
        log.info("Starting to read metadata");
        getContext().getDynaData().read(new StringReader(metadata));
        log.info("Finished reading metadata");
        
        log.info("Storing metadata");
        saveMetadata(metadata);
        form.setValueByFullName("metadata", readMetadata());
        log.info("Finished storing metadata");
        return true;
      } catch (RuntimeException e) {
        log.error(e.getMessage(), e);
        getMessageCtx().showErrorMessage("Could not read the metadata, see log for details");
      }
    }
    return false;
  }
  
  private void saveMetadata(String metadata) {
    File file = getContext().getDynaDataFile();
    if (file == null)
      return;
    
    Writer writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(file));
      writer.write(metadata);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    finally {
      if (writer != null)
        try {
          writer.close();
        } catch (IOException e) {
        }
    }
  }

}
