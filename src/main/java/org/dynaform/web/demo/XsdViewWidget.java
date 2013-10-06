package org.dynaform.web.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.core.BaseUIWidget;

public class XsdViewWidget extends BaseUIWidget {

  private static final long serialVersionUID = 1L;

  private static final int TAB_WIDTH = 2;

  private String name;
  private String[] lines;

  public XsdViewWidget(File file) {
    this.name = file.getName();
    this.lines = getLines(file);
    replaceTabsWithSpaces(lines, TAB_WIDTH);
  }

  protected void init() throws Exception {
    setViewSelector("xsdView");
  }

  private SchemaContext getContext() {
    return (SchemaContext) getEnvironment().requireEntry(SchemaContext.class);
  }

  public String getName() {
    return name;
  }

  public String[] getLines() {
    return lines;
  }

  public void handleEventFormEdit() {
    getContext().selectFormEdit();
  }
  
  public void handleEventXmlEdit() {
    getContext().selectXmlEdit();
  }
  
  public void handleEventMetadataEdit() {
    getContext().selectMetadataEdit();
  }

  private static String[] getLines(File file) {
    List<String> result = new ArrayList<String>();
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader(file));
      String line;
      while ((line = in.readLine()) != null) {
        result.add(line);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e1) {
          // ignore
        }
      }
    }
    return result.toArray(new String[result.size()]);
  }

  private static void replaceTabsWithSpaces(String[] lines, int tabWidth) {
    for (int i = 0; i < lines.length; i++) {
      lines[i] = replaceTabsWithSpaces(lines[i], tabWidth);
    }
  }	

  private static String replaceTabsWithSpaces(String line, int tabWidth) {
    String tab = StringUtils.repeat(" ", tabWidth);
    line = line.replace("\t", tab);
    return line;
  }

}
