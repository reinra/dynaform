package org.dynaform.web.demo;

import java.io.File;
import java.io.FilenameFilter;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * @author Rein Raudjärv
 */
public class SchemaListWidget extends BaseUIWidget {

  private static final Log log = LogFactory.getLog(SchemaListWidget.class);

  private static final long serialVersionUID = 1L;

  private File[] files;

  protected void init() throws Exception {
    setViewSelector("schemaList");
    findFiles();
  }

  private void findFiles() {
    files = getSchemaDir().listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".xsd");
      }
    });
  }

  private File getSchemaDir() {
//    File currentDir = new File(".").getAbsoluteFile().getParentFile();
//    File dir = new File(currentDir.getParent(), "schema").getAbsoluteFile();
//    log.info("currentDir: " + currentDir);
    ServletContext sc = (ServletContext) getEnvironment().getEntry(ServletContext.class);
    File dir = new File(sc.getRealPath("/schema"));
//    File dir = new File("schema");
    log.info("Listing schemas from directory: " + dir);
    return dir;
  }

  public File[] getFiles() {
    return files;
  }

  public void handleEventRefresh() throws Exception {
    findFiles();
  }

  public void handleEventChoose(String param) throws Exception {
    choose(getFile(param));
  }

  private File getFile(String indexStr) {
    int index = Integer.parseInt(indexStr);
    File file = files[index];
    return file;
  }

  private void choose(File file) throws Exception {
    getFlowCtx().start(new SchemaWidget(file, getDynaDataFile(file)));
  }

  private File getDynaDataFile(File xsdFile) {
    return new File(xsdFile.getParentFile(), xsdFile.getName().replace(".xsd", ".dynadata"));
  }

}
